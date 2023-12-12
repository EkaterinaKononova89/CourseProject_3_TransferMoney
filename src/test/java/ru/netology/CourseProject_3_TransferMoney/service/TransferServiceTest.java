package ru.netology.CourseProject_3_TransferMoney.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.repository.TransferRepository;
import ru.netology.CourseProject_3_TransferMoney.servise.TransferService;

import java.io.IOException;

public class TransferServiceTest {
    TransferService sut;
    TransferRepository repository = Mockito.mock(TransferRepository.class);

    @BeforeEach
    public void beforeEach() {
        sut = new TransferService(repository);
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
        Mockito.when(repository.transferMoney(dataForm.divideBy100())).thenReturn("1");

        //when
        String result = sut.transferMoney(dataForm);

        //then
        Assertions.assertEquals("1", result);
    }

    @Test
    public void confirmOperation() throws IOException {
        //given
        ConfirmOperation confirmOperation = new ConfirmOperation("1", "0000");
        Mockito.when(repository.confirmOperation(confirmOperation.getOperationId(), confirmOperation.getCode())).thenReturn(confirmOperation.getOperationId());

        //when
        String result = sut.confirmOperation(confirmOperation);

        //then
        Assertions.assertEquals("1", result);
    }
}
