package domain;


import Exeptions.ValidationExeption;

import java.util.Arrays;

public class Order {
    private final String id;
    private final Email customerEmail;
    private final OrderItem[] items;
    private String promocode;
    private OrderStatus status;

    public Order(OrderItem[] items, String id, Email customerEmail, String promocode) {
        this.items = items != null ? Arrays.copyOf(items, items.length) : new OrderItem[0];
        this.id = id;
        this.customerEmail = customerEmail;
        this.promocode = promocode;
        this.status = OrderStatus.NEW;
    }

    public String getId() {
        return id;
    }

    public String getPromocode() {
        return promocode;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderItem[] getItems() {
        return Arrays.copyOf(items, items.length);
    }
    public void setStatus(OrderStatus status) {
        if (this.status == OrderStatus.PAID && status == OrderStatus.REFUNDED) {
            this.status = status;
        } else if (this.status == OrderStatus.NEW && status == OrderStatus.PAID) {
            this.status = status;
        } else if (status == OrderStatus.FAILED) {
            this.status = status;
        } else {
            throw new ValidationExeption("Invalid state transition from " + this.status + " to " + status);
        }
    }
}
