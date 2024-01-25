package ru.netology.CourseProject_3_TransferMoney.servise;

import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;

import java.io.IOException;

public interface ServiceInterface {

    public Integer countCommission(DataForm dataForm);

    public OperationId transferMoney(DataForm dataForm) throws IOException;

    public OperationId confirmOperation(ConfirmOperation confirmOperation) throws IOException;
}