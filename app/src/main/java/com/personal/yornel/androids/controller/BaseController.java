package com.personal.yornel.androids.controller;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.personal.yornel.androids.util.AppPreferences;
import com.squareup.otto.Bus;

/**
 * Created by Yornel on 23/9/2017.
 */

public class BaseController {

    protected AppPreferences preferences;

    protected Bus bus;

    protected Context context;

    protected FirebaseDatabase database;

    protected DatabaseReference smartphoneRef;

    BaseController(Context context, Bus bus) {
        this.preferences = new AppPreferences(context);
        this.context = context;
        this.bus = bus;
        this.database = FirebaseDatabase.getInstance();
        this.smartphoneRef = database.getReference("smartphone");
    }
}
