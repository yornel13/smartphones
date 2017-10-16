package com.personal.yornel.androids.util;

import com.squareup.otto.Bus;

/**
 * Created by Yornel on 10/9/2017.
 */
public class GlobalBus {
    private static Bus sBus;
    public static Bus getBus() {
        if (sBus == null)
            sBus = new Bus();
        return sBus;
    }
}

