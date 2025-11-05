package com.assignment.OrderProcessing;

import com.assignment.OrderProcessing.entity.Order;
import com.assignment.OrderProcessing.entity.OrderItem;
import com.assignment.OrderProcessing.entity.OrderStatus;
import com.assignment.OrderProcessing.scheduler.OrderProcessingScheduler;
import com.assignment.OrderProcessing.service.OrderService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;

@SpringBootApplication
public class OrderProcessingApplication {

	public static void main(String[] args) {
		OrderService service = new OrderService();

		// 1. Place an order with 2 items
		List<OrderItem> items = Arrays.asList(
				new OrderItem("prod1", 2, 10.0),
				new OrderItem("prod2", 1, 50.0)
		);
		Order order1 = service.placeOrder("alice@example.com", items);
		UUID order1Id = order1.getOrderId();
		System.out.println("Placed Order 1: Status is " + order1.getStatus());

		// 2. Retrieve details
		Optional<Order> retrieved = service.getOrderById(order1Id);
		retrieved.ifPresent(o -> System.out.println("Retrieved Order 1 Details: " + o.getCustomerEmail()));

		// 3. Try to cancel (should succeed as it's PENDING)
		boolean cancelled = service.cancelOrder(order1Id);
		System.out.println("Cancellation attempt on Order 1 successful: " + cancelled);
		System.out.println("Order 1 Status is now: " + service.getOrderById(order1Id).get().getStatus());

		// 4. Place another order that remains PENDING for the scheduler
		Order order2 = service.placeOrder("bob@example.com", items);
		System.out.println("\nPlaced Order 2: Status is " + order2.getStatus());

		// 5. List orders (filtered)
		List<Order> pending = service.listAllOrders(OrderStatus.PENDING);
		System.out.println("Total PENDING orders found: " + pending.size()); // Should be 1 (Order 2)

		OrderProcessingScheduler scheduler = new OrderProcessingScheduler(service);
		scheduler.processPendingOrders();

		List<Order> processing = service.listAllOrders(OrderStatus.PROCESSING);
		System.out.println("Total PROCESSING orders after scheduler run: " + processing.size());

		// 6. Order is shipped
		processing.get(0).setStatus(OrderStatus.SHIPPED);
		System.out.println("Order is shipped: " +processing.get(0));

		// 7. Order is delivered
		processing.get(0).setStatus(OrderStatus.DELIVERED);
		System.out.println("Order is delivered: " +processing.get(0));
	}

}
