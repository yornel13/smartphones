package com.personal.yornel.androids.event;

import com.personal.yornel.androids.model.GoogleResult;

import java.util.List;

/**
 * Created by Yornel on 23/9/2017.
 */

public class SearchPhoneCompleteEvent {

    private List<GoogleResult> response;

    public SearchPhoneCompleteEvent(List<GoogleResult> response) {
        this.response = response;
    }

    public List<GoogleResult> getResponse() {
        return response;
    }
}
