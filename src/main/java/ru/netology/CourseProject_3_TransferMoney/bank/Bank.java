package ru.netology.CourseProject_3_TransferMoney.bank;

import org.springframework.context.annotation.Configuration;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.Card;

@Configuration
public class Bank {

    // имитация одобрения банком. Отправляем запрос на перевод в банк, ждем ответ, достаточно ли средств на карте
    public boolean sayYes(Card cardFrom, Card cardTo, Amount amountWithCommission) {
        return Math.random() < 0.8;
    }

    // имитация деятельности банка. Сообщаем банку, что введен правильный код, далее банк списывает и зачисляет средства
    public void codeTrue() {
    }
}
