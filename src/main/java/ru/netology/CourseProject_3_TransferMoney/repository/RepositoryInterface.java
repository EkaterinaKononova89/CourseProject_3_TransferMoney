package ru.netology.CourseProject_3_TransferMoney.repository;

import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;

public interface RepositoryInterface {

    public OperationId createNewOperationId(DataForm dataForm);

    public OperationId createConfirmOperation(OperationId operationId);

}
