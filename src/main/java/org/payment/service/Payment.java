package org.payment.service;

public class Payment {

    private String currency;

    private Integer amount;

    public Payment(String currency, Integer amount){
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getAmount() {
        return amount;
    }
}
