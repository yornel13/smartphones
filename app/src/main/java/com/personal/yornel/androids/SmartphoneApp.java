package com.personal.yornel.androids;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Yornel on 25/9/2017.
 */

public class SmartphoneApp extends Application {

    public static SmartphoneApp instance = null;

    public static Context getInstance() {
        if (null == instance) {
            instance = new SmartphoneApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
