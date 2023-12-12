package ru.netology.CourseProject_3_TransferMoney.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardTest {
    Card sut;

    @BeforeEach
    public void beforeEach() {
        sut = new Card("1234567890123456", "01/25", "123", new Amount(10_000, "RUR"));
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @Test
    public void addBalanceTest() {
        // given
        Amount amount = new Amount(1_000, "RUR");
        int balanceValue= sut.getBalance().getValue();

        //when
        sut.addBalance(amount);

        //then
        int resultBalance = sut.getBalance().getValue();
        Assertions.assertEquals(balanceValue + 1_000, resultBalance);
    }

    @Test
    public void addBalanceTestErrorDifferentCurrency() {
        // given
        Amount amount = new Amount(1_000, "usd");

        //when
        boolean result = sut.addBalance(amount);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    public void reductionBalanceTest() {
        // given
        Amount amount = new Amount(1_000, "RUR");
        int balanceValue= sut.getBalance().getValue();

        //when
        sut.reductionBalance(amount);

        //then
        int resultBalance = sut.getBalance().getValue();
        Assertions.assertEquals(balanceValue - 1_000, resultBalance);
    }

    @Test
    public void reductionBalanceTestError() {
        // given
        Amount amount = new Amount(1_000, "usd");

        //when
        boolean result = sut.reductionBalance(amount);

        //then
        Assertions.assertFalse(result);
    }
}
