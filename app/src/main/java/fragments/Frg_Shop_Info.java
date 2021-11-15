package fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Filter;
import com.tnt.ibazaar.Act_Image;
import com.tnt.ibazaar.Act_Other_Problems;
import com.tnt.ibazaar.Act_Report;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import adapters.Adapter_Certificates;
import adapters.Adapter_Staff;
import database.DataBase;
import models.ReportItem;
import models.shop.ShopInfo;
import persiancalendar.JalaliCalendar;
import views.CustomSliderView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Shop_Info extends Fragment {

    private ShopInfo shopInfo;

    private Act_Shop mAct;

    public Frg_Shop_Info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.frg__shop__info, container, false);
        mAct = (Act_Shop) getActivity();
        shopInfo = mAct.getShopInfo();
        setupSlider(view);
        ImageView img_shop = (ImageView) view.findViewById(R.id.img_shop);
        if (shopInfo.getIcon_url() != null && !shopInfo.getIcon_url().isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
            String url = prefs.getString("Download_IP", "") +
//                    ":" +
//                    prefs.getInt("Download_Port", 6000) + "/" +
                    prefs.getString("Download_Folder_Shop", "") + "/" +
                    shopInfo.getIcon_url();
            Picasso.with(mAct)
                    .load(url)
                    .placeholder(R.drawable.placeholder_shop)
                    .into(img_shop);
        }

        ImageView img_share = (ImageView) view.findViewById(R.id.img_share);
        img_share.setImageDrawable(
                new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_share)
                        .color(Color.GRAY)
                        .sizeDp(20));

        final ImageView img_bookmark = (ImageView) view.findViewById(R.id.img_bookmark);
        if (!DataBase.isShopBookmarked(mAct.getShopId())) {
            img_bookmark.setImageDrawable(
                    new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_bookmark_border)
                            .color(Color.GRAY)
                            .sizeDp(20));
        } else {
            img_bookmark.setImageDrawable(
                    new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_bookmark)
                            .color(Color.GRAY)
                            .sizeDp(20));
        }
        img_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DataBase.isShopBookmarked(mAct.getShopId())) {
                    img_bookmark.setImageDrawable(
                            new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_bookmark)
                                    .color(Color.GRAY)
                                    .sizeDp(20));
                    mAct.bookmark(true);
                } else {
                    img_bookmark.setImageDrawable(
                            new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_bookmark_border)
                                    .color(Color.GRAY)
                                    .sizeDp(20));
                    mAct.bookmark(false);
                }
            }
        });

        final ImageView img_like = (ImageView) view.findViewById(R.id.img_like);
        img_like.setImageDrawable(
                new IconicsDrawable(mAct, CommunityMaterial.Icon.cmd_heart)
                        .color(Color.GRAY)
                        .sizeDp(20));
        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_like.setImageDrawable(
                        new IconicsDrawable(mAct, CommunityMaterial.Icon.cmd_heart)
                                .color(Color.RED)
                                .sizeDp(20));
            }
        });
        TextView txt_shop_title = (TextView) view.findViewById(R.id.txt_shop_title);
        txt_shop_title.setText(shopInfo.getName());

        ExpandableTextView expand_text_view =
                (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        expand_text_view.setText(shopInfo.getService_description());

        TextView txt_manager_name = (TextView) view.findViewById(R.id.txt_manager_name);
        txt_manager_name.setText(shopInfo.getAdmin_name());

        TextView txt_phone_number = (TextView) view.findViewById(R.id.txt_phone_number);
        txt_phone_number.setText(shopInfo.getPhone());

        TextView txt_website = (TextView) view.findViewById(R.id.txt_website);
        txt_website.setText(shopInfo.getWebsite());

        TextView txt_email = (TextView) view.findViewById(R.id.txt_email);
        txt_email.setText(shopInfo.getEmail());

        TextView txt_address = (TextView) view.findViewById(R.id.txt_address);
        txt_address.setText(shopInfo.getAddress());

        TextView txt_register_date = (TextView) view.findViewById(R.id.txt_register_date);
        txt_register_date.setText(getDate(shopInfo.getRegister_date()));

        TextView txt_guild_type = (TextView) view.findViewById(R.id.txt_guild_type);
        switch (shopInfo.getGuild_type()) {
            case "1":
                txt_guild_type.setText(R.string.Organizational);
                break;
            case "2":
                txt_guild_type.setText(R.string.Services);
                break;
        }
//        txt_guild_type.setText(shopInfo.getGuild_type());

        TextView txt_license_number = (TextView) view.findViewById(R.id.txt_license_number);
        txt_license_number.setText(shopInfo.getLicense_number());

        ImageView img_report = (ImageView) view.findViewById(R.id.img_report);
        img_report.setImageDrawable(new IconicsDrawable(mAct,
                GoogleMaterial.Icon.gmd_report).color(Color.GRAY).sizeDp(20));

        TextView txt_open = (TextView) view.findViewById(R.id.txt_open);
        if (shopInfo.isOpen()) {
            txt_open.setBackgroundResource(R.drawable.bg_open);
            txt_open.setText(R.string.open);
        } else {
            txt_open.setTextColor(Color.RED);
            txt_open.setText(R.string.closed);
        }

        ImageView img_medal = (ImageView) view.findViewById(R.id.img_medal);
        switch (shopInfo.getMedal()) {
            case 1:
                img_medal.setImageResource(R.drawable.ic_gold_medal);
                break;
            case 2:
                img_medal.setImageResource(R.drawable.ic_silver_medal);
                break;
            case 3:
                img_medal.setImageResource(R.drawable.ic_bronze_medal);
                break;
        }

        RatingBar rate = (RatingBar) view.findViewById(R.id.rate);
        rate.setRating(shopInfo.getScore());

        TextView txt_vote_count = (TextView) view.findViewById(R.id.txt_vote_count);
        txt_vote_count.setText(shopInfo.getScore() + " از " + shopInfo.getVote_count() + " رای");

        ImageView img_manager = (ImageView) view.findViewById(R.id.img_manager);
        if (shopInfo.getAdmin_image() != null && !shopInfo.getAdmin_image().isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
            final String url = prefs.getString("Download_IP", "") +
//                    ":" +
//                    prefs.getInt("Download_Port", 6000) + "/" +
                    prefs.getString("Download_Folder_Shop", "") + "/" +
                    shopInfo.getAdmin_image();
            Picasso.with(mAct)
                    .load(url)
                    .placeholder(R.drawable.placeholder_shop)
                    .into(img_manager);
            img_manager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mAct, Act_Image.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });
        }

        RecyclerView list_staff = (RecyclerView) view.findViewById(R.id.list_staff);
        list_staff.setLayoutManager(new LinearLayoutManager(mAct));
        list_staff.setAdapter(new Adapter_Staff(mAct, mAct.getStaff()));
        list_staff.setNestedScrollingEnabled(false);

        RecyclerView list_iso = (RecyclerView) view.findViewById(R.id.list_iso);
        list_iso.setLayoutManager(new LinearLayoutManager(mAct));
        list_iso.setAdapter(new Adapter_Certificates(mAct, mAct.getCertificates()));
        list_iso.setNestedScrollingEnabled(false);

        final TextView txt_report = (TextView) view.findViewById(R.id.txt_report);
        txt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Report.class));
                try {
                    mAct.overridePendingTransition(R.anim.slide_in, R.anim.slide_in);
                } catch (Exception e) {
                    Log.e("eror", " " + e.getMessage());
                }
            }

            private ArrayList<ReportItem> getReportItems() {
                ArrayList<ReportItem> data = new ArrayList<>();
                String[] items = getResources().getStringArray(R.array.report_items);
                for (String s : items) {
                    ReportItem item = new ReportItem();
                    item.setTitle(s);
                    item.setSelected(false);
                    data.add(item);
                }
                return data;
            }
        });
        img_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_report.performClick();
            }
        });
        return view;
    }

    private String getDate(long milliSeconds) {
        Date date = new java.util.Date(milliSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return JalaliCalendar.gregorianToJalali(sdf.format(date));
    }

    private void setupSlider(View view) {
        SliderLayout mHeader = (SliderLayout) view.findViewById(R.id.slider);
        mHeader.setPresetTransformer(SliderLayout.Transformer.Default);
        mHeader.setPresetIndicator(com.daimajia.slider.library.SliderLayout.PresetIndicators.Center_Bottom);

        mHeader.setCustomAnimation(new DescriptionAnimation());
//        Log.e("images", shopInfo.getImage_urls().size() + "");
        if (shopInfo.getImage_urls() != null && shopInfo.getImage_urls().size() > 0) {

            for (String s : shopInfo.getImage_urls()) {
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

}
