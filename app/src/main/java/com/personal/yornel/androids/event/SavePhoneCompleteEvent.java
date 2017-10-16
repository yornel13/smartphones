package com.personal.yornel.androids.event;

import com.personal.yornel.androids.model.Smartphone;

/**
 * Created by Yornel on 27/9/2017.
 */

public class SavePhoneCompleteEvent {

    private Smartphone response;

    public SavePhoneCompleteEvent(Smartphone response) {
        this.response = response;
    }

    public Smartphone getResponse() {
        return response;
    }
}
