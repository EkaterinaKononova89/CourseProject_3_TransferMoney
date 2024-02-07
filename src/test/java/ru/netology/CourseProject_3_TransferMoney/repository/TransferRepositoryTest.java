package ru.netology.CourseProject_3_TransferMoney.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;

public class TransferRepositoryTest {
    TransferRepository sut;

    @BeforeEach
    public void beforeEach() {
        sut = new TransferRepository();
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @Test
    public void createNewOperationIdTest_id() {
        // given
        DataForm dataForm = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));

        //when
        OperationId result1 = sut.createNewOperationId(dataForm);
        OperationId result2 = sut.createNewOperationId(dataForm);

        //then
        Assertions.assertEquals("1", result1.toString());
        Assertions.assertEquals("2", result2.toString());
    }

    @Test
    public void createNewOperationIdTest_map() {
        // given
        DataForm dataForm1 = new DataForm("1234567890123456", "01/25", "123",
                "2345678901234567", new Amount(10_000, "RUR"));
        DataForm dataForm2 = new DataForm("2345678901234567", "02/25", "234",
                "1234567890123456", new Amount(10_000, "RUR"));

        //when
        OperationId key1 = sut.createNewOperationId(dataForm1);
        OperationId key2 = sut.createNewOperationId(dataForm2);
        DataForm result1 = sut.getMapOperationID().get(key1.toString());
        DataForm result2 = sut.getMapOperationID().get(key2.toString());

        //then
        Assertions.assertEquals(dataForm1, result1);
        Assertions.assertEquals(dataForm2, result2);
    }

    @Test
    public void createConfirmOperationTest_id() {
        //given
        OperationId operationId = new OperationId();

        //when
        OperationId result = sut.createConfirmOperation(operationId);

        //then
        Assertions.assertEquals(operationId, result);
    }
}
