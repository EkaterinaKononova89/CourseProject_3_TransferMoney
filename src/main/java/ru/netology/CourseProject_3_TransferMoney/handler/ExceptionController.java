package ru.netology.CourseProject_3_TransferMoney.handler;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorConfirmation;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorInputData;
import ru.netology.CourseProject_3_TransferMoney.error.ErrorTransfer;

@Controller
@CommonsLog
public class ExceptionController {

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<ErrorInputData> eidHandler(ErrorInputData ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransfer.class)
    public ResponseEntity<ErrorTransfer> etHandler(ErrorTransfer ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorConfirmation.class)
    public ResponseEntity<ErrorConfirmation> ecHandler(ErrorConfirmation ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
