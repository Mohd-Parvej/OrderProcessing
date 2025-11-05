package com.assignment.OrderProcessing.scheduler;

import com.assignment.OrderProcessing.entity.Order;
import com.assignment.OrderProcessing.entity.OrderStatus;
import com.assignment.OrderProcessing.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderProcessingScheduler {
    private final OrderService orderService;

    public OrderProcessingScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void processPendingOrders() {
        System.out.println("Running scheduled task to process pending orders...");
        List<Order> pendingOrders = orderService.listAllOrders(OrderStatus.PENDING);

        for (Order order : pendingOrders) {
            System.out.println("Updating order " + order.getOrderId() + " from PENDING to PROCESSING.");
            orderService.updateOrderStatus(order.getOrderId(), OrderStatus.PROCESSING);
        }
        System.out.println("Scheduled task completed.");
    }
}