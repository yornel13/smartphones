package com.personal.yornel.androids.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.personal.yornel.androids.R;
import com.personal.yornel.androids.adapter.OtherAdapter;
import com.personal.yornel.androids.adapter.RecyclerClickListener;
import com.personal.yornel.androids.event.LoadPhoneCompleteEvent;
import com.personal.yornel.androids.event.LoadPhoneFailureEvent;
import com.personal.yornel.androids.model.Smartphone;
import com.personal.yornel.androids.model.Version;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.regex.Pattern;

import de.mrapp.android.dialog.WizardDialog;

import static com.personal.yornel.androids.util.AppUtil.convertDpToPixel;
import static com.personal.yornel.androids.util.AppUtil.convertPixelsToDp;
import static com.personal.yornel.androids.util.AppUtil.getNavigationBarSize;
import static com.personal.yornel.androids.util.AppUtil.getStatusBarHeight;

public class DetailsActivity extends BaseActivity implements RecyclerClickListener.OnItemClickListener {

    private Smartphone smartphone;
    private ImageView smartphoneImage;
    private FloatingActionMenu fabMenu;
    private StickyScrollView stickyScrollView;

    private TextView titleScreen;
    private TextView titleContentScreen;
    private TextView titleRam;
    private TextView titleContentRam;
    private TextView titleProcessor;
    private TextView titleContentProcessor;
    private TextView titleVersion;
    private TextView titleContentVersion;

    private TextView screenSizeText;
    private TextView screenResolutionText;
    private TextView screenDensityText;
    private TextView screenRatioText;
    private TextView screenTypeText;

    private TextView memoryRamText;
    private TextView memoryRomText;
    private TextView memoryExpandableText;

    private TextView processorModelText;
    private TextView processorCpuText;
    private TextView processorTypeText;
    private TextView processorFrequencyText;
    private TextView processorArchitectureText;
    private TextView processorGpu;

    private TextView batterySizeText;
    private TextView batteryTypeText;
    private TextView batteryOtherText;

    private TextView cameraResolutionText;
    private TextView cameraApertureText;
    private TextView cameraFlashText;
    private TextView cameraSensorText;
    private TextView cameraSelfieText;

    private TextView phoneBrandText;
    private TextView phoneSystemText;
    private TextView phoneDimensionsText;
    private TextView phoneWeightText;
    private TextView phoneMaterialsText;
    private TextView phoneColorsText;
    private TextView phoneFingerprintText;
    private TextView phoneJackText;
    private TextView phoneSimText;
    private TextView phoneLaunchText;

    private TextView connectivityLteText;
    private TextView connectivityHspaText;
    private TextView connectivityEdgeText;
    private TextView connectivityWifiText;
    private TextView connectivityBluetoothText;
    private TextView connectivityNavigationText;

    private TextView antutuText;

    private OtherAdapter adapter;
    private static View transitionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartphone_details);
        getSmartphoneFromIntent();
        setToolbarTitle(smartphone.getTitle());
        setUpToolbarWithTitle(true);

        fabMenu = (FloatingActionMenu) findViewById(R.id.menu_fab);
        setFabAction();

        stickyScrollView = (StickyScrollView) findViewById(R.id.scrollView);
        setOnScroll();

        setSmartphoneDetails();

        loadOthersVersions();

        setSpaceAboveScroll();
    }

    @Override
    public void onBackPressed() {
        if (fabMenu.isOpened()) {
            findViewById(R.id.background_fab_menu)
                    .setVisibility(View.GONE);
            fabMenu.toggle(true);
        } else {
            super.onBackPressed();
        }
    }

    private void setSmartphoneDetails() {

        smartphoneImage = (ImageView) findViewById(R.id.smartphone_image);
        titleScreen = (TextView) findViewById(R.id.title_screen);
        titleContentScreen = (TextView) findViewById(R.id.title_content_screen);
        titleRam = (TextView) findViewById(R.id.title_ram);
        titleContentRam = (TextView) findViewById(R.id.title_content_ram);
        titleProcessor = (TextView) findViewById(R.id.title_processor);
        titleContentProcessor = (TextView) findViewById(R.id.title_content_processor);
        titleVersion = (TextView) findViewById(R.id.title_version);
        titleContentVersion = (TextView) findViewById(R.id.title_content_version);

        Picasso.with(this)
                .load(smartphone.getImage())
                .error(R.drawable.google_nexus_512).into(smartphoneImage);

        titleScreen.setText(smartphone.getScreen().getSize()+" Pulgadas");
        titleContentScreen.setText(smartphone.getScreen().getResolution());
        titleRam.setText(smartphone.getMemory().getRam()+" Ram");
        String textTitleContentRam;
        try {
            String separator = Pattern.quote("(");
            textTitleContentRam = smartphone.getMemory().getRom().split(separator)[0]+" Almacenamiento";
        } catch (Exception e) {
            textTitleContentRam = smartphone.getMemory().getRom();
        }
        titleContentRam.setText(textTitleContentRam);
        titleProcessor.setText(smartphone.getProcessor().getModel());
        titleContentProcessor.setText("a "+smartphone.getProcessor().getFrequency());
        titleVersion.setText(smartphone.getPhone().getSystem());
        titleContentVersion.setText(smartphone.getPhone().getSystemUpdate());

        screenSizeText = (TextView) findViewById(R.id.screen_size);
        screenResolutionText = (TextView) findViewById(R.id.screen_resolution);
        screenDensityText = (TextView) findViewById(R.id.screen_density);
        screenRatioText = (TextView) findViewById(R.id.screen_ratio);
        screenTypeText = (TextView) findViewById(R.id.screen_type);

        memoryRamText = (TextView) findViewById(R.id.ram_size);
        memoryRomText = (TextView) findViewById(R.id.rom_size);
        memoryExpandableText = (TextView) findViewById(R.id.rom_expandable);

        processorModelText = (TextView) findViewById(R.id.cpu_model);
        processorCpuText = (TextView) findViewById(R.id.cpu);
        processorTypeText = (TextView) findViewById(R.id.cpu_type);
        processorFrequencyText = (TextView) findViewById(R.id.cpu_frequency);
        processorArchitectureText = (TextView) findViewById(R.id.cpu_architecture);
        processorGpu = (TextView) findViewById(R.id.gpu);

        batterySizeText = (TextView) findViewById(R.id.battery_size);
        batteryTypeText = (TextView) findViewById(R.id.battery_type);
        batteryOtherText = (TextView) findViewById(R.id.battery_other);

        cameraResolutionText = (TextView) findViewById(R.id.camera_principal_megapixels);
        cameraApertureText = (TextView) findViewById(R.id.camera_aperture);
        cameraFlashText = (TextView) findViewById(R.id.camera_flash);
        cameraSensorText = (TextView) findViewById(R.id.camera_brand);
        cameraSelfieText = (TextView) findViewById(R.id.camera_secondary_megapixels);

        phoneBrandText = (TextView) findViewById(R.id.phone_brand);
        phoneSystemText = (TextView) findViewById(R.id.phone_system);
        phoneDimensionsText = (TextView) findViewById(R.id.phone_dimensions);
        phoneWeightText = (TextView) findViewById(R.id.phone_weight);
        phoneMaterialsText = (TextView) findViewById(R.id.phone_materials);
        phoneColorsText = (TextView) findViewById(R.id.phone_colors);
        phoneFingerprintText = (TextView) findViewById(R.id.phone_fingerprint);
        phoneJackText = (TextView) findViewById(R.id.phone_jack);
        phoneSimText = (TextView) findViewById(R.id.phone_sim);
        phoneLaunchText = (TextView) findViewById(R.id.phone_launch);

        connectivityLteText = (TextView) findViewById(R.id.connectivity_lte);
        connectivityHspaText = (TextView) findViewById(R.id.connectivity_hsap);
        connectivityEdgeText = (TextView) findViewById(R.id.connectivity_gsm);
        connectivityWifiText = (TextView) findViewById(R.id.connectivity_wifi);
        connectivityBluetoothText = (TextView) findViewById(R.id.connectivity_bluetooth);
        connectivityNavigationText = (TextView) findViewById(R.id.connectivity_navigation);

        antutuText = (TextView) findViewById(R.id.antutu);

        screenSizeText.setText(smartphone.getScreen().getSize()+" pulgadas");
        screenResolutionText.setText(smartphone.getScreen().getResolution());
        screenDensityText.setText(smartphone.getScreen().getDensity());
        screenRatioText.setText(smartphone.getScreen().getRatio());
        screenTypeText.setText(smartphone.getScreen().getType());

        memoryRamText.setText(smartphone.getMemory().getRam());
        memoryRomText.setText(smartphone.getMemory().getRom());
        memoryExpandableText.setText(smartphone.getMemory().getExpandable());

        processorModelText.setText(smartphone.getProcessor().getModel());
        processorCpuText.setText(smartphone.getProcessor().getCpu());
        processorTypeText.setText(smartphone.getProcessor().getType());
        processorFrequencyText.setText(smartphone.getProcessor().getFrequency());
        if (smartphone.getProcessor().getArchitecture().equalsIgnoreCase("No")) {
            processorArchitectureText.setText("32 Bits");
        } else {
            processorArchitectureText.setText("64 Bits");
        }
        processorGpu.setText(smartphone.getProcessor().getGpu());

        batterySizeText.setText(smartphone.getBattery().getCapacity());
        batteryTypeText.setText(smartphone.getBattery().getType());
        batteryOtherText.setText(smartphone.getBattery().getOthers());

        cameraResolutionText.setText(smartphone.getCamera().getPrincipalResolution());
        cameraApertureText.setText(smartphone.getCamera().getPrincipalAperture());
        cameraFlashText.setText(smartphone.getCamera().getPrincipalFlash());
        cameraSensorText.setText(smartphone.getCamera().getPrincipalSensor());
        cameraSelfieText.setText(smartphone.getCamera().getSecondaryResolution());

        phoneBrandText.setText(smartphone.getPhone().getBrand());
        phoneSystemText.setText(smartphone.getPhone().getSystem());
        phoneDimensionsText.setText(smartphone.getPhone().getDimensions());
        phoneWeightText.setText(smartphone.getPhone().getWeight());
        phoneMaterialsText.setText(smartphone.getPhone().getMaterial());
        phoneColorsText.setText(smartphone.getPhone().getColors());
        phoneFingerprintText.setText(smartphone.getPhone().getFingerprint());
        phoneJackText.setText(smartphone.getPhone().getJack());
        phoneSimText.setText(smartphone.getPhone().getSim());
        phoneLaunchText.setText(smartphone.getPhone().getLaunch());

        connectivityLteText.setText(smartphone.getConnectivity().getLte());
        connectivityHspaText.setText(smartphone.getConnectivity().getHspa());
        connectivityEdgeText.setText(smartphone.getConnectivity().getEdge());
        connectivityWifiText.setText(smartphone.getConnectivity().getWifi());
        connectivityBluetoothText.setText(smartphone.getConnectivity().getBluetooth());
        connectivityNavigationText.setText(smartphone.getConnectivity().getNavigation());

        antutuText.setText(smartphone.getAntutu());

        smartphoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageViewer.Builder(DetailsActivity.this, smartphone.getGallery())
                        .hideStatusBar(false)
                        .setStartPosition(0)
                        .show();
            }
        });

        findViewById(R.id.tabla_kimovil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(smartphone.getUrl()));
                startActivity(i);
            }
        });

        if (smartphone.getReview() == null || smartphone.getReview().isEmpty()) {
            findViewById(R.id.action_review).setVisibility(View.GONE);
        } else {
            findViewById(R.id.action_review).setVisibility(View.VISIBLE);
        }

    }

    private void loadOthersVersions() {
        if (smartphone.getVersions() != null && smartphone.getVersions().size() > 1) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.others_recycler_view);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new OtherAdapter(this, smartphone.getVersions());
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerClickListener(this, this));

            findViewById(R.id.row_other).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.row_other).setVisibility(View.GONE);
        }
    }

    private void getSmartphoneFromIntent() {
        String json = getIntent().getExtras().getString(Smartphone.class.getName());
        smartphone = new Gson().fromJson(json, Smartphone.class);
        if (smartphone == null) {
            finish();
        }
    }

    private void setFabAction() {
        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) {
                    findViewById(R.id.background_fab_menu).setVisibility(View.GONE);
                } else{
                    findViewById(R.id.background_fab_menu).setVisibility(View.VISIBLE);
                }

                fabMenu.toggle(true);
            }
        });
    }

    public boolean isAnimating = false;
    private void setOnScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            stickyScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view,
                                           int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (!isAnimating) {
                        if ((scrollY > oldScrollY)) {
                            if (!fabMenu.isMenuButtonHidden())
                                fabMenu.hideMenuButton(true);
                        } else if ((scrollY < oldScrollY)) {
                            if (fabMenu.isMenuButtonHidden())
                            fabMenu.showMenuButton(true);
                        }
                        isAnimating = true;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                isAnimating = false;
                                if (stickyScrollView.getScrollY() < 10
                                        && fabMenu.isMenuButtonHidden()) {
                                    fabMenu.showMenuButton(true);
                                }
                            }
                        }, 500);
                    }
                }
            });
        }
    }

    private void setSpaceAboveScroll() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Context context = DetailsActivity.this;

                int screenHeightInit;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getRealSize(size);
                    screenHeightInit = size.y;
                } else {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    screenHeightInit = metrics.heightPixels;
                }

                Integer screenHeight = screenHeightInit;
                Integer actionBarHeight = getSupportActionBar().getHeight();
                Integer statusBarHeight = getStatusBarHeight(context);
                Integer navBarHeight = getNavigationBarSize(DetailsActivity.this).y;
                Integer container7Height =  findViewById(R.id.row_7).getHeight();
                Integer container8Height =  findViewById(R.id.row_8).getHeight();

                Float screenHeightDP = convertPixelsToDp(screenHeight.floatValue(), context);
                Float actionBarHeightDP = convertPixelsToDp(actionBarHeight.floatValue(), context);
                Float statusBarHeightDP = convertPixelsToDp(statusBarHeight.floatValue(), context);
                Float navBarHeightDP = convertPixelsToDp(navBarHeight.floatValue(), context);
                Float container7HeightDP = convertPixelsToDp(container7Height.floatValue(), context);
                Float container8HeightDP = convertPixelsToDp(container8Height.floatValue(), context);
                Float separatorHeightDP = screenHeightDP -
                        (actionBarHeightDP + statusBarHeightDP + navBarHeightDP
                                + container7HeightDP + container8HeightDP);

                Integer layoutPixels = convertDpToPixel(separatorHeightDP, DetailsActivity.this).intValue();
                if (layoutPixels > 1) {
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutPixels);
                    findViewById(R.id.container_separator).setLayoutParams(layoutParams);
                }
            }
        }, 200);
    }

    private void closeFab() {
        fabMenu.close(true);
        findViewById(R.id.background_fab_menu).setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Version version = adapter.getItem(position);
        if (version.getActive().equalsIgnoreCase("false")) {
            progressDialog = ProgressDialog.show(this, null,
                    getString(R.string.loading_information), true);
            searchController.loadPhone(version.getUrl());
            transitionView = view;
        }
    }

    @Subscribe
    public void onLoadComplete(LoadPhoneCompleteEvent event){
        progressDialog.dismiss();
        System.out.println(new Gson().toJson(event.getResponse()));
        Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
        intent.putExtra(Smartphone.class.getName(), new Gson().toJson(event.getResponse()));
        Pair<View, String> pPhone = Pair.create(findViewById(R.id.smartphone_image),  getString(R.string.transition_name_phone));
        Pair<View, String> pRam = Pair.create(findViewById(R.id.title_ram), getString(R.string.transition_name_name));
        Pair<View, String> pInches = Pair.create(findViewById(R.id.title_screen), getString(R.string.transition_name_name));
        Pair<View, String> pVersion = Pair.create(findViewById(R.id.title_version), getString(R.string.transition_name_name));
        Pair<View, String> pProcessor = Pair.create(findViewById(R.id.title_processor), getString(R.string.transition_name_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, pPhone, pProcessor, pRam, pInches, pVersion);
        ActivityCompat.startActivity(DetailsActivity.this, intent, options.toBundle());
        finish();
    }

    @Subscribe
    public void onLoadFailure(LoadPhoneFailureEvent event){
        progressDialog.dismiss();
        Toast.makeText(this, event.getResponse(), Toast.LENGTH_SHORT).show();
    }

    public void topAdd(View view) {
        Intent intent = new Intent(this, SyncPhoneActivity.class);
        intent.putExtra(Smartphone.class.getName(), new Gson().toJson(smartphone));
        startActivity(intent);
    }

    WizardDialog dialog;
    public void rating(View view) {
        closeFab();
        WizardDialog.Builder dialogBuilder = new WizardDialog.Builder(this);
        dialogBuilder.setMessageColor(getResources().getColor(R.color.white));
        dialogBuilder.setButtonTextColor(getResources().getColor(R.color.white));
        dialogBuilder.setTitleColor(getResources().getColor(R.color.white));
        dialogBuilder.setTabTextColor(getResources().getColor(R.color.white));
        dialogBuilder.setTabSelectedTextColor(getResources().getColor(R.color.white));
        dialogBuilder.showHeader(false);
        dialogBuilder.addFragment(smartphone.getTitle(), DialogRating.class);
        dialogBuilder.setTabPosition(WizardDialog.TabPosition.PREFER_HEADER);
        dialogBuilder.enableTabLayout(true);
        dialog = dialogBuilder.create();
        dialog.setMaxHeight(750);
        dialogBuilder.setBackground(R.color.material_drawer_dark_background);
        dialogBuilder.setFinishButtonText("CERRAR");
        dialog.show(getSupportFragmentManager(), "");
    }

    public void review(View view) {
        closeFab();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(smartphone.getReview())));
    }

    public static class DialogRating extends Fragment {

        private RatingBar ratingBar;
        private WizardDialog dialog;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_rating, container, false);

            ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);

            dialog = ((DetailsActivity) getActivity()).dialog;

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, final float rating, boolean b) {

                    if (rating > 0) {
                        dialog.getFinishButton().setText("VOTAR");
                    } else {
                        dialog.getFinishButton().setText("CERRAR");
                    }
                    dialog.getFinishButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println(rating);
                            if (rating > 0) {

                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                    //dialog.getFinishButton().performClick();
                }
            });

            return view;
        }
    }
}
