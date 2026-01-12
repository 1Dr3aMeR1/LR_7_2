package application;

import domain.Money;
import domain.Order;
import infrastructure.OrderRepository;
import infrastructure.PaymentGateway;

public class PayOrderUseCase {
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    public PayOrderUseCase(OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    public boolean execute(String orderId) {
        Order order = orderRepository.getById(orderId);
        order.pay();
        boolean paymentResult = paymentGateway.charge(order.getId(), order.total());
        orderRepository.save(order);
        return paymentResult;
    }
}
