package com.tadneit.sale.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author tadneit
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@NoArgsConstructor
public class UnAuthorizationException extends Exception {

    public UnAuthorizationException(String mgs, Throwable throwable) {
        super(mgs, throwable);
    }

    public UnAuthorizationException(String mgs) {
        super(mgs);
    }
}
