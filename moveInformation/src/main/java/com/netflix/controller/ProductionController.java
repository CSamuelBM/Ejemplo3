package com.netflix.controller;

import com.netflix.dto.ProductionDTO.ProductionRegisterDTO;
import com.netflix.dto.ProductionDTO.ProductionResponseDTO;
import com.netflix.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/production")
@RequiredArgsConstructor @CrossOrigin(origins = "*")
public class ProductionController {
    private final ProductionService productionService;

    @GetMapping("/{productionId}")
    public ProductionResponseDTO getDataProduction(@PathVariable long productionId) {
        return productionService.getDataProduction(productionId);
    }

    @PostMapping()
    public long insertDataProduction(@RequestBody ProductionRegisterDTO productionRegisterDTO){
        return productionService.insertDataProduction(productionRegisterDTO);
    }
}
