package ru.netology.CourseProject_3_TransferMoney.controller;

import org.springframework.http.ResponseEntity;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;

public interface Controller {
    public ResponseEntity<OperationId> transferMoney(DataForm dataForm);

    public ResponseEntity<OperationId> transferMoneyConfirm(ConfirmOperation confirmOperation);
}
