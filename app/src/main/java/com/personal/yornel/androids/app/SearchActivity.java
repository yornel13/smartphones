package com.personal.yornel.androids.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.personal.yornel.androids.R;
import com.personal.yornel.androids.adapter.RecyclerClickListener;
import com.personal.yornel.androids.adapter.SearchAdapter;
import com.personal.yornel.androids.controller.SearchController;
import com.personal.yornel.androids.event.LoadPhoneCompleteEvent;
import com.personal.yornel.androids.event.LoadPhoneFailureEvent;
import com.personal.yornel.androids.event.SearchPhoneCompleteEvent;
import com.personal.yornel.androids.event.SearchPhoneFailureEvent;
import com.personal.yornel.androids.model.Battery;
import com.personal.yornel.androids.model.Camera;
import com.personal.yornel.androids.model.Connectivity;
import com.personal.yornel.androids.model.GoogleResult;
import com.personal.yornel.androids.model.Memory;
import com.personal.yornel.androids.model.Phone;
import com.personal.yornel.androids.model.Processor;
import com.personal.yornel.androids.model.Screen;
import com.personal.yornel.androids.model.Smartphone;
import com.squareup.otto.Subscribe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements RecyclerClickListener.OnItemClickListener {

    private MaterialSearchView searchView;
    private MenuItem searchMenu;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SearchAdapter searchAdapter;
    private static View transitionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        setUpToolbarWithTitle(true);

        loadSearchView();

        loadAdapter();
    }

    private void loadAdapter() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        searchAdapter = new SearchAdapter(this, new ArrayList<GoogleResult>());
        mRecyclerView.setAdapter(searchAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, this));

        progressBar = findViewById(R.id.container_progress_bar);
    }

    private void loadSearchView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setEllipsize(false);
        searchView.setSuggestions(preferences.getRecordArray());
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.trim();
                progressBar.setVisibility(View.VISIBLE);
                searchController.search(query);
                getSupportActionBar().setTitle(query);
                preferences.saveRecord(query);
                searchView.setSuggestions(preferences.getRecordArray());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals(getSupportActionBar().getTitle().toString())) {
                    searchAdapter.replaceAll(new ArrayList<GoogleResult>());
                }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                if (getSupportActionBar().getTitle() != null
                        && !getSupportActionBar().getTitle().toString().isEmpty()) {
                    searchView.setQuery(getSupportActionBar().getTitle().toString(), false);
                }
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search, menu);
        searchMenu = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchMenu);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                searchView.showSearch();
            }
        }, 1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:

                //Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Subscribe
    public void onSearchComplete(SearchPhoneCompleteEvent event){
        progressBar.setVisibility(View.GONE);
        for (GoogleResult googleResult: event.getResponse())
            System.out.println(googleResult.getTitle()+" en "+googleResult.getUrl());
        searchAdapter.replaceAll(event.getResponse());
    }

    @Subscribe
    public void onSearchFailure(SearchPhoneFailureEvent event){
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, event.getResponse(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        GoogleResult googleResult = searchAdapter.getItem(position);
        progressDialog = ProgressDialog.show(this, null,
                getString(R.string.loading_information), true);
        searchController.loadPhone(googleResult.getUrl());
        transitionView = view;
    }

    @Subscribe
    public void onLoadComplete(LoadPhoneCompleteEvent event){
        progressDialog.dismiss();
        System.out.println(new Gson().toJson(event.getResponse()));
        Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
        intent.putExtra(Smartphone.class.getName(), new Gson().toJson(event.getResponse()));
        Pair<View, String> pTitle = Pair.create(transitionView.findViewById(R.id.title), getString(R.string.transition_name_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pTitle);
        ActivityCompat.startActivity(SearchActivity.this, intent, options.toBundle());
    }

    @Subscribe
    public void onLoadFailure(LoadPhoneFailureEvent event){
        progressDialog.dismiss();
        Toast.makeText(this, event.getResponse(), Toast.LENGTH_SHORT).show();
    }
}
