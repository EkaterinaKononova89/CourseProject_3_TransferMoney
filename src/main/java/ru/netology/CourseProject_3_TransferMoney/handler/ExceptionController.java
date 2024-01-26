package ru.netology.CourseProject_3_TransferMoney.handler;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorConfirmation;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;
import ru.netology.CourseProject_3_TransferMoney.logger.Logger;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class ExceptionController {
    private final Logger logger;

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<ErrorInputData> eidHandler(ErrorInputData ex) {
        try {
                logger.log(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransfer.class)
    public ResponseEntity<ErrorTransfer> etHandler(ErrorTransfer ex) {
        try {
                logger.log(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorConfirmation.class)
    public ResponseEntity<ErrorConfirmation> ecHandler(ErrorConfirmation ex) {
        try {
                logger.log(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
