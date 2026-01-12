package infrastructure;

import domain.Money;

public interface PaymentGateway {
    boolean charge(String orderId, Money money);
}
