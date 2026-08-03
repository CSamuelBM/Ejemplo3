package com.netflix.service;

import com.netflix.dto.ProductionWorkerDTO.ProductionWorkerRegisterDTO;
import com.netflix.dto.ProductionWorkerDTO.ProductionWorkerResponseDTO;
import com.netflix.entity.ProductionWorkerEntity;
import com.netflix.repository.ProductionWorkerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductionWorkerService {
    private final ProductionWorkerRepository productionWorkerRepository;

    @Transactional
    public ProductionWorkerEntity insertDataProductionWorker(ProductionWorkerRegisterDTO productionWorkerRegisterDTO){
        ProductionWorkerEntity productionWorkerEntity = new ProductionWorkerEntity();
        productionWorkerEntity.saveProductionWorker(productionWorkerRegisterDTO);
        return  productionWorkerRepository.save(productionWorkerEntity);
    }

    @Transactional
    public ProductionWorkerResponseDTO getDataProductionWorker(long productionWorkerId) {
        ProductionWorkerEntity productionWorkerEntity = productionWorkerRepository.findById(productionWorkerId).orElseThrow();
        return new ProductionWorkerResponseDTO(
                productionWorkerEntity.getProductionWorkerId(),
                productionWorkerEntity.getProduction(),
                productionWorkerEntity.getProductionEntity().getProductionId(),
                productionWorkerEntity.getProductionEntity().getAllHectares()
        );
    }
}
