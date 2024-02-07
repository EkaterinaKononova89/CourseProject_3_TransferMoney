package ru.netology.CourseProject_3_TransferMoney.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import ru.netology.CourseProject_3_TransferMoney.bank.Bank;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorConfirmation;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.model.OperationId;
import ru.netology.CourseProject_3_TransferMoney.repository.TransferRepository;

@Service
@RequiredArgsConstructor
@CommonsLog
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

    public OperationId transferMoney(DataForm frontDataForm) {

        DataForm dataForm = frontDataForm.divideBy100();
        OperationId operationId = repository.createNewOperationId(dataForm);

        log.info("ЗАПРОС НА ПЕРЕВОД, operationId " + operationId + ": карта отправителя " + dataForm.getCardFromNumber() +
                ", карта получателя " + dataForm.getCardToNumber() + ", сумма " + dataForm.getAmount().getValue());

        if (checkCard.isExistCardFrom(dataForm.getCardFromNumber(), dataForm.getCardFromValidTill(), dataForm.getCardFromCVV())) {
            if (checkCard.isExistCardTo(dataForm.getCardToNumber())) {

                Amount amountWithCommission = countAmountWithCommission(dataForm);

                if (bank.sayYes(checkCard.getCard(dataForm.getCardFromNumber()), checkCard.getCard(dataForm.getCardToNumber()), amountWithCommission)) {
                    // операция возможна, создаю объект ConfirmOperation
                    OperationId confirmOperationById = repository.createConfirmOperation(operationId);
                    if (confirmOperationById == null) {
                        log.error("ОШИБКА operationId " + operationId + ": перевод с карты " + dataForm.getCardFromNumber() +
                                " на карту " + dataForm.getCardToNumber() + ", сумма операции c комиссией: " + amountWithCommission + ". ПРИЧИНА ОТКАЗА: ");
                        throw new ErrorTransfer("ErrorTransfer - ошибка сервера", operationId.getOperationId());
                    }
                    return confirmOperationById;
                }
                log.error("ОШИБКА operationId " + operationId + ": перевод с карты " + dataForm.getCardFromNumber() +
                        " на карту " + dataForm.getCardToNumber() + ", сумма операции c комиссией: " + amountWithCommission + ". ПРИЧИНА ОТКАЗА: ");
                throw new ErrorInputData("Error input data - Неверные входные данные. ОТКЛОНЕНО БАНКОМ", operationId.getOperationId());
            }
            log.error("ОШИБКА operationId " + operationId + ": перевод с карты " + dataForm.getCardFromNumber() + " на карту "
                    + dataForm.getCardToNumber() + ", сумма операции: " + dataForm.getAmount().getValue() + ". ПРИЧИНА ОТКАЗА: ");
            throw new ErrorInputData("Error input data - Неверные входные данные. КАРТА ПОЛУЧАТЕЛЬ НЕ НАЙДЕНА", operationId.getOperationId());
        }
        log.error("ОШИБКА operationId " + operationId + ": перевод с карты " + dataForm.getCardFromNumber() + " на карту "
                + dataForm.getCardToNumber() + ", сумма операции: " + dataForm.getAmount().getValue() + ". ПРИЧИНА ОТКАЗА: ");
        throw new ErrorInputData("Error input data - Неверные входные данные. КАРТА ОТПРАВИТЕЛЬ НЕ НАЙДЕНА", operationId.getOperationId());
    }

    public OperationId confirmOperation(ConfirmOperation confirmOperation) {

        if (repository.getMapConfirmOperation().containsKey(confirmOperation.getOperationId())) {
            if (repository.getMapConfirmOperation().get(confirmOperation.getOperationId()).equals(confirmOperation.getCode())) { // проверяем совпадение введенного кода с требуемым (в данном случае конечно всегда будет совпадать)

                bank.codeTrue(); // говорим банку, что код верный
                log.info("Успешно! operationId " + confirmOperation.getOperationId() + ". Перевод с карты "
                        + repository.getMapOperationID().get(confirmOperation.getOperationId()).getCardFromNumber()
                        + " на карту " + repository.getMapOperationID().get(confirmOperation.getOperationId()).getCardToNumber()
                        + ", сумма операции: " + repository.getMapOperationID().get(confirmOperation.getOperationId()).getAmount().getValue()
                        + ",комиссия: " + (repository.getMapOperationID().get(confirmOperation.getOperationId()).getAmount().getValue() / 100));
                return repository.getOperationIdAndConfirmOperation().get(confirmOperation.getOperationId());
            }
            log.error("ОШИБКА подтверждения операции " + confirmOperation.getOperationId() + " ПРИЧИНА ОТКАЗА:");
            throw new ErrorInputData("Error input data - Неверные входные данные. ВВЕДЕН НЕВЕРНЫЙ КОД", confirmOperation.getOperationId());
        }
        log.error("ОШИБКА сервера, операция " + confirmOperation.getOperationId() + " не подтверждена");
        throw new ErrorConfirmation("Error confirmation - ошибка сервера, операция не подтверждена", confirmOperation.getOperationId());
    }

    public int cntId() { // метод для корректной работы фронта
        return repository.getCntId();
    }
}
