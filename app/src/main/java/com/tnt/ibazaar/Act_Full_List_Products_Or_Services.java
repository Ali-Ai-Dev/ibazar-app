package com.tnt.ibazaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.Adapter_Products_Or_Services_Full;
import models.Product;
import models.Service;
import network.webconnection.WebConnection;
import views.BadgeView;

public class Act_Full_List_Products_Or_Services extends AppCompatActivity {

    private Act_Full_List_Products_Or_Services mAct;

    private int isService;
    private int shopId;
    private Adapter_Products_Or_Services_Full adapter;
    private RecyclerView list_products_or_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__full__list__products__or__services);

        mAct = this;

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Search.class));
            }
        });


        isService = getIntent().getIntExtra("isService", -1);
        shopId = getIntent().getIntExtra("shopId", -1);

        list_products_or_services = (RecyclerView) findViewById(R.id.list_products_or_services);
        list_products_or_services.setLayoutManager(new LinearLayoutManager(mAct));


        Application.showProgressDialog(mAct);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("shopId", shopId + "")
                .appendQueryParameter("isService", isService + "")
                .appendQueryParameter("limitFrom",
                        String.valueOf(adapter == null ? 0 : adapter.getItemCount()));
        Log.e("builder", " " + builder.toString());
        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Log.e("result fullList", " " + result);
                Application.dismissProgressDialog();
                Log.e("isService ", " " + isService);
                if (isService == 1) {
                    fetchServices(result);
                } else if (isService == 0) {
                    fetchProducts(result);
                }
            }
        }).connect(builder, "fetchFullListProductModelsOrServiceNamesWithDiscount", "GET", 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void fetchProducts(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray productsJson = jsonObject.getJSONArray("Data");
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
            adapter = new Adapter_Products_Or_Services_Full(mAct, products, null, isService, shopId);
            list_products_or_services.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("products fullList error", " " + e.getMessage());
        }
    }

    private void fetchServices(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray servicesJson = jsonObject.getJSONArray("Data");
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
            adapter = new Adapter_Products_Or_Services_Full(mAct, null, services, isService, shopId);
            list_products_or_services.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("Services fullList error", " " + e.getMessage());
        }
    }
}
