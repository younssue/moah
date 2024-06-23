package org.dessert.moah.exception;


import org.dessert.moah.base.type.ErrorCode;

public class BadRequestException extends RuntimeException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
