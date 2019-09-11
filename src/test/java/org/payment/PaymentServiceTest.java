package org.payment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.payment.service.Payment;
import org.payment.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppMain.class)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test()
    public void testWrongTextInput() {
        assertTrue(paymentService.processTextInput("EUR") == null);

        assertTrue(paymentService.processTextInput("EUR100") == null);

        assertTrue(paymentService.processTextInput("EU R100") == null);

        assertTrue(paymentService.processTextInput("EU 1") == null);

        assertTrue(paymentService.processTextInput("100 EUR") == null);
    }

    @Test
    public void testCorrectTextInput() {
        Payment payment = paymentService.processTextInput("USD 1000");
        assertTrue(payment.getCurrency().equals("USD"));
        assertTrue(payment.getAmount() == 1000);

        payment = paymentService.processTextInput("HKD 100");
        assertTrue(payment.getCurrency().equals("HKD"));
        assertTrue(payment.getAmount() == 100);

        payment = paymentService.processTextInput("USD -100");
        assertTrue(payment.getCurrency().equals("USD"));
        assertTrue(payment.getAmount() == -100);

        payment = paymentService.processTextInput("RMB 2000");
        assertTrue(payment.getCurrency().equals("RMB"));
        assertTrue(payment.getAmount() == 2000);

        payment = paymentService.processTextInput("HKD 200");
        assertTrue(payment.getCurrency().equals("HKD"));
        assertTrue(payment.getAmount() == 200);
    }

    @Test
    public void testWrongFileInput() {
        File file = new File("src/main/resources/test.tx");
        String absolutePath = file.getAbsolutePath();

        assertTrue(paymentService.processFileInput(absolutePath).get(0) == null);
    }

    @Test
    public void testCorrectFileInput() {
        File file = new File("src/main/resources/test.txt");
        String absolutePath = file.getAbsolutePath();

        List<Payment> payments = paymentService.processFileInput(absolutePath);

        for(Payment payment : payments){
            assertNotNull(paymentService.getPayments().get(payment.getCurrency()));
        }
    }

    @Test
    public void testProcessWrongTextFileInput() {
        assertTrue(paymentService.processFileInput("EUR").get(0) == null);
    }

    @Test
    public void testProcessCorrectTextFileInput() {
        Payment payment = paymentService.processFileInput("USD 1000").get(0);

        assertTrue(payment.getCurrency().equals("USD"));
        assertTrue(payment.getAmount() == 1000);
    }
}
