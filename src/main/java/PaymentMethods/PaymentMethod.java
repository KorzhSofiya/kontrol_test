package PaymentMethods;

import domain.Money;

public interface PaymentMethod {
    void pay(Money amount);
}
