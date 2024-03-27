package com.bekrenovr.ecommerce.catalog.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "item_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
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

    @Override
    public String toString() {
        return "ItemDetails{" +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", creatingEmployeeId='" + creatingEmployeeId + '\'' +
                ", updatingEmployeeId='" + updatingEmployeeId + '\'' +
                '}';
    }
}
