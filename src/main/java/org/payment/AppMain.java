package org.payment;

import org.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;

@SpringBootApplication
@EnableScheduling
public class AppMain implements CommandLineRunner {

    @Autowired
    private PaymentService paymentService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Comment section before testing
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            paymentService.processInput(scanner.nextLine());
        }
    }
}