package com.example.toponym.exception;

import com.example.toponym.utils.R;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-13
 */
@Data
public class ServiceException extends RuntimeException {

    private R data;

    public ServiceException(String msg) {
        this(R.fail(msg));
    }

    public ServiceException(R r) {
        this.data = r;
    }
}
