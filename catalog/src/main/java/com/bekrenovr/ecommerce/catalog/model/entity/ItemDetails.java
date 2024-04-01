package com.bekrenovr.ecommerce.catalog.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "item_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDetails {

    @Id
    private UUID itemId;

    @OneToOne
    @JoinColumn(name = "item_id")
    @MapsId
    @JsonIgnore
    private Item item;

    @Column(name = "orders_count_total")
    private Integer ordersCountTotal;

    @Column(name = "orders_count_last_month")
    private Integer ordersCountLastMonth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "creating_employee_id")
    private UUID creatingEmployeeId;

    @Column(name = "updating_employee_id")
    private UUID updatingEmployeeId;

    public ItemDetails(Integer ordersCountTotal, Integer ordersCountLastMonth, LocalDateTime createdAt, UUID creatingEmployeeId) {
        this.ordersCountTotal = ordersCountTotal;
        this.ordersCountLastMonth = ordersCountLastMonth;
        this.createdAt = createdAt;
        this.creatingEmployeeId = creatingEmployeeId;
    }
}
