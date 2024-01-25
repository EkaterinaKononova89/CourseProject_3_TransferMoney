package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor // пустой конструктор для работы с Jackson
@AllArgsConstructor // конструктор для эндпоинта
public class ConfirmOperation {

    private String operationId;
    private String code;


    public ConfirmOperation(OperationId operationId) { // конструктор для создания объекта в репозитории
        this.operationId = operationId.toString();
        code = generateCode();
    }

    public String generateCode() { // должен быть полноценный метод, генерирующий случайное 4х значное число
        return "0000";
    }

    public void setOperationIdConfirm(int id) { // вспомогательный метод для корректной работы фронта
        operationId = Integer.toString(id);
    }
}
