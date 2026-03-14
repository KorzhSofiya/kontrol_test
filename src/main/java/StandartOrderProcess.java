import Exeptions.ValidationExeption;
import Exeptions.WrongPromocodeExeption;
import PaymentMethods.PaymentMethod;
import domain.Money;
import domain.Order;
import domain.OrderItem;
import domain.OrderStatus;

import java.util.HashSet;
import java.util.Set;

public class StandartOrderProcess  extends OrderProcessTeplate{
    private final OrderRepository orderRepository;

    public StandartOrderProcess(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected void validateItems(Order order) {
        OrderItem[] items = order.getItems();
        if (items.length == 0) throw new ValidationExeption("Order is empty");
        Set<String> uniqueProducts = new HashSet<>();
        for (OrderItem item : items) {
            if (!uniqueProducts.add(item.getProductId())) {
                throw new ValidationExeption("Duplicate product found: " + item.getProductId());
            }
        }
    }

    @Override
    protected void validatePromoCode(Order order) {
        String promo = order.getPromocode();
        if (promo != null && !promo.isEmpty() && !promo.equals("SPRING15")) {
            throw new WrongPromocodeExeption("Unknown promo code: " + promo);
        }
    }

    @Override
    protected Money calculateTotal(Order order) {
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            double itemTotal = item.getPrice().getAmount() * item.getQuantity();
            total += itemTotal;
        }
        if ("SPRING15".equals(order.getPromocode())) {
            double discount = total * 0.15;
            total -= discount;
        }
        return new Money(total);
    }
}
