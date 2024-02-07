package ru.netology.CourseProject_3_TransferMoney.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.Card;
import ru.netology.CourseProject_3_TransferMoney.repository.TransferRepository;

import java.util.concurrent.ConcurrentHashMap;

public class CheckCardServiceTest {
    CheckCardService sut;
    TransferRepository repository = Mockito.mock(TransferRepository.class);

    @BeforeEach
    public void beforeEach() {
        sut = new CheckCardService(repository);
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @Test
    public void isExistCardFromTest_true() {
        //given
        String cardNumber = "1234567890123456";
        String cardValidTill = "01/25";
        String cardCvc = "123";

        ConcurrentHashMap<String, Card> listOfCardDemoTest = new ConcurrentHashMap<>();
        listOfCardDemoTest.put("1234567890123456", new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")));
        listOfCardDemoTest.put("2345678901234567", new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")));

        Mockito.when(repository.getListOfCardDemo()).thenReturn(listOfCardDemoTest);

        //when
        boolean result = sut.isExistCardFrom(cardNumber, cardValidTill, cardCvc);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    public void isExistCardFromTest_false() {
        //given
        String cardNumber = "3456789012345678";
        String cardValidTill = "03/25";
        String cardCvc = "345";

        ConcurrentHashMap<String, Card> listOfCardDemoTest = new ConcurrentHashMap<>();
        listOfCardDemoTest.put("1234567890123456", new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")));
        listOfCardDemoTest.put("2345678901234567", new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")));

        Mockito.when(repository.getListOfCardDemo()).thenReturn(listOfCardDemoTest);

        //when
        boolean result = sut.isExistCardFrom(cardNumber, cardValidTill, cardCvc);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    public void isExistCardToTest_true() {
        //given
        String cardNumber = "1234567890123456";

        ConcurrentHashMap<String, Card> listOfCardDemoTest = new ConcurrentHashMap<>();
        listOfCardDemoTest.put("1234567890123456", new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")));
        listOfCardDemoTest.put("2345678901234567", new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")));

        Mockito.when(repository.getListOfCardDemo()).thenReturn(listOfCardDemoTest);

        //when
        boolean result = sut.isExistCardTo(cardNumber);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    public void isExistCardToTest_false() {
        //given
        String cardNumber = "3456789012345678";

        ConcurrentHashMap<String, Card> listOfCardDemoTest = new ConcurrentHashMap<>();
        listOfCardDemoTest.put("1234567890123456", new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")));
        listOfCardDemoTest.put("2345678901234567", new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")));

        Mockito.when(repository.getListOfCardDemo()).thenReturn(listOfCardDemoTest);

        //when
        boolean result = sut.isExistCardTo(cardNumber);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    public void getCardTest() {
        //given
        String cardNumber = "1234567890123456";

        ConcurrentHashMap<String, Card> listOfCardDemoTest = new ConcurrentHashMap<>();
        listOfCardDemoTest.put("1234567890123456", new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")));
        listOfCardDemoTest.put("2345678901234567", new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")));

        Mockito.when(repository.getListOfCardDemo()).thenReturn(listOfCardDemoTest);

        //when
        Card result = sut.getCard(cardNumber);

        //then
        Assertions.assertEquals(listOfCardDemoTest.get("1234567890123456"), result);
    }
}
