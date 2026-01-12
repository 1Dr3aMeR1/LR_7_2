package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final String id;
    private final List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status = OrderStatus.NEW;

    public Order(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public OrderStatus getStatus() { return status; }

    public void addLine(OrderLine line) {
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("Невозможно изменить заказ после оплаты");
        }
        lines.add(line);
    }

    public Money total() {
        return lines.stream()
                .map(OrderLine::total)
                .reduce(new Money(java.math.BigDecimal.ZERO), Money::add);
    }

    public void pay() {
        if (lines.isEmpty()) {
            throw new IllegalStateException("Не удается оплатить пустой заказ");
        }
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("Заказ уже оплачен");
        }
        status = OrderStatus.PAID;
    }
}
