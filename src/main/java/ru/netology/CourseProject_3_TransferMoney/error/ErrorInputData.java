package ru.netology.CourseProject_3_TransferMoney.error;

public class ErrorInputData extends RuntimeException {
    private final String msg;
    private final int id;


    public ErrorInputData(String msg, String operationId) {
        this.msg = msg;
        this.id = Integer.parseInt(operationId);
    }

    @Override
    public String getMessage() {
        return msg + ", id операции " + id;
    }
}
