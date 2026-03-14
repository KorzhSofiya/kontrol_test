package PaymentMethods;

import Exeptions.PaymentExeption;
import domain.Money;

public class PauPalPayment implements PaymentMethod{
    @Override
    public void pay(Money amount){
        if (amount.getAmount() < 400.0) {
            throw new PaymentExeption("PayPal minimum amount is 400");
        }
        System.out.println("Paid " + amount + " via PayPal.");
    }
}
