package com.netflix.service;

import com.netflix.dto.ProductionDTO.ProductionRegisterDTO;
import com.netflix.dto.ProductionDTO.ProductionResponseDTO;
import com.netflix.dto.ProductionWorkerDTO.ProductionWorkerRegisterDTO;
import com.netflix.entity.ProductionEntity;
import com.netflix.entity.ProductionWorkerEntity;
import com.netflix.repository.ProductionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductionService {
    private final ProductionRepository productionRepository;
    private final ProductionWorkerService productionWorkerService;

    @Transactional
    public ProductionResponseDTO getDataProduction(long productionId){
        ProductionEntity productionEntity = productionRepository.findById(productionId).orElseThrow();
        return new ProductionResponseDTO(
                productionEntity.getProductionId(),
                productionEntity.getAllWorkers(),
                productionEntity.getAllHectares(),
                productionEntity.getTotalCast(),
                ProductionResponseDTO.productionResponseDTO(productionEntity).productionWorker()
        );
    }

    @Transactional
    public ProductionResponseDTO insertDataProduction(ProductionRegisterDTO productionRegisterDTO) {
        ProductionEntity productionEntity = new ProductionEntity();
        productionEntity.saveProduction(productionRegisterDTO);
        productionEntity = productionRepository.save(productionEntity);

        double totalCast = generalProduction(productionEntity);
        productionEntity.setTotalCast(totalCast);

        productionRepository.saveAndFlush(productionEntity);

        return ProductionResponseDTO.productionResponseDTO(productionEntity);
    }

    private double generalProduction(ProductionEntity production) {
        java.util.concurrent.ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();

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

        if(production.getProductionWorker() != null)
            production.setProductionWorker(new ArrayList<>());

        for(int i = 0; i < totalWorkers; i++) {
            cast = listWeight.get(i) * factor;
            totalCast += cast;
            ProductionWorkerRegisterDTO workerRegisterDTO = new ProductionWorkerRegisterDTO(cast, production);
            ProductionWorkerEntity productionWorker = productionWorkerService.insertDataProductionWorker(workerRegisterDTO);
            if(productionWorker != null)
                production.getProductionWorker().add(productionWorker);
        }

        return totalCast;
    }

}
