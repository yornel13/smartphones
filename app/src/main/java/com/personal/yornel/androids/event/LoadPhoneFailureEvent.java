package com.personal.yornel.androids.event;

/**
 * Created by Yornel on 24/9/2017.
 */

public class LoadPhoneFailureEvent {

    private String response;

    public LoadPhoneFailureEvent(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
