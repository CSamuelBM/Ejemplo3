package com.netflix.entity;

import com.netflix.dto.ProductionDTO.ProductionRegisterDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="production")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="production_id") private long productionId;
    @Column(name="all_workers") private long allWorkers;

    @Column(name="all_hectares") private double allHectares;
    @Column(name="total_cast") private double totalCast;
    @OneToMany(mappedBy="productionEntity", cascade = CascadeType.ALL, orphanRemoval = true) private List<ProductionWorkerEntity> productionWorker = new ArrayList<>();

    public void saveProduction(ProductionRegisterDTO productionRegisterDTO){
        this.setAllWorkers(productionRegisterDTO.allWorkers());
        this.setAllHectares(productionRegisterDTO.allHectares());
        this.setTotalCast(0.0);
        this.setProductionWorker(null);
    }
}
