package org.dessert.moah.common.exception;

import org.dessert.moah.item.entity.Stock;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, StaleObjectStateException.class})
    public ResponseEntity<String> handleOptimisticLockingFailure(Exception ex) {
        log.error("Optimistic locking failure occurred: ", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("There was a conflict with another transaction. Please try again.");
    }
}

