package com.tnt.ibazaar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import adapters.Adapter_Shop_Pager;
import butterknife.BindView;
import butterknife.ButterKnife;
import database.DataBase;
import models.Category;
import models.Certificate;
import models.Product;
import models.ServerResponse;
import models.Service;
import models.Staff;
import models.UserComment;
import models.shop.Announcement;
import models.shop.ShopInfo;
import network.webconnection.WebConnection;
import views.BadgeView;

public class Act_Shop extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.img_basket)
    ImageView img_basket;
    @BindView(R.id.layout_wait)
    RelativeLayout layout_wait;
    private Act_Shop mAct;
    private int shop_id = -1;
    private String shop_title = "";

    private String shopDetailsJson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;
        ButterKnife.bind(mAct);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            shop_id = getIntent().getExtras().getInt("shop_id");

            shop_title = getIntent().getExtras().getString("shop_title");
        } catch (Exception e) {
            Log.e("error", "" + e.getMessage());
            finish();
        }
        if (shop_id == -1)
            finish();
        if (shop_title == null)
            shop_title = "";

        getShopDetails(shop_id);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Search.class));
            }
        });


        img_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Basket.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void getShopDetails(int shop_id) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("shopId", "" + shop_id);
        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                    if (response.getStatus() == 100) {
                        response.setData(jsonObject.getString("Data"));
                    }
                } catch (Exception e) {
                    Log.e("getShopDetails error", " " + e.getMessage());
                }

                if (response == null) {
                    Toast.makeText(mAct, R.string.not_connected_check_internet, Toast.LENGTH_SHORT).show();
                    return;
                }

                shopDetailsJson = (String) response.getData();

                ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
                SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
                Adapter_Shop_Pager adapter = new Adapter_Shop_Pager(getSupportFragmentManager(), mAct);
                pager.setAdapter(adapter);
                viewPagerTab.setViewPager(pager);
                pager.setOffscreenPageLimit(adapter.getCount() / 2);
                pager.setCurrentItem(adapter.getCount() - 1, false);
                layout_wait.setVisibility(View.GONE);

            }
        }).connect(builder, "fetchShopDetails", "GET", 0);
    }


    public ShopInfo getShopInfo() {
        ShopInfo shopInfo = new ShopInfo();
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            JSONObject info = jsonObject.getJSONObject("Info");

            if (info.has("RegisterDate"))
                shopInfo.setRegister_date(info.getLong("RegisterDate"));
            if (info.has("GuildType"))
                shopInfo.setGuild_type(info.getString("GuildType"));
            if (info.has("Title"))
                shopInfo.setName(info.getString("Title"));
            if (info.has("Address"))
                shopInfo.setAddress(info.getString("Address"));
            if (info.has("Medal"))
                shopInfo.setMedal(info.getInt("Medal"));
            if (info.has("ImgName"))
                shopInfo.setIcon_url(info.getString("ImgName"));
            if (info.has("Website"))
                shopInfo.setWebsite(info.getString("Website"));
            if (info.has("Email"))
                shopInfo.setEmail(info.getString("Email"));
            if (info.has("ShopTel"))
                shopInfo.setPhone(info.getString("ShopTel"));
            if (info.has("Score"))
                shopInfo.setScore(Float.parseFloat(info.getString("Score")));
            if (info.has("VoteCount"))
                shopInfo.setVote_count(info.getInt("VoteCount"));
            if (info.has("Admin")) {
                JSONObject admin = info.getJSONObject("Admin");
                shopInfo.setAdmin_name(admin.getString("Name") + " "
                        + admin.getString("Family"));
                shopInfo.setAdmin_image(admin.getString("ImgAdmin"));
            }
            if (info.has("Services"))
                shopInfo.setService_description(info.getString("Services"));
            if (info.has("Open"))
                shopInfo.setOpen(info.getInt("Open") == 1);
            if (info.has("LicenseNumber"))
                shopInfo.setLicense_number(info.getString("LicenseNumber"));

            if (info.has("Image")) {
                JSONArray imagesArray = info.getJSONArray("Image");
//                int img_count = imagesObject.getInt("Counter");
                ArrayList<String> image_urls = new ArrayList<>();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
                for (int i = 0; i < imagesArray.length(); i++) {
                    String url = prefs.getString("Download_IP", "") +
//                            ":" +
//                            prefs.getInt("Download_Port", 6000) + "/" +
                            prefs.getString("Download_Folder_Shop", "") + "/" +
                            imagesArray.getJSONObject(i).getString("Image");
                    image_urls.add(url);
                }
//                Log.e("size", "" + image_urls.size());
                shopInfo.setImage_urls(image_urls);
            }
        } catch (Exception e) {
            Log.e("error", "" + e.getMessage());
        }

        return shopInfo;
    }

    public ArrayList<Category> getProductCategories(int parent) {
        return null;
    }

    public ArrayList<Product> getBestsellers() {
        return null;
    }

    public ArrayList<UserComment> getUserComments() {
        ArrayList<UserComment> userComments = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            JSONArray commentArray = jsonObject.getJSONArray("Comment");
            for (int i = 0; i < commentArray.length(); i++) {
                UserComment userComment = new UserComment();
                JSONObject object = commentArray.getJSONObject(i);
//                userComment.setTitle(object.getString("Title"));
                userComment.setUserName(object.getString("CustomerName") +
                        " " + object.getString("CustomerFamily"));
                userComment.setDate_time(object.getString("DateTime"));
                userComment.setComment(object.getString("Comment"));

                userComment.setP1(object.getInt("Prog1"));
                userComment.setP2(object.getInt("Prog2"));
                userComment.setP3(object.getInt("Prog3"));

                userComments.add(userComment);
            }
        } catch (Exception e) {
            Log.e("getUserComments error", "" + e.getMessage());
        }
        return userComments;
    }

    public ArrayList<Announcement> getAnnouncements() {
        ArrayList<Announcement> announcements = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            JSONArray announcementArray = jsonObject.getJSONArray("Notify");
            for (int i = 0; i < announcementArray.length(); i++) {
                Announcement announcement = new Announcement();
                JSONObject object = announcementArray.getJSONObject(i);
                announcement.setTitle(object.getString("Title"));
                announcement.setDate_time(object.getString("DateTime"));

                announcement.setIcon_url(object.getString("ImageName"));
                announcement.setMessage(object.getString("Msg"));
                announcements.add(announcement);
            }
        } catch (Exception e) {
            Log.e("getAnnouncements error", "" + e.getMessage());
        }
        return announcements;
    }

    public LatLng getShopCoordinates() {
        double lat = 0, lng = 0;
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            JSONObject info = jsonObject.getJSONObject("Info");

            if (info.has("Lat"))
                lat = info.getDouble("Lat");
            if (info.has("Lng"))
                lng = info.getDouble("Lng");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LatLng(lat, lng);
    }

    public ArrayList<Staff> getStaff() {
        ArrayList<Staff> staffs = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            JSONObject info = jsonObject.getJSONObject("Info");
            JSONArray staffArray = info.getJSONArray("Staff");
//            int Counter = staffArray.getInt("Counter");
            for (int i = 0; i < staffArray.length(); i++) {
                Staff staff = new Staff();
                JSONObject object = staffArray.getJSONObject(i);
                staff.setName(object.getString("Name") +
                        " " + object.getString("Family"));
                staff.setImg_name(object.getString("ImgStaff"));
                staff.setPost(object.getString("Post"));
                staffs.add(staff);
            }
        } catch (Exception e) {
            Log.e("getStaff error", "" + e.getMessage());
        }

        return staffs;
    }

    public ArrayList<Certificate> getCertificates() {
        ArrayList<Certificate> certificates = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            JSONObject info = jsonObject.getJSONObject("Info");
            JSONArray CertificateArray = info.getJSONArray("Certificate");
//            int Counter = CertificateArray.getInt("Counter");
            for (int i = 0; i < CertificateArray.length(); i++) {
                Certificate certificate = new Certificate();
                JSONObject object = CertificateArray.getJSONObject(i);
                certificate.setTitle(object.getString("Title"));
                certificate.setImg_name(object.getString("ImgName"));
                certificates.add(certificate);
            }
        } catch (Exception e) {
            Log.e("getCertificates", e.getMessage());
        }
        return certificates;
    }

    public int getShopId() {
        return shop_id;
    }

    public void bookmark(boolean flag) {
        if (!flag) {
            //delete downloaded images and delete image_names from db
//            ArrayList<String> image_names = DataBase.getImageNames(shop_id);
//            if (image_names != null) {
//                for (String image_name : image_names) {
//                    File sdcard = Environment.getExternalStorageDirectory();
//                    File myDir = new File(sdcard, "/android/data/" + Application.getPackageName2());
//                    File file = new File(myDir, image_name);
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                    DataBase.deleteShopImage(shop_id, image_name);
//                }
//            }
//            ArrayList<Staff> staff = getStaff();
//            if (staff != null) {
//                for (Staff s : staff) {
//                    File sdcard = Environment.getExternalStorageDirectory();
//                    File myDir = new File(sdcard, "/android/data/" + Application.getPackageName2());
//                    File file = new File(myDir, s.getImg_name());
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                    DataBase.deleteShopImage(shop_id, s.getImg_name());
//                }
//            }
//            File sdcard = Environment.getExternalStorageDirectory();
//            File myDir = new File(sdcard, "/android/data/" + Application.getPackageName2());
//            File file = new File(myDir, getShopInfo().getAdmin_image());
//            if (file.exists()) {
//                file.delete();
//            }


            //delete shop data from shops table
            DataBase.deleteShop(shop_id);
        } else {
            //download images and insert image_names to db
//            ArrayList<String> image_urls = getShopInfo().getImage_urls();
//            if (image_urls != null) {
//                for (String url : image_urls) {
//                    String file_name = url.substring(url.lastIndexOf("/"));
//                    download(url, file_name);
//                    DataBase.insertImageName(shop_id, file_name);
//                }
//            }
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
//            String admin_image = getShopInfo().getAdmin_image();
//            String url = prefs.getString("Download_IP", "") + ":" +
//                    prefs.getInt("Download_Port", 6000) + "/" +
//                    prefs.getString("Download_Folder_Shop", "") + "/" +
//                    admin_image;
//            download(url, admin_image);
//
//            ArrayList<Staff> staff = getStaff();
//            if (staff != null) {
//                for (Staff s : staff) {
//                    DataBase.insertStaff(s);
//                    url = prefs.getString("Download_IP", "") + ":" +
//                            prefs.getInt("Download_Port", 6000) + "/" +
//                            prefs.getString("Download_Folder_Seller", "") + "/" +
//                            s.getImg_name();
//                    download(url, s.getImg_name());
//                }
//            }
            //insert shop data to db
            DataBase.insertShop(getShopInfo(), shop_id);
        }
    }

    private void download(String path, String file_name) {
        try {
            URL url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
//            int fileLenth = c.getContentLength();

            File sdcard = Environment.getExternalStorageDirectory();
            File myDir = new File(sdcard, "/android/data/" + Application.getPackageName2());
            myDir.mkdirs();
            File outputFile = new File(myDir, file_name);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1;
//            long total = 0;
            while ((len1 = is.read(buffer)) != -1) {
//                total += len1;
                fos.write(buffer, 0, len1);
//                publishProgress("" + (int) ((total * 100) / fileLenth));
            }

            fos.flush();
            fos.close();
            is.close();


        } catch (Exception e) {
            Log.e("download error", " " + e.getMessage());
        }
    }

    public ArrayList<Product> getSalesProducts() {
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            String s = jsonObject.getString("ShopProductsWithDiscount");
            Log.e("ShopProducts_Discount", s);
            JSONArray productsJson = jsonObject.getJSONArray("ShopProductsWithDiscount");
            ArrayList<Product> products = new ArrayList<>();
            for (int i = 0; i < productsJson.length(); i++) {
                Product product = new Product();
                JSONObject productJson = productsJson.getJSONObject(i);
                product.setId(productJson.getInt("productModelId"));
                if (productJson.has("productCategoryTitle"))
                    product.setCategoryTitle(productJson.getString("productCategoryTitle"));
                product.setTitle(productJson.getString("productModelName"));
                product.setBrandTitle(productJson.getString("productBrandName"));
                product.setMain_image(productJson.getString("productDefaultLogoUrl") +
                        productJson.getString("productDefaultLogo"));
                product.setPrice(productJson.getInt("productPrice"));
                product.setDiscount(productJson.getInt("productDiscount"));
                if (product.getDiscount() > 0 && product.getDiscount() < 100) {
                    int discount = product.getPrice() * product.getDiscount() / 100;
                    product.setDiscount(discount);
                }
                products.add(product);
            }
            Log.e("products", " " + s);
            return products;
        } catch (Exception e) {
            Log.e("products error", " " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Service> getSalesServices() {
        try {
            JSONObject jsonObject = new JSONObject(shopDetailsJson);
            String s = jsonObject.getString("ShopServicesWithDiscount");

            JSONArray servicesJson = jsonObject.getJSONArray("ShopServicesWithDiscount");
            ArrayList<Service> services = new ArrayList<>();
            for (int i = 0; i < servicesJson.length(); i++) {
                JSONObject serviceObject = servicesJson.getJSONObject(i);
                Service service = new Service();
                service.setTitle(serviceObject.getString("serviceName"));
                service.setId(serviceObject.getInt("serviceNameId"));
                service.setCategory(serviceObject.getString("serviceCategoryTitle"));
                service.setPrice(serviceObject.getInt("servicePrice"));
                service.setDiscount(serviceObject.getInt("serviceDiscount"));
                if (service.getDiscount() > 0 && service.getDiscount() < 100) {
                    int discount = service.getPrice() * service.getDiscount() / 100;
                    service.setDiscount(discount);
                }
                service.setIcon(serviceObject.getString("serviceDefaultLogoUrl") +
                        serviceObject.getString("serviceDefaultLogo"));
                services.add(service);
            }
            Log.e("ShopServicesWithD", " " + s);
            return services;

        } catch (Exception e) {

        }
        return null;
    }
}
