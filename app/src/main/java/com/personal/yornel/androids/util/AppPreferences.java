package com.personal.yornel.androids.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yornel on 23/9/2017.
 */

public class AppPreferences {

    public static final String IDENTITY = "identity";
    public static final String PREF_FILE_NAME = "app-smartphones-v2";
    private static final String USER = "user";
    private static final String RECORD = "record";

    Context context;
    SharedPreferences preferences;

    public AppPreferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
    }

    public void saveRecord(String search) {

        ArrayList<String> records = getRecord();

        if (records == null)
            records = new ArrayList<>();
        else
            deleteFromArray(records, search);

        records.add(search);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(RECORD, new Gson().toJson(records));
        editor.commit();
    }

    public ArrayList<String> getRecord() {
        String recordString = preferences.getString(RECORD, null);
        if (recordString == null) {
            return null;
        } else {
            ArrayList<String> positions = new Gson()
                    .fromJson(recordString, new TypeToken<ArrayList<String>>() {}.getType());
            return positions;
        }
    }

    public String[] getRecordArray() {
        String recordString = preferences.getString(RECORD, null);
        if (recordString == null) {
            return null;
        } else {
            ArrayList<String> positions = new Gson()
                    .fromJson(recordString, new TypeToken<ArrayList<String>>() {}.getType());
            Collections.reverse(positions);
            return positions.toArray(new String[0]);
        }
    }

    public void deleteFromArray(List<String> array, String key) {

        List<String> listDelete = new ArrayList<>();
        for (String toDelete: array) {
            if (toDelete.trim().equalsIgnoreCase(key.trim())) {
                listDelete.add(toDelete);
            }
        }
        array.removeAll(listDelete);
    }

}
