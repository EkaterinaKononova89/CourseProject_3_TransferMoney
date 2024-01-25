package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor // пустой конструктор для работы с Jackson
@AllArgsConstructor
public class Card {

    private String number;
    private String validTill;
    private String cvc;
    private Amount balance;
}