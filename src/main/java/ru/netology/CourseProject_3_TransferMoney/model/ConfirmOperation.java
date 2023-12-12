package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.Getter;


@Getter
public class ConfirmOperation {

    private String operationId;
    private String code;


    public ConfirmOperation(){} // пустой конструктор для работы с Jackson

    public ConfirmOperation(String operationId, String code){ // конструктор для эндпоинта
        this.operationId = operationId;
        this.code = code;
    }

    public ConfirmOperation(OperationId operationId) { // конструктор для создания объекта в репозитории
       this.operationId = operationId.toString();
        code = generateCode();
    }

    public String generateCode(){ // должен быть полноценный метод, генерирующий случайное 4х значное число
        return "0000";
    }

    public void setOperationId(int id) { // вспомогательный метод для корректной работы фронта
        operationId = Integer.toString(id);
    }
}
