package com.tnt.ibazaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

import adapters.Adapter_Orders;
import butterknife.BindView;
import butterknife.ButterKnife;
import models.Color;
import models.Order;
import models.Product;
import models.Service;
import network.webconnection.WebConnection;
import views.BadgeView;


public class Act_Basket extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;
    private Act_Basket mAct;
    private boolean pay = false;

    private AppCompatButton btn_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__basket);
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

        Application.showProgressDialog(mAct);
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("userAccountParentId",
                        Application.getUserAccountParentId() + "");
        Log.e("builder", builder.toString());

        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Application.dismissProgressDialog();

                if (result == null) {
                    Toast.makeText(mAct, R.string.not_connected_check_internet, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("Status") == 100) {
                        JSONObject Data = object.getJSONObject("Data");
                        ArrayList<Order> orders = new ArrayList<>();
                        JSONArray stockArray = Data.getJSONArray("paymentOrdersCartToStockPrice");
                        for (int i = 0; i < stockArray.length(); i++) {
                            JSONObject orderObject = stockArray.getJSONObject(i);

                            Order order = new Order();

                            Product product = new Product();
                            product.setId(orderObject.getInt("paymentOrderCartToStockPriceId"));
                            product.setTitle(orderObject.getString("modelName"));
                            product.setPrice(orderObject.getInt("price"));
                            product.setDiscount(orderObject.getInt("discount"));
                            String picName = null;
                            if (orderObject.has("shopProductSpecificLogo")) {
                                picName = orderObject.getString("shopProductSpecificLogo");
                                if (picName != null && !picName.isEmpty() && !picName.equalsIgnoreCase("null")) {
                                    product.setMain_image(Data.getString("shopProductSpecificLogoUrl") +
                                            picName);
                                }
                            }
                            if (picName == null || picName.isEmpty() || picName.equalsIgnoreCase("null")) {
                                picName = orderObject.getString("productDefaultLogo");
                                if (picName != null && !picName.isEmpty() && !picName.equalsIgnoreCase("null")) {
                                    product.setMain_image(Data.getString("productDefaultLogoUrl") +
                                            picName);
                                }
                            }

                            order.setProduct(product);
                            order.setMaxOrder(orderObject.getInt("maxOrder"));
                            order.setOrderQuantity(orderObject.getInt("orderQuantity"));

                            orders.add(order);
                        }

                        JSONArray stockColorArray = Data.getJSONArray("paymentOrdersCartToStockColorPrice");
                        for (int i = 0; i < stockColorArray.length(); i++) {
                            JSONObject orderObject = stockColorArray.getJSONObject(i);

                            Order order = new Order();

                            Product product = new Product();
                            product.setId(orderObject.getInt("paymentOrderCartToStockColorPriceId"));
                            product.setTitle(orderObject.getString("modelName"));
                            product.setPrice(orderObject.getInt("price"));
                            product.setDiscount(orderObject.getInt("discount"));
                            String picName = null;
                            if (orderObject.has("shopProductSpecificLogo")) {
                                picName = orderObject.getString("shopProductSpecificLogo");
                                if (picName != null && !picName.isEmpty() && !picName.equalsIgnoreCase("null")) {
                                    product.setMain_image(Data.getString("shopProductSpecificLogoUrl") +
                                            picName);
                                }
                            }
                            if (picName == null || picName.isEmpty() || picName.equalsIgnoreCase("null")) {
                                picName = orderObject.getString("productDefaultLogo");
                                if (picName != null && !picName.isEmpty() && !picName.equalsIgnoreCase("null")) {
                                    product.setMain_image(Data.getString("productDefaultLogoUrl") +
                                            picName);
                                }
                            }

                            Color color = new Color();
                            color.setColorName(orderObject.getString("colorName"));
                            color.setColorCode(orderObject.getString("colorCodeNumber"));
                            product.setProductColor(color);

                            order.setProduct(product);
                            order.setMaxOrder(orderObject.getInt("maxOrder"));
                            order.setOrderQuantity(orderObject.getInt("orderQuantity"));

                            orders.add(order);
                        }

                        JSONArray serviceArray = Data.getJSONArray("paymentOrdersCartToServiceName");
                        for (int i = 0; i < serviceArray.length(); i++) {
                            JSONObject orderObject = serviceArray.getJSONObject(i);
                            Order order = new Order();
                            Service service = new Service();
                            service.setId(orderObject.getInt("paymentOrderCartToShopToServiceNameId"));
                            service.setTitle(orderObject.getString("serviceName"));

                            String picName = null;
                            if (orderObject.has("shopServiceSpecificLogo")) {
                                picName = orderObject.getString("shopServiceSpecificLogo");
                                if (picName != null && !picName.isEmpty() && !picName.equalsIgnoreCase("null")) {
                                    service.setIcon(Data.getString("shopServiceSpecificLogoUrl") +
                                            picName);
                                }
                            }
                            if (picName == null || picName.isEmpty() || picName.equalsIgnoreCase("null")) {
                                picName = orderObject.getString("serviceDefaultLogo");
                                if (picName != null && !picName.isEmpty() && !picName.equalsIgnoreCase("null")) {
                                    service.setIcon(Data.getString("serviceDefaultLogoUrl") +
                                            picName);
                                }
                            }

                            service.setPrice(orderObject.getInt("price"));
                            service.setDiscount(orderObject.getInt("discount"));

                            order.setService(service);
                            order.setMaxOrder(orderObject.getInt("maxOrder"));
                            order.setOrderQuantity(orderObject.getInt("orderQuantity"));

                            orders.add(order);
                        }
                        if (Application.getCountPaymentCartOrderItems() > 0) {
                            LinearLayout layout_empty_basket = (LinearLayout) findViewById(R.id.layout_empty_basket);
                            layout_empty_basket.setVisibility(View.GONE);
                        }

                        RecyclerView list_orders = (RecyclerView) findViewById(R.id.list_orders);
                        list_orders.setLayoutManager(new LinearLayoutManager(mAct));
                        list_orders.setAdapter(new Adapter_Orders(mAct, orders));

                    } else {
                        Toast.makeText(mAct, object.getString("MSG"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("error getCartProducts", " " + e.getMessage());
                }

            }
        }).connect(builder, "fetchPaymentCartOrders", "GET", 2);

        btn_verify = (AppCompatButton) findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Application.getUserAccountParentId() != 0) {
                    Uri.Builder builder1 = new Uri.Builder()
                            .appendQueryParameter("userAccountParentId",
                                    Application.getUserAccountParentId() + "")
                            .appendQueryParameter("bankPaymentGateway", "zarinpal");
                    Application.showProgressDialog(mAct);
                    new WebConnection(new WebConnection.ConnectionResponse() {
                        @Override
                        public void connectionFinish(String result, String urlCanConnect) {
                            Application.dismissProgressDialog();
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getInt("Status") == 100) {
                                    String url = object.getString("Data");
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                } else {
                                    Toast.makeText(mAct, object.getString("MSG"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e("error payPaymentCart", " " + e.getMessage());
                            }
                        }
                    }).connect(builder1, "payPaymentCart", "GET", 2);
                } else {
                    pay = true;
                    startActivity(new Intent(mAct, Act_SignUp.class));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pay)
            btn_verify.performClick();

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data == null) {
            return;
        }
//        Log.e("data uri", " " + data.toString());
        ArrayList<String> urlArray = new ArrayList<>(Arrays.asList(data.toString().split("/")));
        String message = urlArray.get(urlArray.size() - 1);

        try {
            message = URLDecoder.decode(message, "UTF-8");
            if (message.contains("فرآیند پرداخت موفقیت آمیز بود.")) {
                goToMainPage();
                Toast.makeText(mAct, message, Toast.LENGTH_SHORT).show();
            } else if (message.contains("فرآیند پرداخت، قبلا انجام شده و نیاز به تأیید مجدد نیست")){
                goToMainPage();
            }
            Toast.makeText(mAct, message, Toast.LENGTH_SHORT).show();
            Log.e("message", " " + message);
        } catch (Exception e) {
            Log.e("error decode", " " + e.getMessage());
        }

    }

    private void goToMainPage(){
        Application.setCountPaymentCartOrderItems(0);

        Intent intent = new Intent(getApplicationContext(), Act_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
