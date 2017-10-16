package com.personal.yornel.androids.controller;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.personal.yornel.androids.event.DataBaseErrorEvent;
import com.personal.yornel.androids.event.GetTop1CompleteEvent;
import com.personal.yornel.androids.event.GetTop2CompleteEvent;
import com.personal.yornel.androids.event.GetTop3CompleteEvent;
import com.personal.yornel.androids.event.GetTop4CompleteEvent;
import com.personal.yornel.androids.event.SavePhoneCompleteEvent;
import com.personal.yornel.androids.model.Smartphone;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yornel on 27/9/2017.
 */

public class SmartphoneController extends BaseController {

    private SmartphoneController(Context context, Bus bus) {
        super(context, bus);
    }

    public static SmartphoneController createSmartphoneController(Context context, Bus bus) {
        return new SmartphoneController(context, bus);
    }

    public void save(final Smartphone smartphone) {
        String id = smartphoneRef.push().getKey();
        smartphoneRef.child(id).setValue(smartphone, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                    bus.post(new DataBaseErrorEvent());
                } else {
                    bus.post(new SavePhoneCompleteEvent(smartphone));
                }
            }
        });
    }

    public void getLisTop1() {
        smartphoneRef.orderByChild("top1").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Smartphone> smartphones = new ArrayList<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Smartphone smartphone = childSnapshot.getValue(Smartphone.class);
                    smartphones.add(smartphone);
                }
                bus.post(new GetTop1CompleteEvent(smartphones));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                bus.post(new DataBaseErrorEvent());
            }
        });
    }

    public void getLisTop2() {
        smartphoneRef.orderByChild("top2").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Smartphone> smartphones = new ArrayList<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Smartphone smartphone = childSnapshot.getValue(Smartphone.class);
                    smartphones.add(smartphone);
                }
                bus.post(new GetTop2CompleteEvent(smartphones));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                bus.post(new DataBaseErrorEvent());
            }
        });
    }

    public void getLisTop3() {
        smartphoneRef.orderByChild("top3").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Smartphone> smartphones = new ArrayList<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Smartphone smartphone = childSnapshot.getValue(Smartphone.class);
                    smartphones.add(smartphone);
                }
                bus.post(new GetTop3CompleteEvent(smartphones));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                bus.post(new DataBaseErrorEvent());
            }
        });
    }

    public void getLisTop4() {
        smartphoneRef.orderByChild("top4").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Smartphone> smartphones = new ArrayList<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Smartphone smartphone = childSnapshot.getValue(Smartphone.class);
                    smartphones.add(smartphone);
                }
                bus.post(new GetTop4CompleteEvent(smartphones));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                bus.post(new DataBaseErrorEvent());
            }
        });
    }
}
