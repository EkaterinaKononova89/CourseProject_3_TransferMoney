package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.Getter;

@Getter
public class DataForm {

    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;

    public DataForm() {} // пустой конструктор для работы с Jackson
    public DataForm(String cardFromNumber, String cardFromValidTill, String cardFromCVV, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardFromCVV = cardFromCVV;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }
    public DataForm divideBy100 () {     // перевод из копеек, которые приходят с фронта, в рубли
        getAmount().setValue(getAmount().getValue()/100);
        return this;
    }
}
