package ru.netology.CourseProject_3_TransferMoney.service;

import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;

public interface ServiceInterface {

    public Integer countCommission(DataForm dataForm);

    public OperationId transferMoney(DataForm dataForm);

    public OperationId confirmOperation(ConfirmOperation confirmOperation);
}