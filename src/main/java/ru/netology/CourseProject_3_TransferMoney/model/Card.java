package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.Getter;


@Getter
public class Card {

    private String number;
    private String validTill;
    private String cvc;
    private Amount balance;

    public Card() {} // пустой конструктор для работы с Jackson

    public Card(String number, String validTill, String cvc, Amount balance) {
        this.number = number;
        this.validTill = validTill;
        this.cvc = cvc;
        this.balance = balance;
    }

    public boolean addBalance(Amount amount) { // двойная проверка на валюту, еще одна в репозитории
        if (balance.getCurrency().equals(amount.getCurrency())) {
            balance.setValue(balance.getValue() + amount.getValue());
            return true;
        }
        return false;
    }

    public boolean reductionBalance(Amount amount) {
        if (balance.getCurrency().equals(amount.getCurrency())) { // двойная проверка на валюту, еще одна в репозитории
            if (balance.getValue() >= amount.getValue()) { // проверка положительного баланса
                balance.setValue(balance.getValue() - amount.getValue());
                return true;
            }
            return false;
        }
        return false;
    }
}
