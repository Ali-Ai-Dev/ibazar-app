package fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.Dialog;
import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import database.DataBase;
import models.Category;
import models.ServerResponse;
import network.webconnection.WebConnection;
import tools.CustPagerTransformer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Category_Gallery extends Fragment {

    private TextView indicatorTv;
    private View positionView;
    private ViewPager viewPager;
    private List<Frg_Category> fragments = new ArrayList<>();
    private ArrayList<Category> data;
    private int id;
    private String title;

    public Frg_Category_Gallery() {
        // Required empty public constructor
    }

    public static Frg_Category_Gallery newInstance(int id, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("title", title);
        Frg_Category_Gallery frgCategoryGallery = new Frg_Category_Gallery();
        frgCategoryGallery.setArguments(bundle);
        return frgCategoryGallery;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            title = getArguments().getString("title");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.act__category__gallery, container, false);
        positionView = view.findViewById(R.id.position_view);

        dealStatusBar();

//        initImageLoader();

        indicatorTv = (TextView) view.findViewById(R.id.indicator_tv);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(title);
        txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        data = DataBase.selectCategories(id);
        Log.e("id", "" + id);
        if (data == null) {
            getCategories(getContext(), id);

        } else {
            Log.e("size", "" + data.size());
            fillViewPager();
        }
        return view;
    }

    private void getCategories(final Context context, int id) {

        Application.showProgressDialog(context);
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("parentCategoryId", "" + id);
        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Application.dismissProgressDialog();
                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                    if (response.getStatus() == 100) {
                        try {
                            JSONObject Data = jsonObject.getJSONObject("Data");
                            JSONArray jsonArray = Data.getJSONArray("Category");
                            Application.setCategoriesJsonString(
                                    Application.NormalizeString(
                                            jsonObject.getJSONObject("Data").getString("Category")));

                            ArrayList<Category> categories = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);
                                Category category = new Category();
                                category.setId(jsonObject.getInt("ID"));
                                category.setTitle(
                                        Application.NormalizeString(
                                                jsonObject.getString("Name")));
                                category.setParent(jsonObject.getInt("ParnetID"));
                                if (jsonObject.has("ImageName"))
                                    category.setImage_name(
                                            Application.NormalizeString(
                                                    jsonObject.getString("ImageName")));
                                categories.add(category);
                                DataBase.insertCategory(category);
                            }
//                Log.e("end of", "categories");
                            response.setData(categories);
                        } catch (JSONException e) {
                            Log.e("error", " " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.e("error getCategories", " " + e.getMessage());
                }
                if (response == null) {
                    Toast.makeText(context, R.string.not_connected_check_internet, Toast.LENGTH_SHORT).show();
                } else {
                    if (response.getData() != null) {
                        data = (ArrayList<Category>) response.getData();
                        fillViewPager();
                    }
                }
            }
        }).connect(builder, "fetchSpecialListFromCategory", "GET", 0);
    }

    private void fillViewPager() {

        viewPager.setPageTransformer(false, new CustPagerTransformer(getContext()));

        // 2. viewPager adapter
        if (data != null)
            for (Category category : data) {
                // fragment
                fragments.add(Frg_Category.newInstance(category));
            }

        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
//                CommonFragment fragment = fragments.get(position);
                Frg_Category fragment = fragments.get(position);
//                fragment.bindData(imageArray[position % imageArray.length]);
                return fragment;
            }

            @Override
            public int getCount() {
                if (data == null)
                    return 0;
                return data.size();
            }
        });

        // 3. viewPager滑动时，调整指示器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicatorTv();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateIndicatorTv();
    }

    private void updateIndicatorTv() {
        int totalNum = viewPager.getAdapter().getCount();
        int currentItem = viewPager.getCurrentItem() + 1;
        indicatorTv.setText(Html.fromHtml("<font color='#12edf0'>" + currentItem + "</font>  /  " + totalNum));
    }

    private void dealStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);
        }
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
