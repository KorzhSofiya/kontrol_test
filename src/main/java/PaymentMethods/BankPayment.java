package PaymentMethods;

import Exeptions.PaymentExeption;
import domain.Money;

import java.io.IOException;

public class BankPayment implements PaymentMethod{
    @Override
    public void pay(Money amount){
        try{
            double fee = amount.getAmount() * 0.025;
            double total = amount.getAmount() + fee;
            if(amount.getAmount() == 999999.0){
                throw new IOException("Bank API is down(");
            }
            System.out.println("Paid " + total + " (including 2.5% fee) via Bank");
        }catch(IOException e){
            throw new PaymentExeption("Failed to process bank transfer", e);
        }
    }
}
