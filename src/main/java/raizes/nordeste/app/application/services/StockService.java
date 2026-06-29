package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.api.config.audit.AuditLog;
import raizes.nordeste.app.application.dto.CreateStockItemRequest;
import raizes.nordeste.app.application.dto.StockItemResponse;
import raizes.nordeste.app.application.dto.UpdateStockItemRequest;
import raizes.nordeste.app.domain.entities.StockItem;
import raizes.nordeste.app.domain.exceptions.ProductAlreadyInUnitException;
import raizes.nordeste.app.infra.repositories.ProductsRepository;
import raizes.nordeste.app.infra.repositories.StockRepository;
import raizes.nordeste.app.infra.repositories.UnitRepository;
import raizes.nordeste.app.api.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final ProductsRepository productRepository;
    private final UnitRepository unitRepository;

    @Transactional
    @AuditLog
    public StockItemResponse create(CreateStockItemRequest request) {
        var stockItem = stockRepository
                .findByProductIdAndUnitId(request.productId(), request.unitId());

        if (stockItem.isPresent()) {
            throw new ProductAlreadyInUnitException();
        }

        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NotFoundException("Product not found with this id."));

        var unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new NotFoundException("Unit not found with this id."));

        var item = new StockItem();
        item.setProduct(product);
        item.setUnit(unit);
        item.setAmountInStock(request.amount());
        item.setPrice(request.price());

        return StockItemResponse.from(stockRepository.save(item));
    }

    public Page<StockItemResponse> findAllByUnit(Long unitId, Pageable pageable) {
        return stockRepository.findAllByUnitId(unitId, pageable).map(StockItemResponse::from);
    }

    public StockItemResponse findById(Long id) {
        return stockRepository.findById(id)
                .map(StockItemResponse::from)
                .orElseThrow(() -> new NotFoundException("Stock Item not found with this id"));
    }

    @Transactional
    @AuditLog
    public StockItemResponse update(Long id, UpdateStockItemRequest request) {
        var stockItem = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock Item not found with this id"));

        stockItem.setAmountInStock(request.amount());
        stockItem.setPrice(request.price());

        return StockItemResponse.from(stockRepository.save(stockItem));
    }

    @Transactional
    @AuditLog
    public void delete(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new NotFoundException("Stock Item not found with this id");
        }
        stockRepository.deleteById(id);
    }
}
