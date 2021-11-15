package com.tnt.ibazaar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import io.fabric.sdk.android.Fabric;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import models.Advertisement;
import models.Brand;
import models.Category;

/**
 * Created by Omid on 3/13/2018.
 */

public class Application extends android.app.Application {

    private static int level = 0;
    private static String countryCode = "uk";
    private static String packageName;
    private static String brandsJsonString;
    private static Context context;
    private static String categoriesJsonString;
    private static String adsJsonString;
    private static boolean local = false;
    private static String customerId = "0";
    private static ProgressDialog mProgressDialog;
    private static int userAccountParentId;
    private static int countPaymentCartOrderItems;
    private static boolean log_out = false;

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        Application.level = level;
    }

//    public static String getAdsJsonString() {
//        return adsJsonString;
//    }

    public static String getCountryCode() {
        return countryCode;
    }

    public static void setCountryCode(String countryCode) {
        Application.countryCode = countryCode;
    }

    public static String getPackageName2() {
        return packageName;
    }

    public static Context getContext() {
        return context;
    }

//    public static String getBrandsJsonString() {
//        return brandsJsonString;
//    }

    public static ArrayList<Category> getCategoryChildren(int parent_id) {
        try {
            JSONArray jsonArray = new JSONArray(categoriesJsonString);
            ArrayList<Category> categories = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() && Act_Main.active; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (parent_id != jsonObject.getInt("ParnetID"))
                    continue;

                Category category = new Category();
                category.setId(jsonObject.getInt("ID"));
                category.setTitle(jsonObject.getString("Name"));
                category.setParent(jsonObject.getInt("ParnetID"));
                if (jsonObject.has("ImageName"))
                    category.setImage_name(jsonObject.getString("ImageName"));
                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            Log.e("error", " " + e.getMessage());
        }
        return null;
    }

    public static void setCategoriesJsonString(String categoriesJsonString) {
        Application.categoriesJsonString = categoriesJsonString;
    }

    public static void setAdsJsonString(String adsJsonString) {
        Application.adsJsonString = adsJsonString;
    }

    public static void setBrandsJsonString(String brandsJsonString) {
        Application.brandsJsonString = brandsJsonString;
    }

    public static boolean isLocal() {
        return local;
    }

    public static Category getCategory(int cat_id) {
        try {
            JSONArray jsonArray = new JSONArray(categoriesJsonString);

            for (int i = 0; i < jsonArray.length() && Act_Main.active; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (cat_id != jsonObject.getInt("ID"))
                    continue;

                Category category = new Category();
                category.setId(jsonObject.getInt("ID"));
                category.setTitle(jsonObject.getString("Name"));
                category.setParent(jsonObject.getInt("ParnetID"));
                if (jsonObject.has("ImageName"))
                    category.setImage_name(jsonObject.getString("ImageName"));
                return (category);
            }

        } catch (Exception e) {
            Log.e("error", " " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Advertisement> getAds() {

        try {
            JSONArray jsonArray = new JSONArray(adsJsonString);
            ArrayList<Advertisement> ads = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Advertisement advertisement = new Advertisement();
                advertisement.setId(jsonObject.getInt("ID"));
                advertisement.setImage_name(jsonObject.getString("ImageName"));
                advertisement.setType(jsonObject.getInt("Type"));
                advertisement.setShop_id(jsonObject.getInt("DataID"));
//                    advertisement.setLast_modified(jsonObject.getString("last_modified"));
                ads.add(advertisement);
            }
            return ads;
        } catch (Exception e) {
            Log.e("error", "" + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Brand> getBrands() {
        try {
            JSONArray jsonArray = new JSONArray(brandsJsonString);
            ArrayList<Brand> brands = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Brand brand = new Brand();
                brand.setId(jsonObject.getInt("ID"));
                brand.setTitle(jsonObject.getString("Title"));
                brand.setImage_name(jsonObject.getString("ImageName"));
                brands.add(brand);
            }
            return brands;
        } catch (Exception e) {
            Log.e("error", "" + e.getMessage());
        }
        return null;
    }

    public static String getCustomerId() {
        return customerId;
    }

    public static void setCustomerId(String customerId) {
        Application.customerId = customerId;
    }

    public static String NormalizeString(String s) {
        if (s == null || s.contains("null"))
            return "";
        return s;
    }

    public static void showProgressDialog(Context context) {
        if (mProgressDialog != null)
            return;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getContext().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static int getUserAccountParentId() {
        return userAccountParentId;
    }

    public static void setUserAccountParentId(int userAccountParentId) {
        Application.userAccountParentId = userAccountParentId;
    }

    public static int getCountPaymentCartOrderItems() {
        return countPaymentCartOrderItems;
    }

    public static void setCountPaymentCartOrderItems(int countPaymentCartOrderItems) {
        Application.countPaymentCartOrderItems = countPaymentCartOrderItems;
    }

    public static boolean isLog_out() {
        return log_out;
    }

    public static void setLog_out(boolean log_out) {
        Application.log_out = log_out;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        packageName = getPackageName();
        context = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        customerId = preferences.getString("customer_id", "0");
        userAccountParentId = preferences.getInt("userAccountParentId", 0);

    }
}