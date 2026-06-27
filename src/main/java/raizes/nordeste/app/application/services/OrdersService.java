package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.application.dto.CreateOrderRequest;
import raizes.nordeste.app.application.dto.UpdateOrderStatusRequest;
import raizes.nordeste.app.config.security.BearerTokenAuthentication;
import raizes.nordeste.app.domain.entities.*;
import raizes.nordeste.app.domain.exceptions.ProductOutOfStockException;
import raizes.nordeste.app.infra.repositories.*;
import raizes.nordeste.app.shared.exceptions.ConflictException;
import raizes.nordeste.app.shared.exceptions.ForbiddenException;
import raizes.nordeste.app.shared.exceptions.InvalidArgumentException;
import raizes.nordeste.app.shared.exceptions.NotFoundException;

import java.math.BigInteger;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final StockRepository stockRepository;
    private final UnitRepository unitRepository;
    private final UsersRepository usersRepository;
    private final LoyaltyService loyaltyService;
    private final PointsTransactionRepository pointsTransactionRepository;

    @Transactional
    public Order create(CreateOrderRequest request) {
        var auth = (BearerTokenAuthentication) SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null) {
            throw new ForbiddenException("You must be authenticated to order something.");
        }

        var user = auth.getUser();

        var unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new NotFoundException("Unit with this id not found."));

        var order = new Order();
        order.setUnit(unit);
        order.setUser(user);
        order.setCanalPedido(CanalPedido.valueOf(request.canalPedido().toUpperCase()));

        var items = request.items().stream().map(itemRequest -> {
            var stockItem = stockRepository.findByProductIdAndUnitId(itemRequest.productId(), request.unitId())
                    .orElseThrow(ProductOutOfStockException::new);

            if (stockItem.getAmountInStock() < itemRequest.quantity()) {
                throw new ProductOutOfStockException("There's no products available enough for your order.");
            }

            stockItem.setAmountInStock(stockItem.getAmountInStock() - itemRequest.quantity());

            return OrderItem.builder()
                    .order(order)
                    .stockItem(stockItem)
                    .quantity(itemRequest.quantity())
                    .build();
        }).toList();

        var total = items.stream()
                .map(i -> i.getStockItem().getPrice().multiply(BigInteger.valueOf(i.getQuantity())))
                .reduce(BigInteger.ZERO, BigInteger::add);

        order.setItems(new ArrayList<>(items));
        order.setSubTotal(total);
        order.setTotal(total);
        ordersRepository.save(order);

        if (request.pointsToApply() != null && request.pointsToApply() > 0) {
            if (user.getPoints() < request.pointsToApply()) {
                throw new ConflictException("Insufficient loyalty points.");
            }

            var discount = BigInteger.valueOf(request.pointsToApply());

            if (discount.compareTo(order.getTotal()) > 0) {
                throw new ConflictException("The discount cannot exceed the order total.");
            }

            order.setTotal(order.getTotal().subtract(discount));
            order.setDiscount(discount);
            user.setPoints(user.getPoints() - request.pointsToApply());
            usersRepository.save(user);

            var transaction = new PointsTransaction();
            transaction.setUser(user);
            transaction.setOrder(order);
            transaction.setType(PointsTransactionType.REDEEMED);
            transaction.setPoints(request.pointsToApply());
            pointsTransactionRepository.save(transaction);

            ordersRepository.save(order);
        }

        return order;
    }

    public Page<Order> findAllByUnit(Long unitId, Pageable pageable) {
        return ordersRepository.findAllByUnitId(unitId, pageable);
    }

    public Page<Order> findAll(String canalPedido, Pageable pageable) {
        if(canalPedido != null) {
            return ordersRepository.findAllByCanalPedido(
                    CanalPedido.valueOf(canalPedido.toUpperCase()), pageable);
        }
        return ordersRepository.findAll(pageable);
    }

    public Order findById(Long id) {
        var auth = (BearerTokenAuthentication) SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null) {
            throw new ForbiddenException("You must be authenticated to view this order.");
        }

        var order = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with this id."));

        var currentUser = auth.getUser();

        if(!order.getUser().getId().equals(currentUser.getId())
                && currentUser.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("You can't view this order.");
        }

        return order;
    }

    @Transactional
    public Order updateStatus(Long id, UpdateOrderStatusRequest request) {
        var order = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with this id."));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidArgumentException("You can't change the order with a status of " + order.getStatus());
        }

        order.setStatus(OrderStatus.valueOf(request.status().toUpperCase()));
        return ordersRepository.save(order);
    }

    @Transactional
    public void cancel(Long id) {
        var order = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with this id."));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidArgumentException("You can't cancel an order that has already been delivered.");
        }

        order.getItems().forEach(item -> {
            var stockItem = item.getStockItem();
            stockItem.setAmountInStock(stockItem.getAmountInStock() + item.getQuantity());
        });

        order.setStatus(OrderStatus.CANCELLED);
        ordersRepository.save(order);
    }
}