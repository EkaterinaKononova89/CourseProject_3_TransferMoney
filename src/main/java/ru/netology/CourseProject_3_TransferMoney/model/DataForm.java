package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // пустой конструктор для работы с Jackson
@AllArgsConstructor
public class DataForm {

    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;


    public DataForm divideBy100() {     // перевод из копеек, которые приходят с фронта, в рубли
        getAmount().setValue(getAmount().getValue() / 100);
        return this;
    }
}
