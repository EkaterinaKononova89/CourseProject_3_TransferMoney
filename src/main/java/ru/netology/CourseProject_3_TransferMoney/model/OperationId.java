package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor // для тестов
@NoArgsConstructor // для всего остального, в том числе для работы с Jackson
public class OperationId {

    private String operationId;

    public void setOperationId(int id) {
        this.operationId = Integer.toString(id);
    }

    @Override
    public String toString() {
        return operationId;
    }
}
