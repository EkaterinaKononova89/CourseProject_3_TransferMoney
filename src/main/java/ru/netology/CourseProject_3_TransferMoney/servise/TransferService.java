package ru.netology.CourseProject_3_TransferMoney.servise;

import org.springframework.stereotype.Service;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;
import ru.netology.CourseProject_3_TransferMoney.repository.TransferRepository;

import java.io.IOException;


@Service
public class TransferService {
    TransferRepository repository;

    public TransferService(TransferRepository repository) {
        this.repository = repository;
    }

    public String transferMoney(DataForm dataForm) throws IOException {
        return repository.transferMoney(dataForm.divideBy100());
    }

    public String confirmOperation(ConfirmOperation confirmOperation) throws IOException {
        return repository.confirmOperation(confirmOperation.getOperationId(), confirmOperation.getCode());
    }

    public int cntId() { // метод для корректной работы фронта
        return repository.getCntId();
    }
}
