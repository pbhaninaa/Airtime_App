package com.example.qupos.JavaClasses;

public class PayNowResponse {
    private String redirectUrl;
    private boolean isRequestSuccess;

    public PayNowResponse(String redirectUrl, boolean isRequestSuccess) {
        this.redirectUrl = redirectUrl;
        this.isRequestSuccess = isRequestSuccess;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public boolean isRequestSuccess() {
        return isRequestSuccess;
    }
}

