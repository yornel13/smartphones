package com.personal.yornel.androids.event;

/**
 * Created by Yornel on 23/9/2017.
 */

public class SearchPhoneFailureEvent {

    private String response;

    public SearchPhoneFailureEvent(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
