package infrastructure;

import domain.Order;

public interface OrderRepository {
    Order getById(String orderId);
    void save(Order order);
}
