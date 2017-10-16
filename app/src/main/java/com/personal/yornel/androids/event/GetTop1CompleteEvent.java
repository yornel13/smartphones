package com.personal.yornel.androids.event;

import com.personal.yornel.androids.model.Smartphone;

import java.util.List;

/**
 * Created by Yornel on 24/9/2017.
 */

public class GetTop1CompleteEvent {


    private List<Smartphone> response;

    public GetTop1CompleteEvent(List<Smartphone> response) {
        this.response = response;
    }

    public List<Smartphone> getResponse() {
        return response;
    }
}
