package com.tadneit.sale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author tadneit
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusinessException extends Exception {

    public BusinessException(String mgs, Throwable throwable) {
        super(mgs, throwable);
    }

    public BusinessException(String mgs) {
        super(mgs);
    }
}
