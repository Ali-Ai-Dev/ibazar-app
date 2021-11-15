package com.tnt.ibazaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import adapters.Adapter_Colors;
import butterknife.BindView;
import butterknife.ButterKnife;
import models.Color;
import models.Product;
import models.Service;
import network.webconnection.WebConnection;
import views.BadgeView;
import views.CustomSliderView;

public class Act_Product_And_Service extends AppCompatActivity {

    @BindView(R.id.img_share)
    ImageView img_share;
    @BindView(R.id.img_bookmark)
    ImageView img_bookmark;
    @BindView(R.id.txt_product_title)
    TextView txt_product_title;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_final_price)
    TextView txt_final_price;
    @BindView(R.id.btn_add_to_basket)
    Button btn_add_to_basket;
    @BindView(R.id.expandable_text)
    TextView expand_text_view;
    @BindView(R.id.txt_category)
    TextView txt_category;
    @BindView(R.id.layout_wait)
    RelativeLayout layout_wait;
    private Act_Product_And_Service mAct;
    private Product product;
    private Service service;

    private boolean isService;

    private String productJsonString;
    private JSONObject productJson;

    private String serviceJsonString;
    private JSONObject serviceJson;

    private int stockPriceId;
    private int stockColorPriceId;
    private int stockClothColorSizePriceId = -1;
    private int shopToServiceNameId;

    private int shopId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAct = this;
        ButterKnife.bind(mAct);

        isService = getIntent().getExtras().getBoolean("isService", false);

        if (!isService)
            product = getIntent().getExtras().getParcelable("item");
        else
            service = getIntent().getExtras().getParcelable("item");

        shopId = getIntent().getIntExtra("shopId", -1);
        layout_wait.setVisibility(View.VISIBLE);
        Application.showProgressDialog(mAct);
        if (!isService) {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("productModelId", product.getId() + "")
                    .appendQueryParameter("shopId", "" + shopId + "");
            new WebConnection(new WebConnection.ConnectionResponse() {
                @Override
                public void connectionFinish(String result, String urlCanConnect) {
                    Application.dismissProgressDialog();
                    productJsonString = result;
                    try {
                        productJson = new JSONObject(productJsonString);
                        go();
                    } catch (Exception e) {
                        Log.e("errr", " " + e.getMessage());
                    }
                }
            }).connect(builder, "fetchProductDetailsByBarcodeNumberOrProductModelIdWithShopId", "GET", 1);
        } else {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("serviceNameId",
                            service.getId() + "")
                    .appendQueryParameter("shopId", "" + getIntent().getIntExtra("shopId", -1) + "");

            new WebConnection(new WebConnection.ConnectionResponse() {
                @Override
                public void connectionFinish(String result, String urlCanConnect) {
                    Application.dismissProgressDialog();
                    serviceJsonString = result;
                    try {
                        serviceJson = new JSONObject(serviceJsonString);
                        go();
                    } catch (Exception e) {
                        Log.e("errr", " " + e.getMessage());
                    }
                }
            }).connect(builder, "fetchServiceDetailsByServiceNameIdWithShopId", "GET", 1);
        }

        btn_add_to_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.showProgressDialog(mAct);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userAccountParentId",
                                Application.getUserAccountParentId() + "");
                if (stockPriceId != -1) {
                    builder.appendQueryParameter("stockPriceId", "" + stockPriceId);
                }
                if (stockColorPriceId != -1) {
                    builder.appendQueryParameter("stockColorPriceId", "" + stockColorPriceId);
                }
                if (stockClothColorSizePriceId != -1) {
                    builder.appendQueryParameter("stockClothColorSizePriceId", "" + stockClothColorSizePriceId);
                }
                if (shopToServiceNameId != -1) {
                    builder.appendQueryParameter("shopToServiceNameId", "" + shopToServiceNameId);
                }

                builder.appendQueryParameter("shopId", shopId + "");

                Log.e("builder", builder.toString());
                new WebConnection(new WebConnection.ConnectionResponse() {
                    @Override
                    public void connectionFinish(String result, String urlCanConnect) {
                        Application.dismissProgressDialog();
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getInt("Status") == 100) {
                                ImageView img_basket = (ImageView) findViewById(R.id.img_basket);
                                BadgeView badge = new BadgeView(mAct);
                                badge.setTargetView(img_basket);
                                badge.setBadgeCount(object.getInt("Data"));
                                Application.setCountPaymentCartOrderItems(object.getInt("Data"));
                            }
                            Toast.makeText(mAct, object.getString("MSG"), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Log.e("error addToPaymentCart", " " + e.getMessage());
                        }

                    }
                }).connect(builder, "addToPaymentCart", "POST", 2);
            }
        });
    }

    private void go() {
        layout_wait.setVisibility(View.GONE);

        setupSlider();

        TextView txt_product_cat = (TextView) findViewById(R.id.txt_product_cat);
        txt_product_cat.setText(getProductAndServiceCategory());

        TextView txt_product_title = (TextView) findViewById(R.id.txt_product_title);
        txt_product_title.setText(getProductAndServiceTitle());

        TextView txt_product_brand = (TextView) findViewById(R.id.txt_product_brand);
        txt_product_brand.setText(getProductBrandTitle());

        Button btn_specs = (Button) findViewById(R.id.btn_specs);
        btn_specs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Product_Details_Specs.class);
                intent.putExtra("specs", getProductAndServiceSpecs());
                startActivity(intent);
            }
        });

        TextView expandable_text = (TextView) findViewById(R.id.expandable_text);

        String description = getShopDescription();
        if (description == null || description.isEmpty()) {
            description = getProductAndServiceDescription();
        }
        expandable_text.setText(description);

        int price = getPrice();
        int discount = getDiscount();
        if (discount > 0 && discount < 100) {
            discount = price * discount / 100;
        }
        displayPrice(price, discount);

        if (isProductColored()) {
            ArrayList<Color> colors = getColors();
            RecyclerView list_colors = (RecyclerView) findViewById(R.id.list_colors);
            list_colors.setLayoutManager(
                    new LinearLayoutManager(mAct, LinearLayoutManager.HORIZONTAL, true));
            if (colors != null && colors.size() > 0)
                colors.get(0).setSelected(true);
            list_colors.setAdapter(new Adapter_Colors(mAct, colors));
        }

        stockPriceId = getStockPriceId(0);
        stockColorPriceId = getStockColorPriceId(0);
        shopToServiceNameId = getShopToServiceNameId();
    }

    private int getShopToServiceNameId() {
        try {
            if (serviceJson != null) {
                return serviceJson
                        .getJSONObject("Data")
                        .getInt("shopToServiceNameId");
            }
        } catch (Exception e) {
            Log.e("error ShopToServiceName", "" + e.getMessage());
        }
        return -1;
    }

    private int getStockColorPriceId(int index) {
        try {
            if (productJson != null) {
                return productJson
                        .getJSONObject("Data")
                        .getJSONArray("shopProductStock")
                        .getJSONObject(index)
                        .getInt("stockColorPriceId");
            }
        } catch (Exception e) {

        }
        return -1;
    }

    private int getStockPriceId(int index) {
        try {
            if (productJson != null) {
                return productJson
                        .getJSONObject("Data")
                        .getJSONArray("shopProductStock")
                        .getJSONObject(index)
                        .getInt("stockPriceId");
            }
        } catch (Exception e) {
            Log.e("error getStockPriceId", " " + e.getMessage());
        }
        return -1;
    }

    private void displayPrice(int price, int discount) {
        if (discount > 0) {
//            int final_price = item.getPrice() - item.getDiscount();
            String p = String.format(Locale.ENGLISH, "%,d",
                    price);
            txt_price.setText(p, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) txt_price.getText();
            int l = (p).length();
            spannable.setSpan(new StrikethroughSpan(), 0,
                    l, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            txt_price.setText("");
        }
        String p = String.format(Locale.ENGLISH, "%,d\n%s",
                (price - discount), mAct.getString(R.string.currency));
        txt_final_price.setText(p);
    }

    private ArrayList<Color> getColors() {
        ArrayList<Color> colors = new ArrayList<>();
        try {
            if (productJson != null && productJson.getJSONObject("Data").has("shopProductStock")) {
                JSONArray jsonArray = productJson.getJSONObject("Data").getJSONArray("shopProductStock");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Color color = new Color();
                    JSONObject object = jsonArray.getJSONObject(i);
                    color.setColorId(object.getInt("productColorId"));
                    color.setColorName(object.getString("colorName"));
                    color.setColorCode(object.getString("colorCodeNumber"));

                    colors.add(color);
                }
            }
        } catch (Exception e) {
            Log.e("error getColors", " " + e.getMessage());
        }
        return colors;
    }

    private boolean isProductColored() {
        try {
            if (productJson != null)
                return productJson.getJSONObject("Data").has("productModelColors");
        } catch (Exception e) {
            Log.e("error color", " " + e.getMessage());
        }
        return false;
    }

    private int getDiscount() {
        try {
            if (productJson != null) {
                if (productJson.getJSONObject("Data").has("shopProductStock")) {
                    return productJson.getJSONObject("Data").getJSONArray("shopProductStock").getJSONObject(0).getInt("discount");
                }
            } else if (serviceJson != null) {
                if (serviceJson.getJSONObject("Data").has("serviceDiscount")) {
                    return serviceJson.getJSONObject("Data").getInt("serviceDiscount");
                }
            }
        } catch (Exception e) {
            Log.e("Error", " " + e.getMessage());
        }
        return 0;
    }

    private int getPrice() {
        try {
            if (productJson != null) {
                if (productJson.getJSONObject("Data").has("shopProductStock")) {
                    return productJson.getJSONObject("Data").getJSONArray("shopProductStock").getJSONObject(0).getInt("price");
                }
            } else if (serviceJson != null) {
                if (serviceJson.getJSONObject("Data").has("servicePrice")) {
                    return serviceJson.getJSONObject("Data").getInt("servicePrice");
                }
            }
        } catch (Exception e) {
            Log.e("Error", " " + e.getMessage());
        }
        return 0;
    }

    private String getProductBrandTitle() {
        try {
            if (productJson != null)
                return productJson.getJSONObject("Data").getJSONObject("productBrand").getString("brandName");
        } catch (Exception e) {

        }
        return "";
    }

    private String getProductAndServiceCategory() {
        try {
            if (productJson != null)
                return productJson.getJSONObject("Data").getString("categoryProductAndServiceTitle");
            if (serviceJson != null)
                return serviceJson.getJSONObject("Data").getString("categoryProductAndServiceTitle");
        } catch (Exception e) {

        }
        return "";
    }

    private String getProductAndServiceDescription() {
        try {
            if (productJson != null)
                return productJson.getJSONObject("Data").getString("productDefaultDesc");
            if (serviceJson != null)
                return serviceJson.getJSONObject("Data").getString("serviceDefaultDesc");
        } catch (Exception e) {

        }
        return "";
    }

    private String getShopDescription() {
        try {
            if (productJson != null) {
                if (productJson.getJSONObject("Data").has("shopProductSpecificDesc")) {
                    return productJson.getJSONObject("Data").getString("shopProductSpecificDesc");
                }
            } else if (serviceJson != null) {
                if (serviceJson.getJSONObject("Data").has("shopServiceSpecificDesc")) {
                    return serviceJson.getJSONObject("Data").getString("shopServiceSpecificDesc");
                }
            }
        } catch (Exception e) {
            Log.e("Error", " " + e.getMessage());
        }
        return null;
    }

    private String getProductAndServiceSpecs() {
        try {
            if (productJson != null)
                return productJson.getJSONObject("Data").getString("productAttributes");
            if (serviceJson != null)
                return serviceJson.getJSONObject("Data").getString("serviceAttributes");
        } catch (Exception e) {

        }
        return "";
    }

    private String getProductAndServiceTitle() {
        try {
            if (productJson != null)
                return productJson.getJSONObject("Data").getString("productModelName");
            if (serviceJson != null)
                return serviceJson.getJSONObject("Data").getString("serviceName");
        } catch (Exception e) {

        }
        return "";
    }

    private void setupSlider() {
        SliderLayout mHeader = (SliderLayout) findViewById(R.id.slider);
        mHeader.setPresetTransformer(SliderLayout.Transformer.Default);
        mHeader.setPresetIndicator(com.daimajia.slider.library.SliderLayout.PresetIndicators.Center_Bottom);

        mHeader.setCustomAnimation(new DescriptionAnimation());
//        Log.e("images", shopInfo.getImage_urls().size() + "");

        ArrayList<String> images = getProductAndServiceImages();
        if (images != null && images.size() > 0) {

            for (String s : images) {
                Log.e("shop img url", s);
                TextSliderView textSliderView = new TextSliderView(mAct);
                textSliderView
                        .image(s)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {

                            }
                        });

                mHeader.addSlider(textSliderView);
            }
        } else {
            mHeader.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            mHeader.setEnabled(false);
            CustomSliderView customSliderView = new CustomSliderView(mAct);

            customSliderView
                    .image(R.drawable.placeholder_shop)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });

            mHeader.addSlider(customSliderView);
        }

    }

    private ArrayList<String> getProductAndServiceImages() {
        try {
            JSONObject Data;

            JSONArray images = null;
            String images_base_url = "";
            if (productJson != null) {
                Data = productJson.getJSONObject("Data");
                if (Data.has("shopProductSpecificPictures")) {
                    images = Data.getJSONArray("shopProductSpecificPictures");
                    images_base_url = Data.getString("shopProductSpecificPicturesUrl");
                } else {
                    images = Data.getJSONArray("productDefaultPictures");
                    images_base_url = Data.getString("productDefaultPicturesUrl");
                }
            } else if (serviceJson != null) {
                Data = serviceJson.getJSONObject("Data");
                if (Data.has("shopServiceSpecificPictures")) {
                    images = Data.getJSONArray("shopServiceSpecificPictures");
                    images_base_url = Data.getString("shopServiceSpecificPicturesUrl");
                } else {
                    images = Data.getJSONArray("serviceDefaultPictures");
                    images_base_url = Data.getString("serviceDefaultPicturesUrl");
                }
            }
            ArrayList<String> image_list = new ArrayList<>();
            if (images != null)
                for (int i = 0; i < images.length(); i++) {
                    JSONObject img = images.getJSONObject(i);
                    String s = img.getString("picture");
                    image_list.add(images_base_url + s);
                }
            return image_list;
        } catch (Exception e) {
            Log.e("error", " " + e.getMessage());
        }
        return null;
    }

    public void updatePrice(Color item, int position) {
        try {
            if (productJson != null) {
                JSONObject Data = productJson.getJSONObject("Data");
                JSONObject product = Data
                        .getJSONArray("shopProductStock")
                        .getJSONObject(position);

                int price = product.getInt("price");
                int discount = product.getInt("discount");
                if (discount > 0 && discount < 100) {
                    discount = price * discount / 100;
                }

                displayPrice(price, discount);
            }

            stockColorPriceId = getStockColorPriceId(position);

        } catch (Exception e) {
            Log.e("error updatePrice", " " + e.getMessage());
        }
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
}
