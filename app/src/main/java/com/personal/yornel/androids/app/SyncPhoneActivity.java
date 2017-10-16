package com.personal.yornel.androids.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;
import com.personal.yornel.androids.R;
import com.personal.yornel.androids.event.DataBaseErrorEvent;
import com.personal.yornel.androids.event.SavePhoneCompleteEvent;
import com.personal.yornel.androids.model.Smartphone;
import com.squareup.otto.Subscribe;

public class SyncPhoneActivity extends BaseActivity {

    private Smartphone smartphone;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_phone);
        getSmartphoneFromIntent();
        setToolbarTitle(smartphone.getTitle());
        setUpToolbarWithTitle(true);

        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkbox4);
    }

    private void getSmartphoneFromIntent() {
        String json = getIntent().getExtras().getString(Smartphone.class.getName());
        smartphone = new Gson().fromJson(json, Smartphone.class);
        if (smartphone == null) {
            finish();
        }
    }

    public void save(View view) {

        smartphone.setTop1(checkBox1.isChecked()?1:0);
        smartphone.setTop2(checkBox2.isChecked()?1:0);
        smartphone.setTop3(checkBox3.isChecked()?1:0);
        smartphone.setTop4(checkBox4.isChecked()?1:0);

        progressDialog = ProgressDialog.show(this, null,getString(R.string.saving_information), true);
        smartphoneController.save(smartphone);
    }

    @Subscribe
    public void onSaveComplete(SavePhoneCompleteEvent event){
        progressDialog.dismiss();
        Toast.makeText(this, "Smartphone guardado", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Subscribe
    public void onDateBaseError(DataBaseErrorEvent event){
        progressDialog.dismiss();
        Toast.makeText(this, "Error de base de datos", Toast.LENGTH_SHORT).show();
    }
}
