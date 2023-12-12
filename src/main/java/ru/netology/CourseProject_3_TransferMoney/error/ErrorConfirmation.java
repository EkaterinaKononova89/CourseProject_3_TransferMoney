package ru.netology.CourseProject_3_TransferMoney.error;

public class ErrorConfirmation extends RuntimeException {
    private String msg;
    private int id;


    public ErrorConfirmation(String msg, String operationId) {
        this.msg = msg;
        this.id = Integer.parseInt(operationId);
    }

    @Override
    public String getMessage() {
        return msg + ", id операции " + id;
    }
}
