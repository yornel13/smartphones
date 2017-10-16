package com.personal.yornel.androids.event;

import com.personal.yornel.androids.model.Smartphone;

/**
 * Created by Yornel on 24/9/2017.
 */

public class LoadPhoneCompleteEvent {


    private Smartphone response;

    public LoadPhoneCompleteEvent(Smartphone response) {
        this.response = response;
    }

    public Smartphone getResponse() {
        return response;
    }
}
