package com.assignment.OrderProcessing.service;

import com.assignment.OrderProcessing.entity.Order;
import com.assignment.OrderProcessing.entity.OrderItem;
import com.assignment.OrderProcessing.entity.OrderStatus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderService {
    private final Map<UUID, Order> orderStore = new HashMap<>();

    /**
     * Requirement 1. Create an order
     */
    public Order placeOrder(String customerEmail, List<OrderItem> items) {
        Order order = new Order();
        order.setCustomerEmail(customerEmail);
        order.setItems(items);
        orderStore.put(order.getOrderId(), order);
        return order;
    }

    /**
     * Requirement 2. Retrieve order details by ID
     */
    public Optional<Order> getOrderById(UUID orderId) {
        return Optional.ofNullable(orderStore.get(orderId));
    }

    /**
     * Requirement 3. List all orders, optionally filtered by status
     */
    public List<Order> listAllOrders(OrderStatus statusFilter) {
        return orderStore.values().stream()
                .filter(order -> statusFilter == null || order.getStatus() == statusFilter)
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Requirement 4. Cancel an order (only if PENDING)
     */
    public boolean cancelOrder(UUID orderId) {
        Order order = orderStore.get(orderId);
        if (order != null && order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }

    /**
     * Requirement 5. Update order status (internal use)
     */
    public void updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderStore.get(orderId);
        if (order != null) {
            order.setStatus(newStatus);
        }
    }
}