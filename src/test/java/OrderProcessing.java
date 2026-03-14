import Exeptions.AppExeption;
import Exeptions.PaymentExeption;
import Exeptions.ValidationExeption;
import Exeptions.WrongPromocodeExeption;
import PaymentMethods.BankPayment;
import PaymentMethods.CardPayment;
import PaymentMethods.PauPalPayment;
import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderProcessing {
    private StandartOrderProcess processor;
    private OrderRepository mockRepo;

    @BeforeEach
    void setUp() {
        mockRepo = id -> Optional.empty();
        processor = new StandartOrderProcess(mockRepo);
    }
    @Test
    void testSuccessfulOrderProcessing() {
        OrderItem[] items = {new OrderItem("P1", 1, new Money(1000.0))};
        Order order = new Order(items,"1", new Email("test@mail.com"), null);

        assertDoesNotThrow(() -> processor.process(order, new CardPayment()));
        assertEquals(OrderStatus.PAID, order.getStatus());
    }
    @Test
    void testPromoCodeDiscountApplied() {
        OrderItem[] items = {new OrderItem("P1", 1, new Money(1000.0))}; // Зі знижкою має бути 850
        Order order = new Order(items, "2",new Email("test@mail.com"), "SPRING15");
        assertDoesNotThrow(() -> processor.process(order, new PauPalPayment()));
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testStateTransitionPaidToRefunded() {
        OrderItem[] items = {new OrderItem("P1", 1, new Money(1000))};
        Order order = new Order(items,"3", new Email("test@mail.com"), null);

        processor.process(order, new CardPayment());
        assertEquals(OrderStatus.PAID, order.getStatus());

        order.setStatus(OrderStatus.REFUNDED);
        assertEquals(OrderStatus.REFUNDED, order.getStatus());
    }
    @Test
    void testInvalidPromoCodeThrowsException() {
        OrderItem[] items = {new OrderItem("P1", 1, new Money(1000))};
        Order order = new Order(items, "4",new Email("test@mail.com"),  "FAKE_PROMO");

        assertThrows(WrongPromocodeExeption.class, () -> processor.process(order, new CardPayment()));
        assertEquals(OrderStatus.FAILED, order.getStatus());
    }
    @ParameterizedTest
    @ValueSource(doubles = {36000, 40000})
    void testCardPaymentLimitExceeded(double amount) {
        OrderItem[] items = {new OrderItem("P1", 1, new Money(amount))};
        Order order = new Order(items,"5", new Email("test@mail.com"), null);

        assertThrows(PaymentExeption.class, () -> processor.process(order, new CardPayment()));
    }
    @Test
    void testPayPalMinimumAmount() {
        OrderItem[] items = {new OrderItem("P1", 1, new Money(300))};
        Order order = new Order(items,"6", new Email("test@mail.com"), null);

        assertThrows(PaymentExeption.class, () -> processor.process(order, new PauPalPayment()));
    }
    @Test
    void testDuplicateProductsThrowException() {
        OrderItem[] items = {
                new OrderItem("P1", 1, new Money(100)),
                new OrderItem("P1", 2, new Money(200))
        };
        Order order = new Order(items,"7", new Email("test@mail.com"), null);

        assertThrows(ValidationExeption.class, () -> processor.process(order, new CardPayment()));
    }
    @Test
    void testExceptionChainingOnBankTransfer() {
        OrderItem[] items = {new OrderItem("P1", 1, new Money(99999))};
        Order order = new Order(items,"8", new Email("test@mail.com"), null);

        AppExeption exception = assertThrows(AppExeption.class, () -> processor.process(order, new BankPayment()));
        assertNotNull(exception.getCause());
    }
    @Test
    void testInvalidEmail() {
        assertThrows(ValidationExeption.class, () -> new Email("bad-email.com"));
    }


    @Test
    void testDefensiveCopyWorks() {
        OrderItem[] originalItems = {new OrderItem("P1", 1, new Money(100))};
        Order order = new Order(originalItems,"9", new Email("test@mail.com"), null);

        OrderItem[] retrievedItems = order.getItems();
        retrievedItems[0] = new OrderItem("HACKED", 1, new Money(0));


        assertNotEquals("HACKED", order.getItems()[0].getProductId());
    }
}
