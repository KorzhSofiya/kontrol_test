package PaymentMethods;

import Exeptions.PaymentExeption;
import domain.Money;

public class CardPayment implements PaymentMethod{
    @Override
    public void pay(Money amount){
        if (amount.getAmount() > 35000.0) {
            throw new PaymentExeption("Card limit exceeded. Maximum is 35000");
        }
        System.out.println("Paid " + amount + " via Card.");
    }
}
