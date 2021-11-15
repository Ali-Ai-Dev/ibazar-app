package com.tnt.ibazaar;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.model.LatLng;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rey.material.app.Dialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;

import adapters.Adapter_Navigation_Drawer;
import fragments.Frg_Category;
import fragments.Frg_Category_Gallery;
import fragments.Frg_Location;
import fragments.Frg_Product;
import fragments.Frg_Shop;
import models.Advertisement;

import views.BadgeView;
import views.DrawerArrowDrawable;

public class Act_Main extends AppCompatActivity
//        implements OnMapReadyCallback
{

    public static boolean active = true;
    Stack<Fragment> fragments;
    private Act_Main mAct;
    private SharedPreferences prefs;
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private DrawerLayout drawer;
    private SpaceNavigationView spaceNavigationView;
    private Fragment current_fragment;
    private LatLng selected_location = null;
    private int sidebar_height;
    private int topHeight = -500;
    private int bottomHeight = -500;
    private Frg_Product frg_product;
    private Frg_Shop frg_shop;
    private Frg_Location frg_location;
    private boolean shop_added = false,
            product_added = false,
            location_added = false;
    private SliderLayout mDemoSlider;
    private boolean exit = false;
    private boolean ShowingTopAndBottom = true;
    private Dialog dialog;
    private int cat1_id = -1;
    private String cat_title = "";

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        initNavigationDrawer();

        ImageView img_basket = (ImageView) findViewById(R.id.img_basket);
        BadgeView badge = new BadgeView(mAct);
        badge.setTargetView(img_basket);
        badge.setBadgeCount(Application.getCountPaymentCartOrderItems());
        img_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Basket.class));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        //make map persian
        String languageToLoad = "fa_";
        final Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        //make map persian
        setContentView(R.layout.act__main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(mAct);

        sidebar_height = 0;
        try {
            sidebar_height = getIntent().getExtras().getInt("height");
        } catch (Exception e) {

        }

        fragments = new Stack<>();

        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Search.class));
            }
        });

        initNavigationDrawer();

        initSpaceNavigation();

        initSlider();

        calculateTopAndBottomHeight();

    }

    private void calculateTopAndBottomHeight() {
        spaceNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (bottomHeight != -500)
                    return;
                bottomHeight = spaceNavigationView.getHeight();
                topHeight = mDemoSlider.getHeight();

            }
        });
    }

    private void initSpaceNavigation() {
        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);

        frg_location = Frg_Location.newInstance(sidebar_height, 130);
        frg_product = new Frg_Product();
        frg_shop = new Frg_Shop();

        spaceNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                frg_location.relocateMyLocationButton(spaceNavigationView.getLayoutParams().height);

                if (Build.VERSION.SDK_INT < 16)
                    spaceNavigationView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    spaceNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mapFragment();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                if (!isShowingTopAndBottom()) {
                    spaceNavigationView.changeActiveItem(-1);
                    return;
                }

                mapFragment();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                if (!isShowingTopAndBottom()) {
                    spaceNavigationView.changeActiveItem(-1);
                    showTopAndBottom(true);
                    return;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (itemIndex) {
                    case 0:
                        productFragment(ft);
                        break;
                    case 1:
                        shopFragment(ft);
                        break;
                }
            }


            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                if (itemIndex == -1) {
                    onCentreButtonClick();
                } else
                    onItemClick(itemIndex, itemName);
            }
        });
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.product),
                R.drawable.ic_product));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.shop),
                R.drawable.ic_store));
        spaceNavigationView.shouldShowFullBadgeText(true);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
    }

    private void shopFragment(FragmentTransaction ft) {

        if (current_fragment == frg_shop) {
            Log.e("shopFragment", "current_fragment returning");
            return;
        }
        if (!shop_added) {
//            if (location_added || product_added)
            ft.add(R.id.fragment, frg_shop).addToBackStack(null);
//            else
//                ft.replace(R.id.fragment, frg_shop).addToBackStack(null);
            shop_added = true;
        }
        ft.show(frg_shop);
        fragments.push(current_fragment);
        if (location_added || product_added)
            ft.hide(current_fragment);
        ft.commit();
        current_fragment = frg_shop;
        Log.e("shopFragment", "shop not added");
    }

    private void productFragment(FragmentTransaction ft) {
        if (current_fragment == frg_product) {
            Log.e("productFragment", "current_fragment returning");
            return;
        }

        if (!product_added) {
            Log.e("!product_added", "adding product frg");
//            if (location_added || shop_added)
            ft.add(R.id.fragment, frg_product);
//            else
//                ft.replace(R.id.fragment, frg_product);
            product_added = true;
        }
        ft.show(frg_product);
        fragments.push(current_fragment);
        ft.hide(current_fragment);
        ft.commit();
        current_fragment = frg_product;
    }

    private void mapFragment() {

        if (current_fragment == frg_location) {
            Log.e("mapFragment", "current_fragment returning");
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (checkPermissions()) {
            if (!location_added) {
                ft.add(R.id.fragment, frg_location).addToBackStack(null);
                location_added = true;
            }
            ft.show(frg_location);
            if (current_fragment != null) {
                fragments.push(current_fragment);
                ft.hide(current_fragment);
            }
            ft.commit();
            current_fragment = frg_location;

        } else
            requestPermission(mAct, 96);
    }

    private void initNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView imageView = (ImageView) findViewById(R.id.img_menu);
        drawerArrowDrawable = new DrawerArrowDrawable(getResources());
        drawerArrowDrawable.setStrokeColor(getResources()
                .getColor(R.color.md_white_1000));
        imageView.setImageDrawable(drawerArrowDrawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });


        CircularImageView img_user = (CircularImageView) findViewById(R.id.img_photo);
        String image_name = prefs.getString(Application.getCustomerId() + "image", "");
        String image = prefs.getString("Download_IP", "") +
                prefs.getString("Download_Folder_Customers", "") + "/" + image_name;
        Log.e("image", "" + image);
        if (!image_name.isEmpty() && !image_name.equalsIgnoreCase("null"))
            Picasso.with(mAct)
                    .load(image)
                    .placeholder(R.drawable.placeholder)
                    .into(img_user);
        else
            img_user.setImageResource(R.drawable.contactoutline);

        String customer_id = prefs.getString("customer_id", "0");
        final boolean logged_in = !customer_id.equals("0");

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logged_in) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    startActivity(new Intent(mAct, Act_Edit_Profile.class));
                } else
                    startActivity(new Intent(mAct, Act_SignUp.class));
            }
        });
        TextView txt_username = (TextView) findViewById(R.id.txt_user_name);
        String name = prefs.getString(Application.getCustomerId() + "name", "")
                + " " +
                prefs.getString(Application.getCustomerId() + "family", "");
        String mobile = prefs.getString(Application.getCustomerId() + "mobile", "");
        if (!name.equals(" ") && !name.contains("null")) {
            txt_username.setText(name);
        } else if (!mobile.equals("") && !mobile.equalsIgnoreCase("null"))
            txt_username.setText(mobile);
        else
            txt_username.setText(R.string.guest);

        txt_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.RIGHT);
                Intent i = new Intent(mAct, Act_Edit_Profile.class);
                i.putExtra("mobile", prefs.getString(Application.getCustomerId() + "mobile", ""));
                i.putExtra("name", prefs.getString(Application.getCustomerId() + "name", ""));
                i.putExtra("family", prefs.getString(Application.getCustomerId() + "family", ""));
                i.putExtra("reagent", prefs.getString(Application.getCustomerId() + "reagent", ""));
                startActivity(i);
            }
        });
        TextView txt_credit = (TextView) findViewById(R.id.txt_amount);

        if (logged_in) {
            txt_credit.setText(
                    String.format(Locale.ENGLISH, "%s %,d %s", getString(R.string.stock) + ": ", prefs.getLong("credit", 0),
                            getString(R.string.currency))
            );
            txt_credit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mAct, Act_Credit.class));
                }
            });
        } else {
            txt_credit.setText(R.string.register_login);
            txt_credit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mAct, Act_SignUp.class));
                }
            });
        }

        TextView txt_increase_credit = (TextView) findViewById(R.id.txt_increase_credit);
        if (logged_in) {
            txt_increase_credit.setText(R.string.increase_credit);
            txt_increase_credit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mAct, Act_Credit.class));
                }
            });
        } else {
            txt_increase_credit.setText("");
            txt_increase_credit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        RecyclerView lst_menu_items = (RecyclerView) findViewById(R.id.list_menus);
        lst_menu_items.setLayoutManager(new LinearLayoutManager(mAct));

        String[] s;
        if (logged_in) {
            s = getResources().getStringArray(R.array.menu_items_logged_in);
        } else {
            s = getResources().getStringArray(R.array.menu_items_not_logged_in);
        }
        Adapter_Navigation_Drawer adapter = new Adapter_Navigation_Drawer(mAct, s);
        lst_menu_items.setAdapter(adapter);
        TextView txt_score = (TextView) findViewById(R.id.txt_score);
        if (logged_in) {

            txt_score.setText("امتیاز من: 200");
        } else {
            txt_score.setText("");
        }
    }

    private void initSlider() {

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        mDemoSlider.setPresetTransformer(com.daimajia.slider.library.SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(com.daimajia.slider.library.SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(10000);

        final ArrayList<Advertisement> ads = Application.getAds();

        ArrayList<String> urls = new ArrayList<>();
        if (ads != null)
            for (Advertisement ad : ads) {
                String url = prefs.getString("Download_IP", "") +
                        prefs.getString("Download_Folder_Ad", "") +
                        "/" + ad.getImage_name();
                urls.add(url);
            }

        for (String name : urls) {
            DefaultSliderView textSliderView = new DefaultSliderView(mAct);

            // initialize a SliderLayout
            textSliderView
                    .image(name)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Advertisement ad = ads.get(mDemoSlider.getCurrentPosition());
                            if (ad.getType() == 1)
                                goToShop(ad.getShop_id());
                        }
                    });

            mDemoSlider.addSlider(textSliderView);

        }

    }

    public void goToShop(int shop_id, String shop_title) {
        Intent intent = new Intent(mAct, Act_Shop.class);
        intent.putExtra("shop_id", shop_id);
        intent.putExtra("shop_title", shop_title);
        startActivity(intent);
    }

    public void goToShop(int shop_id) {
        Intent intent = new Intent(mAct, Act_Shop.class);
        intent.putExtra("shop_id", shop_id);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else if (fragments != null && !fragments.isEmpty() &&
                (current_fragment instanceof Frg_Product ||
                        current_fragment instanceof Frg_Shop)) {

            fragments.removeAllElements();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(current_fragment);
            ft.show(frg_location);
            ft.commit();
            current_fragment = frg_location;
            spaceNavigationView.changeActiveItem(-1);

        } else if (!(current_fragment instanceof Frg_Location)) {
            Fragment topFragment = fragments.pop();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(current_fragment);
            ft.show(topFragment);
            ft.commit();
            current_fragment = topFragment;
            if (current_fragment instanceof Frg_Location) {
                spaceNavigationView.changeActiveItem(-1);
            } else if (current_fragment instanceof Frg_Product) {
                if (!isShowingTopAndBottom())
                    showTopAndBottom(true);
                spaceNavigationView.changeActiveItem(0);
            } else {
                if (!isShowingTopAndBottom())
                    showTopAndBottom(true);
                spaceNavigationView.changeActiveItem(1);
            }
        } else if (!exit) {
            Toast.makeText(mAct, R.string.press_back_again, Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);

        } else {
            finish();
        }
    }

    private void requestPermission(Activity mAct, int reqCode) {
        if (Build.VERSION.SDK_INT < 23)
            return;
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct,
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        if (neededPermissions.size() == 0)
            return;

        String[] strings = new String[neededPermissions.size()];
        for (int i = 0; i < neededPermissions.size(); i++) {
            strings[i] = neededPermissions.get(i);
        }
        ActivityCompat.requestPermissions(mAct, strings, reqCode);
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct,
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        if (neededPermissions.size() == 0)
            return true;
        else {
            for (String s : neededPermissions) {
                Log.e("neededPermissions", "" + s);
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 96) {
            if (checkPermissions()) {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.fragment, frg_location);
//                ft.commitAllowingStateLoss();

                if (!location_added) {
                    ft.add(R.id.fragment, frg_location).addToBackStack(null);
                    location_added = true;
                }
                ft.show(frg_location);
                if (current_fragment != null) {
                    fragments.push(current_fragment);
                    ft.hide(current_fragment);
                }
                ft.commitAllowingStateLoss();
                current_fragment = frg_location;

            }
        }
    }

    public boolean isShowingTopAndBottom() {
        return ShowingTopAndBottom;
    }

    public void showTopAndBottom(boolean b) {
        ShowingTopAndBottom = b;
        frg_location.showSideBar(!b);
        if (b) {
            mDemoSlider.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                  // fromXDelta
                    0,                  // toXDelta
                    -topHeight,                // fromYDelta
                    0);                 // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            mDemoSlider.startAnimation(animate);

            spaceNavigationView.setVisibility(View.VISIBLE);
            TranslateAnimation animate2 = new TranslateAnimation(
                    0,                  // fromXDelta
                    0,                  // toXDelta
                    bottomHeight,                // fromYDelta
                    0);                 // toYDelta
            animate2.setDuration(500);
            animate2.setFillAfter(true);
            spaceNavigationView.startAnimation(animate2);

            ImageView img_bar = (ImageView) findViewById(R.id.img_bar);
            TranslateAnimation animate3 = new TranslateAnimation(
                    0,                  // fromXDelta
                    0,                  // toXDelta
                    -topHeight - 20,                // fromYDelta
                    0);                 // toYDelta
            animate3.setDuration(500);
            animate3.setFillAfter(true);
            img_bar.startAnimation(animate3);
        } else {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                  // fromXDelta
                    0,                  // toXDelta
                    0,                // fromYDelta
                    -topHeight);                 // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!(current_fragment instanceof Frg_Location)) {
                        Log.e("showTopAndBottomTest", "here");
                        showTopAndBottom(true);
                    }
                    if (current_fragment instanceof Frg_Product) {
                        Log.e("showTopAndBottomTest", "prod");
                    }
                    if (current_fragment instanceof Frg_Category) {
                        Log.e("showTopAndBottomTest", "categ");
                    }
                    if (current_fragment instanceof Frg_Location) {
                        Log.e("showTopAndBottomTest", "Frg_Location");
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mDemoSlider.startAnimation(animate);

            ImageView img_bar = (ImageView) findViewById(R.id.img_bar);
            TranslateAnimation animate3 = new TranslateAnimation(
                    0,                  // fromXDelta
                    0,                  // toXDelta
                    0,                // fromYDelta
                    -topHeight - 20);                 // toYDelta
            animate3.setDuration(500);
            animate3.setFillAfter(true);
            img_bar.startAnimation(animate3);

            TranslateAnimation animate2 = new TranslateAnimation(
                    0,                  // fromXDelta
                    0,                  // toXDelta
                    0,                // fromYDelta
                    bottomHeight);                 // toYDelta
            animate2.setDuration(500);
            animate2.setFillAfter(true);
            spaceNavigationView.startAnimation(animate2);
            spaceNavigationView.setVisibility(View.GONE);

        }
    }

    public void loadCategory(int id, String title) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Frg_Category_Gallery frgCategoryGallery = Frg_Category_Gallery.newInstance(id, title);
        ft.add(R.id.fragment, frgCategoryGallery).addToBackStack(null);
        ft.show(frgCategoryGallery);
        fragments.push(current_fragment);
        ft.hide(current_fragment);
        ft.commit();
        current_fragment = frgCategoryGallery;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    public int getFirstPosition() {
        return frg_location.getFirstPosition();
    }

    public int getLastPosition() {
        return frg_location.getLastPosition();
    }

    public void sidebarSmoothScrollToPosition(int pos) {
        frg_location.sidebarSmoothScrollToPosition(pos);
    }

    public void logOut() {
        SharedPreferences.Editor mEditor = prefs.edit();
        mEditor.remove(Application.getCustomerId() + "mobile");
        mEditor.remove(Application.getCustomerId() + "name");
        mEditor.remove(Application.getCustomerId() + "family");
        mEditor.remove("customer_id");
        Application.setCustomerId("0");
        Application.setUserAccountParentId(0);
        mEditor.remove("userAccountParentId");
        Application.setLog_out(true);

        mEditor.apply();


        if (drawer.isDrawerOpen(Gravity.RIGHT))
            drawer.closeDrawer(Gravity.RIGHT);

        onResume();
    }

    public void setSelected_location(LatLng selected_location) {
        this.selected_location = selected_location;
    }

    public void setCategory(int id) {
        frg_location.setCategory(id);
    }

    public void setCategoryByPosition(int position) {

        frg_location.setCategoryByPosition(position);
    }


}
