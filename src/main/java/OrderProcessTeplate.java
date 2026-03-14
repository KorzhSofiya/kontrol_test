import Exeptions.AppExeption;
import Exeptions.BuisnessExeption;
import PaymentMethods.PaymentMethod;
import domain.Money;
import domain.Order;
import domain.OrderItem;
import domain.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OrderProcessTeplate {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessTeplate.class);
    public final void process(Order order, PaymentMethod paymentMethod){
        logger.info("Початок обробки замовлення: {}", order.getId());
        try {
            validateItems(order);
            validatePromoCode(order);
            Money total = calculateTotal(order);
            processPayment(order, paymentMethod, total);
            completeOrder(order);
        } catch (BuisnessExeption e) {
            logger.warn("Бізнес-помилка під час обробки: {}", e.getMessage());
            order.setStatus(OrderStatus.FAILED);
            throw e;
        } catch (Exception e) {
            logger.error("Неочікуваний системний збій", e);
            order.setStatus(OrderStatus.FAILED);
            throw new AppExeption("System error during order processing", e);
        }
    }
    protected abstract void validateItems(Order order);
    protected abstract void validatePromoCode(Order order);
    protected abstract Money calculateTotal(Order order);
    protected void processPayment(Order order, PaymentMethod paymentMethod, Money amount) {
        paymentMethod.pay(amount);
    }
    protected void completeOrder(Order order) {
        order.setStatus(OrderStatus.PAID);
        logger.info("Замовлення {} успішно оплачено", order.getId());
    }
}
