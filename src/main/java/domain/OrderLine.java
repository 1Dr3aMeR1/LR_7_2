package domain;

public class OrderLine {
    private final String product;
    private final int quantity;
    private final Money price;

    public OrderLine(String product, int quantity, Money price) {
        if (quantity <= 0) throw new IllegalArgumentException("Количество должно быть > 0");
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Money total() {
        return new Money(price.getAmount().multiply(java.math.BigDecimal.valueOf(quantity)));
    }

    public String getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public Money getPrice() { return price; }
}
