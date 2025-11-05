package com.assignment.OrderProcessing.entity;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Order {
    private UUID orderId;
    private String customerEmail;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItem> items;

    public Order() {
        this.orderId = UUID.randomUUID();
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }
}