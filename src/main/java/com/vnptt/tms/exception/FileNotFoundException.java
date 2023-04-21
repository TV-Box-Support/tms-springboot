package com.vnptt.tms.exception;

/**
 * Returns in case the file cannot be found in the data server
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }

}
