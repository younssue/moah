package org.dessert.moah.common.exception;


import org.dessert.moah.common.type.ErrorCode;

public class NotFoundException extends RuntimeException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
