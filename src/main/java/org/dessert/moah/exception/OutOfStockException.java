package org.dessert.moah.exception;

import org.dessert.moah.base.type.ErrorCode;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(ErrorCode errorCode){
        super(errorCode.getDescription());
    }
}
