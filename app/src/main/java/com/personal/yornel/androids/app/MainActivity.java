package com.personal.yornel.androids.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.personal.yornel.androids.R;
import com.personal.yornel.androids.adapter.MainAdapter;
import com.personal.yornel.androids.adapter.RecyclerClickListener;
import com.personal.yornel.androids.event.GetTop1CompleteEvent;
import com.personal.yornel.androids.event.GetTop2CompleteEvent;
import com.personal.yornel.androids.event.GetTop3CompleteEvent;
import com.personal.yornel.androids.event.GetTop4CompleteEvent;
import com.personal.yornel.androids.event.LoadPhoneCompleteEvent;
import com.personal.yornel.androids.event.LoadPhoneFailureEvent;
import com.personal.yornel.androids.model.Smartphone;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import de.mrapp.android.dialog.MaterialDialog;

public class MainActivity extends BaseActivity implements RecyclerClickListener.OnItemClickListener,
        Drawer.OnDrawerItemClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static long ITEM_DRAWER_MORE_SEARCH = 0;
    private static long ITEM_DRAWER_QUALITY_PRICE = 1;
    private static long ITEM_DRAWER_TOP_PHONE = 2;
    private static long ITEM_DRAWER_RATING = 3;
    private static long ITEM_DRAWER_SEARCH = 4;
    private static long ITEM_DRAWER_LOGIN = 5;
    private static long ITEM_DRAWER_EXIT = 6;

    private GoogleApiClient googleApiClient;
    private SignInButton googleSignInButton;
    private static final int SIGN_IN_CODE = 777;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MainAdapter mainAdapter;
    private static View transitionView;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = setToolbarTitle("Smartphones");

        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mainAdapter = new MainAdapter(this, new ArrayList<Smartphone>());
        mRecyclerView.setAdapter(mainAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, this));

        progressBar = findViewById(R.id.container_progress_bar);

        loadDrawer(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            hadleSignInResult(result);
        }
    }

    private void hadleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()) {
            goMainScreen();
            PrimaryDrawerItem itemLogin = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_LOGIN).withName("Salir").withSelectable(false);
            itemLogin.withIcon(GoogleMaterial.Icon.gmd_perm_identity);
        }else{
            Toast.makeText(this, "No se pudo acceder", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void loadDrawer(Toolbar toolbar) {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.smartphones_splash_invert_blurry)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_MORE_SEARCH).withName("Mas Buscados");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_QUALITY_PRICE).withName("Calidad Precio");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_TOP_PHONE).withName("Topes de Gama");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_RATING).withName("Raiting");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_SEARCH).withName("Buscar");
        PrimaryDrawerItem itemLogin = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_LOGIN).withName("Ingresar").withSelectable(false);
        PrimaryDrawerItem itemExit = new PrimaryDrawerItem().withIdentifier(ITEM_DRAWER_EXIT).withName("Salir").withSelectable(false);
        item1.withIcon(GoogleMaterial.Icon.gmd_history).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf"));
        item2.withIcon(FoundationIcons.Icon.fou_like).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf"));
        item3.withIcon(GoogleMaterial.Icon.gmd_high_quality).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf"));
        item4.withIcon(GoogleMaterial.Icon.gmd_star_border).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf"));
        item5.withIcon(GoogleMaterial.Icon.gmd_search).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf"));
        itemLogin.withIcon(GoogleMaterial.Icon.gmd_perm_identity);
        itemExit.withIcon(GoogleMaterial.Icon.gmd_exit_to_app);

        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addStickyDrawerItems(itemLogin)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Top").withSelectable(false)
                                .withEnabled(false).withTypeface(Typeface
                                .createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf")),
                        item1,
                        item2,
                        item3,
                        item4,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("  Buscar").withSelectable(false)
                                .withEnabled(false).withTypeface(Typeface
                                .createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf")),
                        item5.withSelectable(false)

                )
                .withOnDrawerItemClickListener(this).build();
        drawer.setSelection(item1);
    }

    public void getTop1() {
        progressBar.setVisibility(View.VISIBLE);
        mainAdapter.replaceAll(new ArrayList<Smartphone>());
        smartphoneController.getLisTop1();
    }

    public void getTop2() {
        progressBar.setVisibility(View.VISIBLE);
        mainAdapter.replaceAll(new ArrayList<Smartphone>());
        smartphoneController.getLisTop2();
    }

    public void getTop3() {
        progressBar.setVisibility(View.VISIBLE);
        mainAdapter.replaceAll(new ArrayList<Smartphone>());
        smartphoneController.getLisTop3();
    }

    public void getTop4() {
        progressBar.setVisibility(View.VISIBLE);
        mainAdapter.replaceAll(new ArrayList<Smartphone>());
        smartphoneController.getLisTop4();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Smartphone smartphone = mainAdapter.getItem(position);
        progressDialog = ProgressDialog.show(this, null,
                getString(R.string.loading_information), true);
        transitionView = view;
        onLoadComplete(new LoadPhoneCompleteEvent(smartphone));
        /*searchController.loadPhone(smartphone.getUrl());*/
    }

    @Subscribe
    public void onLoadComplete(LoadPhoneCompleteEvent event){
        progressDialog.dismiss();
        System.out.println(new Gson().toJson(event.getResponse()));
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(Smartphone.class.getName(), new Gson().toJson(event.getResponse()));
        Pair<View, String> pPhone = Pair.create(transitionView.findViewById(R.id.smartphone_image),  getString(R.string.transition_name_phone));
        Pair<View, String> pTitle = Pair.create(transitionView.findViewById(R.id.title_layout), getString(R.string.transition_name_name));
        Pair<View, String> pProcessor = Pair.create(transitionView.findViewById(R.id.processor), getString(R.string.transition_name_name));
        Pair<View, String> pRam = Pair.create(transitionView.findViewById(R.id.ram), getString(R.string.transition_name_name));
        Pair<View, String> pInches = Pair.create(transitionView.findViewById(R.id.inches), getString(R.string.transition_name_name));
        Pair<View, String> pVersion = Pair.create(transitionView.findViewById(R.id.version), getString(R.string.transition_name_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, pPhone, pTitle, pProcessor, pRam, pInches, pVersion);
        ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
    }

    @Subscribe
    public void onLoadFailure(LoadPhoneFailureEvent event){
        progressDialog.dismiss();
        Toast.makeText(this, event.getResponse(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if (drawerItem.getIdentifier() == ITEM_DRAWER_SEARCH) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        } else if (drawerItem.getIdentifier() == ITEM_DRAWER_MORE_SEARCH) {
            setToolbarSubTitle("Mas buscados");
            getTop1();
        } else if (drawerItem.getIdentifier() == ITEM_DRAWER_QUALITY_PRICE) {
            setToolbarSubTitle("Calidad-precio");
            getTop2();
        } else if (drawerItem.getIdentifier() == ITEM_DRAWER_TOP_PHONE) {
            setToolbarSubTitle("Topes de gama");
            getTop3();
        } else if (drawerItem.getIdentifier() == ITEM_DRAWER_RATING) {
            setToolbarSubTitle("Mas votados");
            getTop4();
        } else if (drawerItem.getIdentifier() == ITEM_DRAWER_LOGIN) {
            showDialogComing();
        }
        return false;
    }

    private void showDialogComing() {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this);


        //googleSignInButton = (SignInButton) findViewById(R.id.google_signIn_button);
        //dialogBuilder.setTitle("Ops lo sentimos!");
        //dialogBuilder.setMessage("Esta funcionalidad aun no esta implementa, esperala muy pronto.");
        dialogBuilder.showHeader(true);
        dialogBuilder.setView(R.layout.sign_in_buttons);
        dialogBuilder.setHeaderBackground(R.drawable.smartphones_splash);

        //dialogBuilder.setHeaderIcon(R.drawable.ic_suggestion);
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        MaterialDialog dialog = dialogBuilder.create();
        dialog.show();
        SignInButton signInButton = (SignInButton) dialog.findViewById(R.id.google_signIn_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hola");
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });
    }

    @Subscribe
    public void onGetTop1Complete(GetTop1CompleteEvent event){
        if (drawer.getCurrentSelection() == ITEM_DRAWER_MORE_SEARCH) {
            progressBar.setVisibility(View.GONE);
            mainAdapter.replaceAll(event.getResponse());
        }
    }

    @Subscribe
    public void onGetTop2Complete(GetTop2CompleteEvent event){
        if (drawer.getCurrentSelection() == ITEM_DRAWER_QUALITY_PRICE) {
            progressBar.setVisibility(View.GONE);
            mainAdapter.replaceAll(event.getResponse());
        }
    }

    @Subscribe
    public void onGetTop3Complete(GetTop3CompleteEvent event){
        if (drawer.getCurrentSelection() == ITEM_DRAWER_TOP_PHONE) {
            progressBar.setVisibility(View.GONE);
            mainAdapter.replaceAll(event.getResponse());
        }
    }

    @Subscribe
    public void onGetTop4Complete(GetTop4CompleteEvent event){
        if (drawer.getCurrentSelection() == ITEM_DRAWER_RATING) {
            progressBar.setVisibility(View.GONE);
            mainAdapter.replaceAll(event.getResponse());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
