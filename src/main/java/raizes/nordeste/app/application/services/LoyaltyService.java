package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.domain.entities.Order;
import raizes.nordeste.app.domain.entities.PointsTransaction;
import raizes.nordeste.app.domain.entities.PointsTransactionResponse;
import raizes.nordeste.app.domain.entities.PointsTransactionType;
import raizes.nordeste.app.infra.repositories.OrdersRepository;
import raizes.nordeste.app.infra.repositories.PointsTransactionRepository;
import raizes.nordeste.app.infra.repositories.UsersRepository;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class LoyaltyService {
    private final PointsTransactionRepository pointsTransactionRepository;
    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void earnPoints(Order order) {
        var points = order.getTotal()
                .divide(BigInteger.valueOf(100))
                .longValue();

        if (points <= 0) return;

        var user = order.getUser();
        user.setPoints(user.getPoints() + points);
        usersRepository.save(user);

        var transaction = new PointsTransaction();
        transaction.setUser(user);
        transaction.setOrder(order);
        transaction.setType(PointsTransactionType.EARNED);
        transaction.setPoints(points);
        pointsTransactionRepository.save(transaction);
    }

    public Page<PointsTransactionResponse> findAllByUser(Long userId, Pageable pageable) {
        return pointsTransactionRepository.findAllByUserId(userId, pageable)
                .map(PointsTransactionResponse::from);
    }
}