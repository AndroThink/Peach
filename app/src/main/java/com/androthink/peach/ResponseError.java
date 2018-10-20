package com.androthink.peach;

public class ResponseError {
    private int responseCode;
    private String errorMessage;
    private boolean networkError;
    private boolean timeoutError;

    public ResponseError(){}

    ResponseError(int responseCode, String errorMessage, boolean networkError, boolean timeoutError) {
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
        this.networkError = networkError;
        this.timeoutError = timeoutError;
    }

    public int getResponseCode() {
        return responseCode;
    }

    void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isNetworkError() {
        return networkError;
    }

    void setNetworkError(boolean networkError) {
        this.networkError = networkError;
    }

    public boolean isTimeoutError() {
        return timeoutError;
    }

    void setTimeoutError(boolean timeoutError) {
        this.timeoutError = timeoutError;
    }
}
