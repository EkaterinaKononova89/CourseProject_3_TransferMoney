package ru.netology.CourseProject_3_TransferMoney.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.Card;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TransferRepositoryTest {
    TransferRepository sut;

    @BeforeEach
    public void beforeEach() {
        sut = new TransferRepository();
        ConcurrentHashMap<String, Card> listOfCardDemo = new ConcurrentHashMap<>();
        {
            listOfCardDemo.put("1234567890123456", new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")));
            listOfCardDemo.put("2345678901234567", new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")));
            listOfCardDemo.put("3456789012345678", new Card("3456789012345678", "03/25", "345", new Amount(200_000, "rub")));
        }
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @Test
    public void isExistCardFromTest_True() {
        // given
        String cardNumber = "1234567890123456";
        String cardValidTill = "01/25";
        String cardCvc = "123";

        //when
        boolean result = sut.isExistCardFrom(cardNumber, cardValidTill, cardCvc);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    public void isExistCardFromTest_False() {
        // given
        String cardNumber = "1234567890123456";
        String cardValidTill = "01/25";
        String cardCvc = "122";

        //when
        boolean result = sut.isExistCardFrom(cardNumber, cardValidTill, cardCvc);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    public void isExistCardToTest_True() {
        // given
        String cardNumber = "2345678901234567";

        //when
        boolean result = sut.isExistCardTo(cardNumber);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    public void isExistCardToTest_False() {
        // given
        String cardNumber = "2345678901234566";

        //when
        boolean result = sut.isExistCardTo(cardNumber);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    public void transferMoneyTest() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        //when
        String result1 = sut.transferMoney(dataForm);
        String result2 = sut.transferMoney(dataForm);

        //then
        Assertions.assertEquals("1", result1);
        Assertions.assertEquals("2", result2);
    }

    @Test
    public void transferMoneyTest_errorCardFrom400() throws IOException {
        // given
        DataForm dataForm = new DataForm("1111222233331111", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        //when

        //then
        Assertions.assertThrows(ErrorInputData.class, () -> { // данное действие увеличивает id операции до 1
            sut.transferMoney(dataForm);
        });

        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> { // данное действие увеличивает id операции до 2
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. КАРТА ОТПРАВИТЕЛЬ НЕ НАЙДЕНА, id операции 2", exception.getMessage());
    }

    @Test
    public void transferMoneyTest_errorCardTo400() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234566", new Amount(10_000, "RUR"));

        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> { //
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. КАРТА ПОЛУЧАТЕЛЬ НЕ НАЙДЕНА, id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyTest_errorCurrency500() throws IOException {
        // given
        DataForm dataForm = new DataForm("4567890123456789", "04/25", "456",
                "2345678901234567", new Amount(10_000, "RUR"));

        //when

        //then
        ErrorTransfer exception = Assertions.assertThrows(ErrorTransfer.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error transfer - ошибка перевода. РАЗНЫЕ ВАЛЮТЫ, id операции 1", exception.getMessage());
    }

    @Test
    public void transferMoneyTest_errorBalance400() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(99_900, "RUR"));

        //when

        //then
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.transferMoney(dataForm);
        });
        Assertions.assertEquals("Error input data - Неверные входные данные. НЕДОСТАТОЧНО СРЕДСТВ, id операции 1", exception.getMessage());
    }

    @Test
    public void confirmOperationTest_id() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        //when
        String operationId = sut.transferMoney(dataForm);
        String result = sut.confirmOperation(operationId, "0000");

        //then
        Assertions.assertEquals(operationId, result);
    }

    @Test
    public void confirmOperationTest_changeBalance() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        Card cardFrom = sut.getCard("1234567890123456");
        int balanceCardFrom = cardFrom.getBalance().getValue();

        Card cardTo = sut.getCard("2345678901234567");
        int balanceCardTo = cardTo.getBalance().getValue();

        //when
        String operationId = sut.transferMoney(dataForm);
        sut.confirmOperation(operationId, "0000");

        //then
        int newBalanceCardFrom = cardFrom.getBalance().getValue();
        int newBalanceCardTo = cardTo.getBalance().getValue();
        Assertions.assertEquals(balanceCardFrom - 10_100, newBalanceCardFrom);
        Assertions.assertEquals(balanceCardTo + 10_000, newBalanceCardTo);
    }

    @Test
    public void confirmOperationTest_errorCode400() throws IOException {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        Card cardFrom = sut.getCard("1234567890123456");
        int balanceCardFrom = cardFrom.getBalance().getValue();

        Card cardTo = sut.getCard("2345678901234567");
        int balanceCardTo = cardTo.getBalance().getValue();

        //when
        String operationId = sut.transferMoney(dataForm);
        ErrorInputData exception = Assertions.assertThrows(ErrorInputData.class, () -> {
            sut.confirmOperation(operationId, "0001");
        });

        //then
        int newBalanceCardFrom = cardFrom.getBalance().getValue();
        int newBalanceCardTo = cardTo.getBalance().getValue();
        Assertions.assertEquals(balanceCardFrom, newBalanceCardFrom);
        Assertions.assertEquals(balanceCardTo, newBalanceCardTo);
        Assertions.assertEquals("Error input data - Неверные входные данные. ВВЕДЕН НЕВЕРНЫЙ КОД, id операции 1", exception.getMessage());
    }
}
