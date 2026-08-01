package com.netflix.service;

import com.netflix.dto.ProductionDTO.ProductionRegisterDTO;
import com.netflix.dto.ProductionDTO.ProductionResponseDTO;
import com.netflix.dto.ProductionWorkerDTO.ProductionWorkerRegisterDTO;
import com.netflix.entity.ProductionEntity;
import com.netflix.repository.ProductionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProductionService {
    private final ProductionRepository productionRepository;
    private final ProductionWorkerService productionWorkerService;

    public long insertDataProduction(ProductionRegisterDTO productionRegisterDTO){
        ProductionEntity productionEntity = new ProductionEntity();
        productionEntity.saveProduction(productionRegisterDTO);
        productionRepository.save(productionEntity);
        return productionEntity.getProductionId();
    }

    @Transactional
    public ProductionResponseDTO getDataProduction(long productionId) {
        ProductionEntity production = productionCast(productionId);
        return new ProductionResponseDTO(
                production.getProductionId(),
                production.getAllWorkers(),
                production.getAllHectares(),
                production.getTotalCast(),
                ProductionResponseDTO.productionResponseDTO(production).productionWorker()
        );
    }

    private ProductionEntity productionCast(long productionId){
        java.util.concurrent.ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();
        ProductionEntity production = productionRepository.findById(productionId).orElseThrow();

        long totalWorkers = production.getAllWorkers();
        double totalHectares = production.getAllHectares();

        double sumWeight = 0.0, totalCast = 0.0, factor, cast, rndm;
        List<Double> listWeight = new ArrayList<>();

        for(int i = 0; i < totalWorkers; i++) {
            rndm = random.nextDouble(0.03, 0.06);
            listWeight.add(rndm);
            sumWeight += rndm;
        }

        factor = totalHectares / sumWeight;
        for(int i = 0; i < totalWorkers; i++) {
            cast = listWeight.get(i) * factor;
            totalCast += cast;
            ProductionWorkerRegisterDTO productionWorkerRegisterDTO = new ProductionWorkerRegisterDTO(cast, production);
            productionWorkerService.insertDataProductionWorker(productionWorkerRegisterDTO);
        }

        production.setTotalCast(totalCast);
        return production;
    }
}
