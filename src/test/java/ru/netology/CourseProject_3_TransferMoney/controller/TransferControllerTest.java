package ru.netology.CourseProject_3_TransferMoney.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.servise.TransferService;

import java.io.IOException;

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
    public void transferMoneyTest() throws IOException {
        //given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(1_000_000, "RUR"));
        Mockito.when(service.transferMoney(dataForm.divideBy100())).thenReturn("1");

        //when
        String result = sut.transferMoney(dataForm);

        //then
        Assertions.assertEquals("1", result);
    }

    @Test
    public void transferMoneyConfirm() throws IOException {
        //given
        ConfirmOperation confirmOperation = new ConfirmOperation("1", "0000");
        Mockito.when(service.confirmOperation(confirmOperation)).thenReturn(confirmOperation.getOperationId());

        //when
        String result = sut.transferMoneyConfirm(confirmOperation);

        //then
        Assertions.assertEquals("1", result);
    }
}
