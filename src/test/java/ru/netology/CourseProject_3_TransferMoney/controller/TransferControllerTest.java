package ru.netology.CourseProject_3_TransferMoney.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorConfirmation;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;
import ru.netology.CourseProject_3_TransferMoney.service.TransferService;

public class TransferControllerTest {
    TransferController sut;
    TransferService service = Mockito.mock(TransferService.class);


    @BeforeEach
    public void beforeEach() {
        sut = new TransferController(service);
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @Test
    public void transferMoneyTest_200_OK() {
        //given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(1_000_000, "RUR"));
        Mockito.when(service.transferMoney(dataForm.divideBy100())).thenReturn(new OperationId("1"));

        //when
        ResponseEntity<OperationId> result = sut.transferMoney(dataForm);

        //then
        Assertions.assertEquals("<200 OK OK,1,[]>", result.toString()); // почему так много ОК?
    }

    @Test
    public void transferMoneyTest_error400() {
        //given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(1_000_000, "RUR"));
        Mockito.when(service.transferMoney(dataForm.divideBy100())).thenThrow
                (new ErrorInputData("Error input data - Неверные входные данные. ОТКЛОНЕНО БАНКОМ", "1"));

        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. ОТКЛОНЕНО БАНКОМ, id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyTest_error500() {
        //given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(1_000_000, "RUR"));
        Mockito.when(service.transferMoney(dataForm.divideBy100())).thenThrow
                (new ErrorTransfer("ErrorTransfer - ошибка сервера", "1"));

        //when

        //then
        ErrorTransfer exception = Assertions.assertThrows(ErrorTransfer.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("ErrorTransfer - ошибка сервера, id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyConfirm_200_OK() {
        //given
        ConfirmOperation confirmOperation = new ConfirmOperation("1", "0000");
        Mockito.when(service.confirmOperation(confirmOperation)).thenReturn(new OperationId(confirmOperation.getOperationId()));

        //when
        ResponseEntity<OperationId> result = sut.transferMoneyConfirm(confirmOperation);

        //then
        Assertions.assertEquals("<200 OK OK,1,[]>", result.toString());
    }

    @Test
    public void transferMoneyConfirm_error400() {
        //given
        ConfirmOperation confirmOperation = new ConfirmOperation("1", "0000");
        Mockito.when(service.confirmOperation(confirmOperation)).thenThrow
                (new ErrorInputData("Error input data - Неверные входные данные. ВВЕДЕН НЕВЕРНЫЙ КОД", "1"));

        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.transferMoneyConfirm(confirmOperation);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. ВВЕДЕН НЕВЕРНЫЙ КОД, id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyConfirm_error500() {
        //given
        ConfirmOperation confirmOperation = new ConfirmOperation("1", "0000");
        Mockito.when(service.confirmOperation(confirmOperation)).thenThrow
                (new ErrorConfirmation("Error confirmation - ошибка сервера, операция не подтверждена", "1"));

        //when

        //then
        ErrorConfirmation exception = Assertions.assertThrows(ErrorConfirmation.class, () -> {
            sut.transferMoneyConfirm(confirmOperation);
        });
        Assertions.assertEquals("Error confirmation - ошибка сервера, операция не подтверждена, id операции 1", exception.getMessage());
    }
}
