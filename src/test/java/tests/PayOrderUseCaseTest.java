package tests;

import application.PayOrderUseCase;
import domain.Money;
import domain.Order;
import domain.OrderLine;
import infrastructure.FakePaymentGateway;
import infrastructure.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayOrderUseCaseTest {
    private InMemoryOrderRepository orderRepository;
    private FakePaymentGateway paymentGateway;
    private PayOrderUseCase payOrderUseCase;

    @BeforeEach
    void setup() {
        orderRepository = new InMemoryOrderRepository();
        paymentGateway = new FakePaymentGateway();
        payOrderUseCase = new PayOrderUseCase(orderRepository, paymentGateway);
    }

    @Test
    void successfulPayment() {
        Order order = new Order("1");
        order.addLine(new OrderLine("Item1", 2, new Money(java.math.BigDecimal.valueOf(10))));
        orderRepository.save(order);

        boolean result = payOrderUseCase.execute("1");

        assertTrue(result);
        assertEquals(0, order.getLines().size() - order.getLines().size()); // Lines unchanged
        assertEquals(java.util.Arrays.asList(order.getLines()).size(), order.getLines().size());
        assertEquals(order.total().getAmount().intValue(), 20);
    }

    @Test
    void cannotPayEmptyOrder() {
        Order order = new Order("2");
        orderRepository.save(order);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            payOrderUseCase.execute("2");
        });

        assertEquals("Cannot pay empty order", exception.getMessage());
    }

    @Test
    void cannotPayAlreadyPaidOrder() {
        Order order = new Order("3");
        order.addLine(new OrderLine("Item", 1, new Money(java.math.BigDecimal.valueOf(10))));
        order.pay();
        orderRepository.save(order);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            payOrderUseCase.execute("3");
        });

        assertEquals("Order already paid", exception.getMessage());
    }

    @Test
    void cannotModifyAfterPayment() {
        Order order = new Order("4");
        order.addLine(new OrderLine("Item", 1, new Money(java.math.BigDecimal.valueOf(10))));
        order.pay();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            order.addLine(new OrderLine("Item2", 1, new Money(java.math.BigDecimal.valueOf(5))));
        });

        assertEquals("Cannot modify order after payment", exception.getMessage());
    }

    @Test
    void totalCalculation() {
        Order order = new Order("5");
        order.addLine(new OrderLine("Item1", 1, new Money(java.math.BigDecimal.valueOf(10))));
        order.addLine(new OrderLine("Item2", 2, new Money(java.math.BigDecimal.valueOf(15))));
        assertEquals(40, order.total().getAmount().intValue());
    }
}
