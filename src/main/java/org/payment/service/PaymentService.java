package org.payment.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

@Service
public class PaymentService {

    private Map<String, Integer> mapPayments;

    private static Map<String, Double> mapExchangeRates;
        static {
            mapExchangeRates = new HashMap<>();
            mapExchangeRates.put("HKD", 7.76799);
            mapExchangeRates.put("RMB", 6.35728);
            mapExchangeRates.put("NZD", 7.27271);
            mapExchangeRates.put("GBP", 20.17348);
        }

    private boolean isStart = true;

    public PaymentService(){
        mapPayments = new HashMap<>();
    }

    public Map<String, Integer> getPayments(){
        return mapPayments;
    }

    /**
     * Process user input.
     * If user input equals 'quit' exit.
     * When app starting up check input from a file.
     * If file exists, load input from a file,
     * then process input from console.
     * @param input User input.
     */
    public void processInput(String input){
        // Exit app
        if (input.equals("quit")) {
            exit(0);
        // Process input from a file when app starting up
        } else if (isStart) {
            processFileInput(input);
            isStart = false;
        // Process input from console
        } else {
            processTextInput(input);
        }
    }

    /**
     * Process file input.
     * If file exist load input from a file.
     * Check every line of .txt file.
     * The format of the file must be
     * one or more lines with Currency Code Amount.
     * @param input User input.
     * return List<Payment> This return list of payments.
     */
    public List<Payment> processFileInput(String input) {
        List<Payment> payments = payments = new ArrayList<>();

        try {
            File file = new File(input);
            //Check if file exist
            if(file.exists()) {
                Pattern p = Pattern.compile("[.]txt$");
                Matcher m = p.matcher(input);
                //Check it is .txt file
                if (m.find()) {
                    Scanner scannerFile = new Scanner(file);
                    //Process every line of file
                    while (scannerFile.hasNextLine()) {
                        payments.add(processTextInput(scannerFile.nextLine()));
                    }
                    scannerFile.close();
                } else {
                    System.out.println("File must have .txt format!");
                }
            } else {
                payments.add(processTextInput(input));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payments;
    }

    /**
     * Process input from console.
     * Check input with regular expression.
     * Input must consist currency, space and amount.
     * Currency may be any uppercase 3 letter code.
     * @param input User input.
     * @return Payment This return object of payment/null.
     */
    public Payment processTextInput(String input){
        Pattern p = Pattern.compile("^([A-Z]{3}) (-?\\d+)$");
        Matcher m = p.matcher(input);

        Payment payment = null;
        //If match result
        if (m.find()) {
            //Group currency and amount to record of payment
            payment = new Payment(m.group(1), Integer.parseInt(m.group(2)));
            //Map net amount of each currency
            Integer netAmount = mapPayments.get(payment.getCurrency());
            mapPayments.put(payment.getCurrency(), (netAmount == null) ? payment.getAmount() : netAmount + payment.getAmount());
        //Else print error message
        } else {
            System.out.println("Enter must consist currency, space and amount, such as USD 100, USD -100 etc.");
        System.out.println("Currency may be any uppercase 3 letter code, such as USD, HKD, RMB, NZD, GBP etc.");
    }

        return payment;
    }

    /**
     * Process output to console.
     * If net amount of currency is 0, then not be displayed.
     * Write exchange rate compared to USD.
     */
    @Scheduled(fixedDelay=60000, initialDelay = 60000)
    private void processOutput(){
        System.out.println("The net amounts of each currency");

        for (Map.Entry<String, Integer> entry : mapPayments.entrySet()) {
            // If the net amount is 0, that currency should not be displayed.
            if (entry.getValue() != 0) {
                String output = entry.getKey() + " " + entry.getValue();
                //Write the USD equivalent amount next to currency
                Double exchangeRate = mapExchangeRates.get(entry.getKey());
                if (exchangeRate != null) {
                    output += " (USD " + String.format("%.2f", entry.getValue() / exchangeRate) + ")";
                }
                System.out.println(output);
            }
        }
    }
}
