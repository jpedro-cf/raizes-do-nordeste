package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.application.dto.CreateProductRequest;
import raizes.nordeste.app.domain.entities.Product;
import raizes.nordeste.app.infra.repositories.ProductsRepository;
import raizes.nordeste.app.api.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;

    @Transactional
    public Product create(CreateProductRequest request) {
        var product = new Product();
        product.setName(request.name());
        product.setBasePrice(request.basePrice());

        return productsRepository.save(product);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    public Product findById(Long id){
        return productsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with this id not found."));
    }

    @Transactional
    public void delete(Long id) {
        if (!productsRepository.existsById(id)) {
            throw new NotFoundException("Product with this id not found");
        }
        productsRepository.deleteById(id);
    }
}
