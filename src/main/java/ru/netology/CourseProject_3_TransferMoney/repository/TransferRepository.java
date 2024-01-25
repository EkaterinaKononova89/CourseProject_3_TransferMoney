package ru.netology.CourseProject_3_TransferMoney.repository;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import ru.netology.CourseProject_3_TransferMoney.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Getter
public class TransferRepository implements RepositoryInterface {
    private final List<Card> listDemo = new ArrayList<>(Arrays.asList( // List для удобства добавления
            new Card("1234567890123456", "01/25", "123", new Amount(100_000, "RUR")),
            new Card("2345678901234567", "02/25", "234", new Amount(150_000, "RUR")),
            new Card("3456789012345678", "03/25", "345", new Amount(200_000, "RUR"))
    ));
    private final ConcurrentHashMap<String, Card> listOfCardDemo = new ConcurrentHashMap<>(); // перевела карты в мапу

    {
        listOfCardDemo.put(listDemo.get(0).getNumber(), listDemo.get(0));
        listOfCardDemo.put(listDemo.get(1).getNumber(), listDemo.get(1));
        listOfCardDemo.put(listDemo.get(2).getNumber(), listDemo.get(2));
    }

    private final AtomicInteger cntId = new AtomicInteger(0);
    private final HashMap<String, String> mapConfirmOperation = new HashMap<>(); // не потокобезопасная, т.к. нет необходимости,
    // все операции будут иметь уникальные номера, а после подтверждения операции можно чистить эту мапу от подтвержденного эл-та
    private final HashMap<String, DataForm> mapOperationID = new HashMap<>(); // связывает ID с DataForm
    private final HashMap<String, OperationId> operationIdAndConfirmOperation = new HashMap<>(); // связывает ConfirmOperation с OperationId

    public int getCntId() {
        return cntId.get();
    }

    public OperationId createNewOperationId(DataForm dataForm) {
        OperationId operationId = new OperationId();
        operationId.setOperationId(cntId.incrementAndGet());
        mapOperationID.put(operationId.getOperationId(), dataForm);
        return operationId;
    }

    public OperationId createConfirmOperation(OperationId operationId) {
        ConfirmOperation confirmOperation = new ConfirmOperation(operationId);
        mapConfirmOperation.put(confirmOperation.getOperationId(), confirmOperation.getCode());
        operationIdAndConfirmOperation.put(confirmOperation.getOperationId(), operationId);
        return operationId;
    }
}