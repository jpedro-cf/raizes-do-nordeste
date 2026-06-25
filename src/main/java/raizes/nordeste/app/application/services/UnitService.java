package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.application.dto.CreateUnitRequest;
import raizes.nordeste.app.domain.entities.Unit;
import raizes.nordeste.app.infra.repositories.UnitRepository;
import raizes.nordeste.app.shared.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;

    @Transactional
    public Unit create(CreateUnitRequest request) {
        var unit = new Unit();
        unit.setName(request.name());
        unit.setAddress(request.address());
        return unitRepository.save(unit);
    }

    public Page<Unit> findAll(Pageable pageable) {
        return unitRepository.findAll(pageable);
    }

    public Unit findById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unit with this id not found"));
    }

    @Transactional
    public void delete(Long id) {
        if (!unitRepository.existsById(id)) {
            throw new NotFoundException("Unit with this id not found");
        }
        unitRepository.deleteById(id);
    }
}