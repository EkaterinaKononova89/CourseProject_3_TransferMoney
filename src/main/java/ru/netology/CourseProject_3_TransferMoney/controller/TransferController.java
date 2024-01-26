package ru.netology.CourseProject_3_TransferMoney.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.CourseProject_3_TransferMoney.handler.ExceptionController;
import ru.netology.CourseProject_3_TransferMoney.logger.Logger;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;
import ru.netology.CourseProject_3_TransferMoney.resolver.ConfirmOperationParam;
import ru.netology.CourseProject_3_TransferMoney.resolver.DataFormParam;
import ru.netology.CourseProject_3_TransferMoney.servise.TransferService;

import java.io.IOException;

@RestController
@Validated
public class TransferController extends ExceptionController implements Controller {
    private final TransferService transferService;

    public TransferController(Logger logger, TransferService transferService){
        super(logger);
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    @CrossOrigin
    public ResponseEntity<OperationId> transferMoney(@RequestBody @DataFormParam DataForm dataForm) {
        try {
            return new ResponseEntity<>(transferService.transferMoney(dataForm), HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/confirmOperation")
    @CrossOrigin
    public ResponseEntity<OperationId> transferMoneyConfirm(@RequestBody @ConfirmOperationParam ConfirmOperation confirmOperation) {
        try {
            confirmOperation.setOperationIdConfirm(transferService.cntId()); // вспомогательный метод для корректной работы фронта в однопоточной среде
            return new ResponseEntity<>(transferService.confirmOperation(confirmOperation), HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
