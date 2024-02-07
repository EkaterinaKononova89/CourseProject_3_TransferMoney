package ru.netology.CourseProject_3_TransferMoney.error;

public class ErrorConfirmation extends RuntimeException {
    private final String msg;
    private final int id;

    // автогенерация конструктора не подойдет, т.к. поле id меняет тип
    public ErrorConfirmation(String msg, String operationId) {
        this.msg = msg;
        this.id = Integer.parseInt(operationId);
    }

    @Override
    public String getMessage() {
        return msg + ", id операции " + id;
    }
}
