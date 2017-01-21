package com.testography.amgradle.data.network.error;

public class ApiError extends Throwable {
    private int statusCode;
    private String message;

    @Override
    public String getMessage() {
        return message + " statusCode: " + statusCode;
    }
}
