package ru.netology.CourseProject_3_TransferMoney.repository;

import org.springframework.stereotype.Repository;
import ru.netology.CourseProject_3_TransferMoney.Logger.Logger;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorConfirmation;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;
import ru.netology.CourseProject_3_TransferMoney.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TransferRepository {
    private final List<Card> listDemo = new ArrayList<>(Arrays.asList( // List для удобства добавления
            new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")),
            new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")),
            new Card("3456789012345678", "03/25", "345", new Amount(200_000, "rub")),
            new Card("4567890123456789", "04/25", "456", new Amount(3_000, "usd")),
            new Card("5678901234567890", "05/25", "567", new Amount(4_000, "usd")),
            new Card("6789012345678901", "06/25", "678", new Amount(5_000, "usd")),
            new Card("7890123456789012", "07/25", "789", new Amount(20_000, "cny"))
    ));
    private final ConcurrentHashMap<String, Card> listOfCardDemo = new ConcurrentHashMap<>(); // перевела карты в мапу
    {
        listOfCardDemo.put(listDemo.get(0).getNumber(), listDemo.get(0));
        listOfCardDemo.put(listDemo.get(1).getNumber(), listDemo.get(1));
        listOfCardDemo.put(listDemo.get(2).getNumber(), listDemo.get(2));
        listOfCardDemo.put(listDemo.get(3).getNumber(), listDemo.get(3));
        listOfCardDemo.put(listDemo.get(4).getNumber(), listDemo.get(4));
        listOfCardDemo.put(listDemo.get(5).getNumber(), listDemo.get(5));
        listOfCardDemo.put(listDemo.get(6).getNumber(), listDemo.get(6));
    }

    private final AtomicInteger cntId = new AtomicInteger(0);
    private final HashMap<String, String> mapConfirmOperation = new HashMap<>(); // не потокобезопасная, т.к. нет необходимости,
    // все операции будут иметь уникальные номера, а после подтверждения операции можно чистить эту мапу от подтвержденного эл-та
    private final HashMap<String, DataForm> mapOperationID = new HashMap<>(); // связывает ID с DataForm

    public int getCntId() {
        return cntId.get();
    }

    public boolean isExistCardFrom(String cardNumber, String cardValidTill, String cardCvc) {
        if (listOfCardDemo.containsKey(cardNumber)) {
            return listOfCardDemo.get(cardNumber).getValidTill().equals(cardValidTill)
                    && listOfCardDemo.get(cardNumber).getCvc().equals(cardCvc);
        }
        return false;
    }

    public boolean isExistCardTo(String cardNumber) {
        return listOfCardDemo.containsKey(cardNumber);
    }

    public Card getCard(String cardNumber) {
        return listOfCardDemo.get(cardNumber);
    }

    public String transferMoney(DataForm dataForm) throws IOException {
        Logger logger = Logger.getInstance();

        OperationId operationId = new OperationId();
        operationId.setOperationId(cntId.incrementAndGet());
        mapOperationID.put(operationId.getOperationId(), dataForm); // хранит ВСЕ операции

        logger.log("ЗАПРОС НА ПЕРЕВОД, operationId " + operationId + ": карта отправителя " + dataForm.getCardFromNumber() +
                ", карта получателя " + dataForm.getCardToNumber() + ", \nсумма " + dataForm.getAmount().getValue());
        if (isExistCardFrom(dataForm.getCardFromNumber(), dataForm.getCardFromValidTill(), dataForm.getCardFromCVV())) {
            if (isExistCardTo(dataForm.getCardToNumber())) {

                Amount amountWithCommission = new Amount(dataForm.getAmount().getValue() * 101 / 100, dataForm.getAmount().getCurrency());

                Card cardFrom = getCard(dataForm.getCardFromNumber());
                Card cardTo = getCard(dataForm.getCardToNumber());

                if (cardFrom.getBalance().getCurrency().equals(cardTo.getBalance().getCurrency())) { // проверка на валюту, есть в классе Карта
                    if (cardFrom.getBalance().getValue() >= amountWithCommission.getValue()) { // проверка баланса, есть в классе Карта

                        // операция возможна, создаю объект ConfirmOperation
                        ConfirmOperation confirmOperation = new ConfirmOperation(operationId);
                        mapConfirmOperation.put(confirmOperation.getOperationId(), confirmOperation.getCode());
                        return operationId.toString();
                    }
                    logger.log(" ОШИБКА перевода с карты " + cardFrom.getNumber() + " на карту " + cardTo.getNumber()
                            + ", сумма операции c комиссией: " + amountWithCommission + "\nПРИЧИНА ОТКАЗА: НЕДОСТАТОЧНО СРЕДСТВ"
                            + ", остаток на карте ОТПРАВИТЕЛЯ " + cardFrom.getBalance() + ", остаток у ПОЛУЧАТЕЛЯ " + cardTo.getBalance());
                    throw new ErrorInputData("Error input data - Неверные входные данные. НЕДОСТАТОЧНО СРЕДСТВ", operationId.getOperationId());
                }
                logger.log(" ОШИБКА перевода с карты " + cardFrom.getNumber() + " на карту " + cardTo.getNumber()
                        + ", сумма операции: " + dataForm.getAmount() + ". \nПРИЧИНА ОТКАЗА: НЕСООТВЕТСТВИЕ ВАЛЮТ КАРТЫ ОТПРАВИТЕЛЯ И ПОЛУЧАТЕЛЯ."
                        + " Oстаток на карте ОТПРАВИТЕЛЯ " + cardFrom.getBalance() + ", остаток у ПОЛУЧАТЕЛЯ " + cardTo.getBalance());
                throw new ErrorTransfer("Error transfer - ошибка перевода. РАЗНЫЕ ВАЛЮТЫ", operationId.getOperationId());
            }
            logger.log(" ОШИБКА перевода с карты " + dataForm.getCardFromNumber() + " на карту " + dataForm.getCardToNumber()
                    + ", сумма операции: " + dataForm.getAmount().getValue() + ". ПРИЧИНА ОТКАЗА: КАРТА ПОЛУЧАТЕЛЬ НЕ НАЙДЕНА");
            throw new ErrorInputData("Error input data - Неверные входные данные. КАРТА ПОЛУЧАТЕЛЬ НЕ НАЙДЕНА", operationId.getOperationId());
        }
        logger.log(" ОШИБКА перевода с карты " + dataForm.getCardFromNumber() + " на карту " + dataForm.getCardToNumber()
                + ", сумма операции: " + dataForm.getAmount().getValue() + " ПРИЧИНА ОТКАЗА: КАРТА ОТПРАВИТЕЛЬ НЕ НАЙДЕНА");
        throw new ErrorInputData("Error input data - Неверные входные данные. КАРТА ОТПРАВИТЕЛЬ НЕ НАЙДЕНА", operationId.getOperationId());
    }

    public String confirmOperation(String operationId, String code) throws IOException {
        Logger logger = Logger.getInstance();

        Amount amountWithCommission = new Amount(mapOperationID.get(operationId).getAmount().getValue() * 101 / 100,
                mapOperationID.get(operationId).getAmount().getCurrency());

        if (mapConfirmOperation.containsKey(operationId)) {
            if (mapConfirmOperation.get(operationId).equals(code)) { // проверяем совпадение введенного кода с требуемым (в данном случае конечно всегда будет совпадать)

                if (getCard(mapOperationID.get(operationId).getCardFromNumber()).reductionBalance(amountWithCommission)) { // еще раз проверка баланса, т.к. не потокобезопасно
                    getCard(mapOperationID.get(operationId).getCardToNumber()).addBalance(mapOperationID.get(operationId).getAmount()); // зачисление на карту-получатель

                    logger.log(" Успешно! operationId " + operationId + " Перевод с карты " + mapOperationID.get(operationId).getCardFromNumber() + " на карту "
                            + mapOperationID.get(operationId).getCardToNumber() + ", сумма операции: "
                            + mapOperationID.get(operationId).getAmount().getValue() + ",\n комиссия: "
                            + (mapOperationID.get(operationId).getAmount().getValue()/100) + ", остаток на карте ОТПРАВИТЕЛЯ "
                            + getCard(mapOperationID.get(operationId).getCardFromNumber()).getBalance() + ", остаток у ПОЛУЧАТЕЛЯ "
                            + getCard(mapOperationID.get(operationId).getCardToNumber()).getBalance());
                    return operationId;
                }
                logger.log("ОШИБКА СПИСАНИЯ, что-то пошло не так");
                throw new ErrorConfirmation("Error confirmation - ошибка сервера, что-то пошло не так, операция не подтверждена", operationId);
            }
            logger.log("ОШИБКА подтверждения операции " + operationId + " ВВЕДЕН НЕВЕРНЫЙ КОД");
            throw new ErrorInputData("Error input data - Неверные входные данные. ВВЕДЕН НЕВЕРНЫЙ КОД", operationId);
        }
        logger.log("Error confirmation - ошибка сервера, операция не подтверждена");
        throw new ErrorConfirmation("Error confirmation - ошибка сервера, операция не подтверждена", operationId);
    }
}