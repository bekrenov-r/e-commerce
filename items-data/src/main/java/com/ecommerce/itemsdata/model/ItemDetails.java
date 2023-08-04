package com.ecommerce.itemsdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "item_details")
@Getter
@Setter
@NoArgsConstructor
public class ItemDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String creatingEmployeeId;

    @Column(name = "updating_employee_id")
    private String updatingEmployeeId;

    public ItemDetails(Integer ordersCountTotal, Integer ordersCountLastMonth, LocalDateTime createdAt, String creatingEmployeeId) {
        this.ordersCountTotal = ordersCountTotal;
        this.ordersCountLastMonth = ordersCountLastMonth;
        this.createdAt = createdAt;
        this.creatingEmployeeId = creatingEmployeeId;
    }

    public ItemDetails(LocalDateTime createdAt, LocalDateTime updatedAt, String creatingEmployeeId, String updatingEmployeeId) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.creatingEmployeeId = creatingEmployeeId;
        this.updatingEmployeeId = updatingEmployeeId;
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
