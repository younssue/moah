package org.dessert.moah.common.exception;


import org.dessert.moah.common.type.ErrorCode;

public class BadRequestException extends RuntimeException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
