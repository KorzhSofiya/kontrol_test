package Exeptions;

public class PaymentExeption extends AppExeption{
    public PaymentExeption(String messege){super(messege);}
    public PaymentExeption(String messege, Throwable cause){super(messege, cause);}
}
