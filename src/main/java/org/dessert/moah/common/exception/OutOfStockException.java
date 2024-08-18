package org.dessert.moah.common.exception;

import org.dessert.moah.common.type.ErrorCode;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(ErrorCode errorCode){
        super(errorCode.getDescription());
    }
}
