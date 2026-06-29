package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.application.dto.LoyaltyResponse;
import raizes.nordeste.app.api.config.security.BearerTokenAuthentication;
import raizes.nordeste.app.domain.entities.Order;
import raizes.nordeste.app.domain.entities.PointsTransaction;
import raizes.nordeste.app.domain.entities.PointsTransactionResponse;
import raizes.nordeste.app.domain.entities.PointsTransactionType;
import raizes.nordeste.app.infra.repositories.OrdersRepository;
import raizes.nordeste.app.infra.repositories.PointsTransactionRepository;
import raizes.nordeste.app.infra.repositories.UsersRepository;
import raizes.nordeste.app.api.exceptions.ForbiddenException;

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

    public LoyaltyResponse findAllByUser() {
        var auth = (BearerTokenAuthentication) SecurityContextHolder
                .getContext().getAuthentication();

        if(auth == null) {
            throw new ForbiddenException("You must be authenticated to perform this operation.");
        }

        var user = auth.getUser();
        var transactions = pointsTransactionRepository.findAllByUserId(user.getId())
                .stream()
                .map(PointsTransactionResponse::from)
                .toList();

        return new LoyaltyResponse(user.getPoints(), transactions);
    }
}