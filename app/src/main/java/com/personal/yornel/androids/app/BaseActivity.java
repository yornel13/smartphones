package com.personal.yornel.androids.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.personal.yornel.androids.R;
import com.personal.yornel.androids.controller.SearchController;
import com.personal.yornel.androids.controller.SmartphoneController;
import com.personal.yornel.androids.util.AppPreferences;
import com.personal.yornel.androids.util.GlobalBus;
import com.squareup.otto.Bus;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Yornel on 07-jul-16.
 */
public class BaseActivity extends AppCompatActivity {

    protected Bus bus = GlobalBus.getBus();
    protected View progressBar;
    protected ProgressDialog progressDialog;
    protected AppPreferences preferences;
    protected SearchController searchController;
    protected SmartphoneController smartphoneController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new AppPreferences(this);
        searchController = SearchController.createSearchController(this, bus);
        smartphoneController = SmartphoneController.createSmartphoneController(this, bus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.getClass().getSimpleName(),
                "onResume: " + this.getClass().getSimpleName());
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(this.getClass().getSimpleName(),
                "onPause: " + this.getClass().getSimpleName());
        bus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    protected void setUpToolbarWithTitle(boolean hasBackButton){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(hasBackButton);
            getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected Toolbar setToolbarTitle(String titleString) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText(titleString);
            Typeface tf = Typeface.createFromAsset(getApplicationContext()
                    .getAssets(), "fonts/OpenSans-Regular.ttf");
            title.setTypeface(tf);
        }
        return toolbar;
    }

    protected Toolbar setToolbarSubTitle(String titleString) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        title.setText(titleString);
        Typeface tf = Typeface.createFromAsset(getApplicationContext()
                .getAssets(),"fonts/OpenSans-Regular.ttf");
        title.setTypeface(tf);
        return toolbar;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
