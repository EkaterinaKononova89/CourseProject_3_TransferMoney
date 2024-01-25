package ru.netology.CourseProject_3_TransferMoney.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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

import java.io.IOException;


@Service
@AllArgsConstructor
public class TransferService implements ServiceInterface {
    private final TransferRepository repository;
    private final CheckCardService checkCard;
    private final Bank bank;

    public Integer countCommission(DataForm dataForm) {
        return dataForm.getAmount().getValue() / 100;
    }

    public Amount countAmountWithCommission(DataForm dataForm) {
        return new Amount(dataForm.getAmount().getValue() + countCommission(dataForm), dataForm.getAmount().getCurrency());
    }

    public OperationId transferMoney(DataForm frontDataForm) throws IOException {
        Logger logger = Logger.getInstance();

        DataForm dataForm = frontDataForm.divideBy100();
        OperationId operationId = repository.createNewOperationId(dataForm);

        synchronized ("File.log") {
            logger.log("ЗАПРОС НА ПЕРЕВОД, operationId " + operationId + ": карта отправителя " + dataForm.getCardFromNumber() +
                    ", карта получателя " + dataForm.getCardToNumber() + ", \nсумма " + dataForm.getAmount().getValue());
        }
        if (checkCard.isExistCardFrom(dataForm.getCardFromNumber(), dataForm.getCardFromValidTill(), dataForm.getCardFromCVV())) {
            if (checkCard.isExistCardTo(dataForm.getCardToNumber())) {

                Amount amountWithCommission = countAmountWithCommission(dataForm);

                if (bank.sayYes(checkCard.getCard(dataForm.getCardFromNumber()), checkCard.getCard(dataForm.getCardToNumber()), amountWithCommission)) {
                    // операция возможна, создаю объект ConfirmOperation
                    OperationId confirmOperationById = repository.createConfirmOperation(operationId);
                    if (confirmOperationById == null) {
                        synchronized ("File.log") {
                            logger.log(" ОШИБКА перевода с карты " + dataForm.getCardFromNumber() + " на карту " + dataForm.getCardToNumber()
                                    + ", сумма операции c комиссией: " + amountWithCommission + "\nПРИЧИНА ОТКАЗА: ");
                        }
                        throw new ErrorTransfer("ErrorTransfer - ошибка сервера", operationId.getOperationId());
                    }
                    return confirmOperationById;
                }
                synchronized ("File.log") {
                    logger.log(" ОШИБКА перевода с карты " + dataForm.getCardFromNumber() + " на карту " + dataForm.getCardToNumber()
                            + ", сумма операции c комиссией: " + amountWithCommission + "\nПРИЧИНА ОТКАЗА: ");
                }
                throw new ErrorInputData("Error input data - Неверные входные данные. ОТКЛОНЕНО БАНКОМ", operationId.getOperationId());
            }
            synchronized ("File.log") {
                logger.log(" ОШИБКА перевода с карты " + dataForm.getCardFromNumber() + " на карту " + dataForm.getCardToNumber()
                        + ", сумма операции: " + dataForm.getAmount().getValue() + ". ПРИЧИНА ОТКАЗА: ");
            }
            throw new ErrorInputData("Error input data - Неверные входные данные. КАРТА ПОЛУЧАТЕЛЬ НЕ НАЙДЕНА", operationId.getOperationId());
        }
        synchronized ("File.log") {
            logger.log(" ОШИБКА перевода с карты " + dataForm.getCardFromNumber() + " на карту " + dataForm.getCardToNumber()
                    + ", сумма операции: " + dataForm.getAmount().getValue() + " ПРИЧИНА ОТКАЗА: ");
        }
        throw new ErrorInputData("Error input data - Неверные входные данные. КАРТА ОТПРАВИТЕЛЬ НЕ НАЙДЕНА", operationId.getOperationId());
    }

    public OperationId confirmOperation(ConfirmOperation confirmOperation) throws IOException {
        Logger logger = Logger.getInstance();

        if (repository.getMapConfirmOperation().containsKey(confirmOperation.getOperationId())) {
            if (repository.getMapConfirmOperation().get(confirmOperation.getOperationId()).equals(confirmOperation.getCode())) { // проверяем совпадение введенного кода с требуемым (в данном случае конечно всегда будет совпадать)

                bank.codeTrue(); // говорим банку, что код верный
                synchronized ("File.log") {
                    logger.log(" Успешно! operationId "
                            + confirmOperation.getOperationId() + " Перевод с карты " +
                            repository.getMapOperationID().get(confirmOperation.getOperationId()).getCardFromNumber() + " на карту "
                            + repository.getMapOperationID().get(confirmOperation.getOperationId()).getCardToNumber() + ", сумма операции: "
                            + repository.getMapOperationID().get(confirmOperation.getOperationId()).getAmount().getValue() + ",\n комиссия: "
                            + (repository.getMapOperationID().get(confirmOperation.getOperationId()).getAmount().getValue() / 100));
                }
                return repository.getOperationIdAndConfirmOperation().get(confirmOperation.getOperationId());
            }
            synchronized ("File.log") {
                logger.log("ОШИБКА подтверждения операции " + confirmOperation.getOperationId() + " ПРИЧИНА ОТКАЗА:");
            }
            throw new ErrorInputData("Error input data - Неверные входные данные. ВВЕДЕН НЕВЕРНЫЙ КОД", confirmOperation.getOperationId());
        }
        synchronized ("File.log") {
            logger.log("ОШИБКА сервера, операция " + confirmOperation.getOperationId() + " не подтверждена");
        }
        throw new ErrorConfirmation("Error confirmation - ошибка сервера, операция не подтверждена", confirmOperation.getOperationId());
    }

    public int cntId() { // метод для корректной работы фронта
        return repository.getCntId();
    }
}
