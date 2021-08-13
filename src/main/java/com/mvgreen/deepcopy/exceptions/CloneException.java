package com.mvgreen.deepcopy.exceptions;

public class CloneException extends RuntimeException {

    public CloneException(Exception cause) {
        super(cause);
    }

    public CloneException(String message) {
        super(message);
    }

}
