package raizes.nordeste.app.api.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import raizes.nordeste.app.application.Hasher;
import raizes.nordeste.app.domain.entities.*;
import raizes.nordeste.app.infra.repositories.ProductsRepository;
import raizes.nordeste.app.infra.repositories.StockRepository;
import raizes.nordeste.app.infra.repositories.UnitRepository;
import raizes.nordeste.app.infra.repositories.UsersRepository;

import java.math.BigInteger;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;
    private final UnitRepository unitRepository;
    private final StockRepository stockRepository;
    private final Hasher hasher;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (usersRepository.findByEmail("admin@email.com").isPresent()) return;

        var user = User.builder()
                .name("ADMIN")
                .email("admin@email.com")
                .password(hasher.hash(adminPassword))
                .phone(null)
                .age(null)
                .points(0L)
                .lgpdConsent(true)
                .role(UserRole.ADMIN)
                .createdAt(Instant.now())
                .build();

        usersRepository.save(user);

        var product = Product.builder()
                        .name("Produto de Teste")
                        .basePrice(BigInteger.valueOf(10500))
                        .createdAt(Instant.now())
                        .build();

        productsRepository.save(product);

        var unit = Unit.builder()
                        .address("São Paulo - Centro")
                        .name("Unidade de São Paulo e Região")
                        .createdAt(Instant.now())
                        .build();

        unitRepository.save(unit);

        var stockItem = StockItem.builder()
                .product(product)
                .unit(unit)
                .amountInStock(10L)
                .price(product.getBasePrice())
                .createdAt(Instant.now())
                .build();

        stockRepository.save(stockItem);
    }
}
