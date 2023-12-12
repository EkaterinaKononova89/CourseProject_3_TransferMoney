package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.Getter;

@Getter
public class Amount {

    private Integer value;
    private String currency;

    public Amount () { } // пустой конструктор для работы с Jackson

    public Amount(Integer value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "сумма " + value
                + ", валюта " + currency;
    }
}