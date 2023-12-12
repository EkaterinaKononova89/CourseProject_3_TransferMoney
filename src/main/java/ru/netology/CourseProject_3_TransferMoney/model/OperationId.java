package ru.netology.CourseProject_3_TransferMoney.model;

import lombok.Getter;

@Getter
public class OperationId {

    private String operationId;

    public void setOperationId(int id){
        this.operationId = Integer.toString(id);
    }

    @Override
    public String toString() {
        return operationId;
    }
}
