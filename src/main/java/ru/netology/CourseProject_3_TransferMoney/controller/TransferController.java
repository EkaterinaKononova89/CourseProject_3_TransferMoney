package ru.netology.CourseProject_3_TransferMoney.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netology.CourseProject_3_TransferMoney.Logger.Logger;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorConfirmation;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.resolver.ConfirmOperationParam;
import ru.netology.CourseProject_3_TransferMoney.resolver.DataFormParam;
import ru.netology.CourseProject_3_TransferMoney.servise.TransferService;

import java.io.IOException;

@RestController
@Validated
public class TransferController {
    TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    @CrossOrigin
    public String transferMoney( @RequestBody @DataFormParam DataForm dataForm) throws IOException {
        return transferService.transferMoney(dataForm);
    }

    @PostMapping("/confirmOperation")
    @CrossOrigin
    public String transferMoneyConfirm( @RequestBody @ConfirmOperationParam ConfirmOperation confirmOperation) throws IOException {
        confirmOperation.setOperationId(transferService.cntId()); // вспомогательный метод для корректной работы фронта в однопоточной среде
        return transferService.confirmOperation(confirmOperation);
    }

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<String> eidHandler(ErrorInputData ex) throws IOException{
        Logger logger = Logger.getInstance();
        logger.log(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransfer.class)
    public ResponseEntity<String> etHandler(ErrorTransfer ex) throws IOException{
        Logger logger = Logger.getInstance();
        logger.log(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorConfirmation.class)
    public ResponseEntity<String> ecHandler(ErrorConfirmation ex) throws IOException{
        Logger logger = Logger.getInstance();
        logger.log(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
