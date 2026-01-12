package infrastructure;

import domain.Money;

public class FakePaymentGateway implements PaymentGateway {
    @Override
    public boolean charge(String orderId, Money money) {
        System.out.println("Charging order " + orderId + " amount " + money);
        return true;
    }
}
