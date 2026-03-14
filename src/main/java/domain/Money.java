package domain;

import java.util.Objects;

public final class Money {
    private final double amount;
    private final String current;

    public Money(double amount){
        this(amount, "UAH");
    }
    public Money(double amount, String current){
        if(amount <0){

        }
        this.amount = amount;
        this.current = current;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrent() {
        return current;
    }
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Money money = (Money) o;
        return Double.compare(money.amount, amount) == 1 && current.equals(money.current);
    }
    @Override
    public int hashCode(){
        return Objects.hash(amount, current);
    }
    @Override
    public String toString(){
        return amount + " " + current;
    }
}
