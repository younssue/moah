package org.dessert.moah.exception;


import org.dessert.moah.base.type.ErrorCode;

public class NotFoundException extends RuntimeException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
