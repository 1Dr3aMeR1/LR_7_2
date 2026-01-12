package infrastructure;

import domain.Order;

import java.util.HashMap;
import java.util.Map;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<String, Order> storage = new HashMap<>();

    @Override
    public Order getById(String orderId) {
        return storage.get(orderId);
    }

    @Override
    public void save(Order order) {
        storage.put(order.getId(), order);
    }
}
