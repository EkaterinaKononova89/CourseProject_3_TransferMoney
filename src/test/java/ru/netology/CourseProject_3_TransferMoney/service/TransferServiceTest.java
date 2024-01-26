package ru.netology.CourseProject_3_TransferMoney.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.CourseProject_3_TransferMoney.bank.Bank;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorConfirmation;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;
import ru.netology.CourseProject_3_TransferMoney.logger.Logger;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;
import ru.netology.CourseProject_3_TransferMoney.repository.TransferRepository;
import ru.netology.CourseProject_3_TransferMoney.servise.CheckCardService;
import ru.netology.CourseProject_3_TransferMoney.servise.TransferService;

import java.io.IOException;
import java.util.HashMap;

public class TransferServiceTest {
    TransferService sut;
    TransferRepository repository = Mockito.mock(TransferRepository.class);
    CheckCardService checkCard = Mockito.mock(CheckCardService.class);
    Bank bank = Mockito.mock(Bank.class);
    Logger logger = Mockito.mock(Logger.class);

    @BeforeEach
    public void beforeEach() {
        sut = new TransferService(repository, checkCard, bank, logger);
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @Test
    public void countCommissionTest() {
        //given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        //when
        Integer result = sut.countCommission(dataForm);

        //then
        Assertions.assertEquals(100, result);
    }

    @Test
    public void countAmountWithCommissionTest() {
        //given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        //when
        Amount result = sut.countAmountWithCommission(dataForm);

        //then
        Assertions.assertEquals(10_100, result.getValue());
    }

    @Test
    public void transferMoneyTest() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        OperationId operationId = new OperationId("1");
        Mockito.when(repository.createNewOperationId(dataForm)).thenReturn(operationId);
        Mockito.when(checkCard.isExistCardFrom(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(checkCard.isExistCardTo(Mockito.any())).thenReturn(true);
        Mockito.when(bank.sayYes(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(repository.createConfirmOperation(operationId)).thenReturn(operationId);

        //when
        OperationId result = sut.transferMoney(dataForm);

        //then
        Assertions.assertEquals("1", result.toString());
    }

    @Test
    public void transferMoneyTest_errorCardFrom400() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123455", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        OperationId operationId = new OperationId("1");
        Mockito.when(repository.createNewOperationId(dataForm)).thenReturn(operationId);
        Mockito.when(checkCard.isExistCardFrom(dataForm.getCardFromNumber(), dataForm.getCardFromValidTill(), dataForm.getCardFromCVV())).thenReturn(false);

        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. КАРТА ОТПРАВИТЕЛЬ НЕ НАЙДЕНА, " +
                "id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyTest_errorCardTo400() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234568", new Amount(10_000, "RUR"));

        OperationId operationId = new OperationId("1");
        Mockito.when(repository.createNewOperationId(dataForm)).thenReturn(operationId);
        Mockito.when(checkCard.isExistCardFrom(dataForm.getCardFromNumber(), dataForm.getCardFromValidTill(), dataForm.getCardFromCVV())).thenReturn(true);
        Mockito.when(checkCard.isExistCardTo(dataForm.getCardToNumber())).thenReturn(false);

        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. КАРТА ПОЛУЧАТЕЛЬ НЕ НАЙДЕНА, " +
                "id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyTest_errorBank400() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234568", new Amount(10_000, "RUR"));

        OperationId operationId = new OperationId("1");
        Mockito.when(repository.createNewOperationId(dataForm)).thenReturn(operationId);
        Mockito.when(checkCard.isExistCardFrom(dataForm.getCardFromNumber(), dataForm.getCardFromValidTill(), dataForm.getCardFromCVV())).thenReturn(true);
        Mockito.when(checkCard.isExistCardTo(dataForm.getCardToNumber())).thenReturn(true);
        Mockito.when(bank.sayYes(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. ОТКЛОНЕНО БАНКОМ, " +
                "id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyTest_error500() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234568", new Amount(10_000, "RUR"));

        OperationId operationId = new OperationId("1");
        Mockito.when(repository.createNewOperationId(dataForm)).thenReturn(operationId);
        Mockito.when(checkCard.isExistCardFrom(dataForm.getCardFromNumber(), dataForm.getCardFromValidTill(), dataForm.getCardFromCVV())).thenReturn(true);
        Mockito.when(checkCard.isExistCardTo(dataForm.getCardToNumber())).thenReturn(true);
        Mockito.when(bank.sayYes(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(repository.createConfirmOperation(operationId)).thenReturn(null); // без Mockito тоже возвращает null

        //when

        //then
        ErrorTransfer exception = Assertions.assertThrows(ErrorTransfer.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("ErrorTransfer - ошибка сервера, " +
                "id операции 1", exception.getMessage());
    }

    @Test
    public void confirmOperation() throws IOException {
        //given
        ConfirmOperation confirmOperation1 = new ConfirmOperation("1", "0000");
        ConfirmOperation confirmOperation2 = new ConfirmOperation("2", "2222");
        ConfirmOperation confirmOperation3 = new ConfirmOperation("3", "3333");

        HashMap<String, String> mapConfirmOperationTest = new HashMap<>();
        mapConfirmOperationTest.put("1", "0000");
        mapConfirmOperationTest.put("2", "2222");
        mapConfirmOperationTest.put("3", "3333");

        HashMap<String, DataForm> mapOperationIDTest = new HashMap<>();
        mapOperationIDTest.put("1", new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR")));
        mapOperationIDTest.put("2", new DataForm("2345678901234567", "01/25", "123",
                "1234567890123456", new Amount(10_000, "RUR")));
        mapOperationIDTest.put("3", new DataForm("3456789012345678", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR")));


        HashMap<String, OperationId> operationIdAndConfirmOperationTest = new HashMap<>();
        operationIdAndConfirmOperationTest.put("1", new OperationId("1"));
        operationIdAndConfirmOperationTest.put("2", new OperationId("2"));
        operationIdAndConfirmOperationTest.put("3", new OperationId("3"));

        Mockito.when(repository.getMapConfirmOperation()).thenReturn(mapConfirmOperationTest);
        Mockito.when(repository.getOperationIdAndConfirmOperation()).thenReturn(operationIdAndConfirmOperationTest);
        Mockito.when(repository.getMapOperationID()).thenReturn(mapOperationIDTest);

        //when
        OperationId result1 = sut.confirmOperation(confirmOperation1);
        OperationId result2 = sut.confirmOperation(confirmOperation2);
        OperationId result3 = sut.confirmOperation(confirmOperation3);
        //then
        Assertions.assertEquals("1", result1.toString());
        Assertions.assertEquals("2", result2.toString());
        Assertions.assertEquals("3", result3.toString());
    }

    @Test
    public void confirmOperation_error400() throws IOException {
        //given
        ConfirmOperation confirmOperation = new ConfirmOperation("1", "0000");

        HashMap<String, String> mapConfirmOperationTest = new HashMap<>();
        mapConfirmOperationTest.put("1", "1111");

        Mockito.when(repository.getMapConfirmOperation()).thenReturn(mapConfirmOperationTest);

        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.confirmOperation(confirmOperation);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. ВВЕДЕН НЕВЕРНЫЙ КОД, id операции 1", exception.getMessage());
    }

    @Test
    public void confirmOperation_error500() throws IOException {
        //given
        ConfirmOperation confirmOperation = new ConfirmOperation("1", "0000");

        HashMap<String, String> mapConfirmOperationTest = new HashMap<>();
        mapConfirmOperationTest.put("2", "2222");

        Mockito.when(repository.getMapConfirmOperation()).thenReturn(mapConfirmOperationTest);

        //when

        //then
        ErrorConfirmation exception = Assertions.assertThrows(ErrorConfirmation.class, () -> {
            sut.confirmOperation(confirmOperation);
        });
        Assertions.assertEquals("Error confirmation - ошибка сервера, операция не подтверждена, id операции 1", exception.getMessage());
    }
}
