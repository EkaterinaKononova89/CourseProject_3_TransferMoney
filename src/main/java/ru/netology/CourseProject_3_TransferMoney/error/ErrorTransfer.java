package ru.netology.CourseProject_3_TransferMoney.error;

public class ErrorTransfer extends RuntimeException {
    private String msg;
    private int id;


    public ErrorTransfer(String msg, String operationId) {
        this.msg = msg;
        this.id = Integer.parseInt(operationId);
    }

    @Override
    public String getMessage() {
        return msg + ", id операции " + id;
    }
}
