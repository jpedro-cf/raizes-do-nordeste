package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.application.dto.CreateOrderRequest;
import raizes.nordeste.app.application.dto.UpdateOrderStatusRequest;
import raizes.nordeste.app.config.security.BearerTokenAuthentication;
import raizes.nordeste.app.domain.entities.*;
import raizes.nordeste.app.domain.exceptions.ProductOutOfStockException;
import raizes.nordeste.app.infra.repositories.OrdersRepository;
import raizes.nordeste.app.infra.repositories.StockRepository;
import raizes.nordeste.app.infra.repositories.UnitRepository;
import raizes.nordeste.app.infra.repositories.UsersRepository;
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

    @Transactional
    public Order create(CreateOrderRequest request) {
        var auth = (BearerTokenAuthentication) SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null) {
            throw new ForbiddenException("You must be authenticated to order something.");
        }

        var userId = auth.getPrincipal();

        var unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new NotFoundException("Unit with this id not found."));

        var user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with this id not found."));

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
        order.setTotal(total);

        return ordersRepository.save(order);
    }

    public Page<Order> findAllByUnit(Long unitId, Pageable pageable) {
        return ordersRepository.findAllByUnitId(unitId, pageable);
    }

    public Order findById(Long id) {
        var auth = (BearerTokenAuthentication) SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null) {
            throw new ForbiddenException("You must be authenticated to order something.");
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