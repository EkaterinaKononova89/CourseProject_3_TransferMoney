package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // пустой конструктор для работы с Jackson
@AllArgsConstructor
public class Amount {

    private Integer value;
    private String currency;

    @Override
    public String toString() {
        return "сумма " + value + ", валюта " + currency;
    }
}