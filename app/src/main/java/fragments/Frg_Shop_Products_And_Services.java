package fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.tnt.ibazaar.Act_Filter;
import com.tnt.ibazaar.Act_Full_List_Products_Or_Services;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.Adapter_Products_Or_Services;
import adapters.Adapter_Products_Or_Services_Full;
import models.Product;
import models.ProductCategory;
import models.Service;
import network.webconnection.WebConnection;
import views.customfab.CustomFloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Shop_Products_And_Services extends Fragment {

    private final String ORDER_NEW = "new";
    private final String ORDER_VISIT_COUNT = "visitCount";
    private final String ORDER_SELL_COUNT = "sellCount";
    private final String ORDER_PRICE_LESS_TO_HIGH = "priceLessToHigh";
    private final String ORDER_PRICE_HIGH_TO_LESS = "priceHighToLess";
    private Act_Shop mAct;
    private int isService = -1; //-1: unknown, 1: is Service, 0: is Product
    private RecyclerView list_products_or_services;
    private ImageView img_new;
    private ImageView img_visitCount;
    private ImageView img_sellCount;
    private ImageView img_priceLessToHigh;
    private ImageView img_priceHighToLess;
    private String previous_order = ORDER_NEW;

    private int category;

    public Frg_Shop_Products_And_Services() {
        // Required empty public constructor
    }

    public static Frg_Shop_Products_And_Services getInstance(int isService) {
        Frg_Shop_Products_And_Services fragment = new Frg_Shop_Products_And_Services();
        Bundle bundle = new Bundle();
        bundle.putInt("isService", isService);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isService = getArguments().getInt("isService");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAct = (Act_Shop) getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg__shop__products_and_services, container, false);

        RecyclerView list_sales = (RecyclerView) view.findViewById(R.id.list_special_offers);
        TextView txt_sales = (TextView) view.findViewById(R.id.txt_sales);
        list_sales.setLayoutManager(
                new LinearLayoutManager(mAct, LinearLayoutManager.HORIZONTAL, true));

        img_new = (ImageView) view.findViewById(R.id.img_new);
        img_visitCount = (ImageView) view.findViewById(R.id.img_visitCount);
        img_sellCount = (ImageView) view.findViewById(R.id.img_sellCount);
        img_priceHighToLess = (ImageView) view.findViewById(R.id.img_priceHighToLess);
        img_priceLessToHigh = (ImageView) view.findViewById(R.id.img_priceLessToHigh);

        if (isService == 0) {
            ArrayList<Product> salesProducts = mAct.getSalesProducts();

            if (salesProducts != null)
                list_sales.setAdapter(new Adapter_Products_Or_Services(
                        mAct, salesProducts, null, isService, mAct.getShopId()));
            else
                txt_sales.setVisibility(View.GONE);

        } else if (isService == 1) {
            ArrayList<Service> salesServices = mAct.getSalesServices();

            if (salesServices != null)
                list_sales.setAdapter(new Adapter_Products_Or_Services(
                        mAct, null, salesServices, isService, mAct.getShopId()));
            else
                txt_sales.setVisibility(View.GONE);
        }


        list_products_or_services =
                (RecyclerView) view.findViewById(R.id.list_products_or_services);
        list_products_or_services.setLayoutManager(new LinearLayoutManager(mAct));
        list_products_or_services.setNestedScrollingEnabled(false);

        final CustomFloatingActionButton fab = (CustomFloatingActionButton)
                view.findViewById(R.id.fab_filter);
        NestedScrollView nested = (NestedScrollView) view.findViewById(R.id.nested);

        fab.setImageDrawable(new IconicsDrawable(mAct,
                CommunityMaterial.Icon.cmd_filter_outline)
                .color(Color.WHITE)
                .sizeDp(20));

        nested.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int dy, int oldScrollX, int oldDy) {
                if (dy > oldDy) {
                    fab.hide();
                } else if (dy < oldDy) {
                    fab.show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Filter.class));
                try {
                    mAct.overridePendingTransition(R.anim.slide_in, R.anim.slide_in);
                } catch (Exception e) {
                    Log.e("eror", " " + e.getMessage());
                }
            }
        });


        TextView txt_products_or_services = (TextView) view.findViewById(R.id.txt_products_or_services);
        if (isService == 1) {
            txt_products_or_services.setText(R.string.Service);
        } else if (isService == 0) {
            txt_products_or_services.setText(R.string.products);
        }

        final AppCompatSpinner spnr_category_level_0 = (AppCompatSpinner) view.findViewById(R.id.spnr_category_level_0);
        final AppCompatSpinner spnr_category_level_1 = (AppCompatSpinner) view.findViewById(R.id.spnr_category_level_1);
        Application.showProgressDialog(spnr_category_level_0.getContext());
        final Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("shopId", "" + mAct.getShopId());
        Log.e("shopId", mAct.getShopId() + "");
        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Application.dismissProgressDialog();
                if (result == null) {
                    Toast.makeText(mAct, R.string.check_net, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("Status") == 100) {
                        JSONArray Data = object.getJSONArray("Data");
                        ArrayList<ProductCategory> categories = new ArrayList<>();
                        ProductCategory category = new ProductCategory();
                        if (isService == 0)
                            category.setTitle(getString(R.string.select_product_category));
                        else if (isService == 1)
                            category.setTitle(getString(R.string.select_service_category));
                        category.setId(-1);
                        categories.add(category);
                        for (int i = 0; i < Data.length(); i++) {
                            JSONObject catObject = Data.getJSONObject(i);
                            ProductCategory cat = new ProductCategory();
                            cat.setId(catObject.getInt("categoryProductAndServiceLevel0Id"));
                            cat.setTitle(catObject.getString("title"));
                            if (catObject.getInt("isService") == isService)
                                categories.add(cat);
                        }
                        ArrayAdapter<ProductCategory> adapter = new ArrayAdapter<>(
                                mAct, android.R.layout.simple_dropdown_item_1line, categories);
                        spnr_category_level_0.setAdapter(adapter);


                        spnr_category_level_0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ProductCategory category = (ProductCategory) spnr_category_level_0.getSelectedItem();
                                Application.showProgressDialog(spnr_category_level_0.getContext());
                                Uri.Builder builder = new Uri.Builder()
                                        .appendQueryParameter("parentId", "" + category.getId());
                                new WebConnection(new WebConnection.ConnectionResponse() {
                                    @Override
                                    public void connectionFinish(String result, String urlCanConnect) {
                                        Application.dismissProgressDialog();
                                        if (result == null) {
                                            Toast.makeText(mAct, R.string.check_net, Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        try {
                                            JSONObject object = new JSONObject(result);
                                            if (object.getInt("Status") == 100) {
                                                JSONArray Data = object.getJSONArray("Data");
                                                ArrayList<ProductCategory> categories = new ArrayList<>();
                                                ProductCategory category = new ProductCategory();
                                                if (isService == 0)
                                                    category.setTitle(getString(R.string.select_product_category));
                                                else if (isService == 1)
                                                    category.setTitle(getString(R.string.select_service_category));
                                                category.setId(-1);
                                                categories.add(category);
                                                for (int i = 0; i < Data.length(); i++) {
                                                    JSONObject catObject = Data.getJSONObject(i);
                                                    ProductCategory cat = new ProductCategory();
                                                    cat.setId(catObject.getInt("categoryProductAndServiceLevel1Id"));
                                                    cat.setParent(0);
                                                    cat.setTitle(catObject.getString("title"));
                                                    categories.add(cat);
                                                }
                                                ArrayAdapter<ProductCategory> adapter = new ArrayAdapter<>(
                                                        mAct, android.R.layout.simple_dropdown_item_1line, categories);
                                                spnr_category_level_1.setAdapter(adapter);
                                            } else {
                                                Toast.makeText(mAct, object.getString("Status_Str"), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            Log.e("error cat level 1", " " + e.getMessage());
                                        }
                                    }
                                }).connect(builder, "fetchCategoryProductAndServiceLevel1ByParentId", "GET", 1);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        Toast.makeText(mAct, object.getString("Status_Str"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("error", " " + e.getMessage());
                }
            }
        }).connect(builder, "fetchCategoryProductAndServiceLevel0ByShopId", "GET", 1);

        spnr_category_level_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                ProductCategory cat = (ProductCategory) spnr_category_level_1.getSelectedItem();
                category = cat.getId();

                fetchProductsByCategory(ORDER_NEW);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AppCompatButton btn_full_list = (AppCompatButton) view.findViewById(R.id.btn_full_list);
        btn_full_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Full_List_Products_Or_Services.class)
                        .putExtra("isService", isService)
                        .putExtra("shopId", mAct.getShopId());
                startActivity(intent);
            }
        });

        img_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchProductsByCategory(ORDER_NEW);
            }
        });

        img_priceHighToLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchProductsByCategory(ORDER_PRICE_HIGH_TO_LESS);
            }
        });

        img_visitCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchProductsByCategory(ORDER_VISIT_COUNT);
            }
        });

        img_priceLessToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchProductsByCategory(ORDER_PRICE_LESS_TO_HIGH);
            }
        });

        img_sellCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchProductsByCategory(ORDER_SELL_COUNT);
            }
        });
        return view;


    }

    private void fetchProductsByCategory(String OrderBy) {
        Application.showProgressDialog(mAct);
        Uri.Builder builder1 = new Uri.Builder()
                .appendQueryParameter("categoryProductAndServiceLevel1Id",
                        category + "")
                .appendQueryParameter("shopId", "" + mAct.getShopId())
                .appendQueryParameter("orderBy", OrderBy)
                .appendQueryParameter("isFetchProduct", isService == 1 ? "0" : "1");

        changeIconColor(OrderBy);

        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Application.dismissProgressDialog();
                try {
                    if (isService == 1) {
                        ArrayList<Service> services = new ArrayList<>();
                        JSONObject object = new JSONObject(result);
                        JSONArray Data = object.getJSONArray("Data");
                        for (int i = 0; i < Data.length(); i++) {
                            JSONObject serviceObject = Data.getJSONObject(i);
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
                        list_products_or_services.setAdapter(new Adapter_Products_Or_Services_Full(
                                mAct, null, services, isService, mAct.getShopId()));
                    } else if (isService == 0) {
                        ArrayList<Product> products = new ArrayList<>();
                        JSONObject object = new JSONObject(result);
                        JSONArray Data = object.getJSONArray("Data");
                        for (int i = 0; i < Data.length(); i++) {
                            Product product = new Product();
                            JSONObject productJson = Data.getJSONObject(i);
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
                        list_products_or_services.setAdapter(new Adapter_Products_Or_Services_Full(
                                mAct, products, null, isService, mAct.getShopId()));
                    }
                } catch (Exception e) {
                    Log.e("er ProductsByCategory", " " + e.getMessage());
                }
            }
        }).connect(builder1, "fetchProductAndServiceByCategoryProductAndServiceLevel1Id", "GET", 0);
    }

    private void changeIconColor(String orderBy) {
        switch (previous_order) {
            case ORDER_NEW:
                img_new.setImageDrawable(new IconicsDrawable(mAct,
                        FoundationIcons.Icon.fou_burst_new)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            case ORDER_VISIT_COUNT:
                img_visitCount.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_visibility)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            case ORDER_SELL_COUNT:
                img_sellCount.setColorFilter(Color.BLACK);
                break;
            case ORDER_PRICE_LESS_TO_HIGH:
                img_priceLessToHigh.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_trending_up)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            case ORDER_PRICE_HIGH_TO_LESS:
                img_priceHighToLess.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_trending_down)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
        }

        switch (orderBy) {
            case ORDER_NEW:
                img_new.setImageDrawable(new IconicsDrawable(mAct,
                        FoundationIcons.Icon.fou_burst_new)
                        .color(getResources().getColor(R.color.md_light_blue_A700))
                        .sizeDp(20));
                break;
            case ORDER_VISIT_COUNT:
                img_visitCount.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_visibility)
                        .color(getResources().getColor(R.color.md_light_blue_A700))
                        .sizeDp(20));
                break;
            case ORDER_SELL_COUNT:
                img_sellCount.setColorFilter(getResources().getColor(R.color.md_light_blue_A700));
                break;
            case ORDER_PRICE_LESS_TO_HIGH:
                img_priceLessToHigh.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_trending_up)
                        .color(getResources().getColor(R.color.md_light_blue_A700))
                        .sizeDp(20));
                break;
            case ORDER_PRICE_HIGH_TO_LESS:
                img_priceHighToLess.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_trending_down)
                        .color(getResources().getColor(R.color.md_light_blue_A700))
                        .sizeDp(20));
                break;
        }

        previous_order = orderBy;
    }


}
