package fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.tnt.ibazaar.Act_Main;
import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

import adapters.Adapter_Brands;
import adapters.Adapter_Brands_Categories;
import database.DataBase;
import tools.CallBack;
import views.LoadMapDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Product extends Fragment {
//    private int h;
//    public static Frg_Product newInstance(int h) {
//        Bundle bundle = new Bundle();
//        bundle.putInt("h", h);
//        Frg_Product frg_product = new Frg_Product();
//        frg_product.setArguments(bundle);
//        return frg_product;
//    }

    public Frg_Product() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            h = getArguments().getInt("h");
//        }
//    }

    private Act_Main mAct;

    private LatLng latLng = null;
    private int type = 1;

    private ImageView img_topest;
    private ImageView img_nearest;
    private ImageView img_new;
    private ImageView img_most_liked;
    private ImageView img_most_stared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAct = (Act_Main) getActivity();
        View view = inflater.inflate(R.layout.frg__product, container, false);
        RecyclerView list_cats = (RecyclerView) view.findViewById(R.id.list_cats);
        list_cats.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, true));
//        list_cats.setAdapter(
//                new Adapter_Brands_Categories(Application.getCategoryChildren(0), getActivity()));
        list_cats.setAdapter(
                new Adapter_Brands_Categories(DataBase.selectCategories(0), mAct));
        RecyclerView list_brands = (RecyclerView) view.findViewById(R.id.list_brands);
        list_brands.setLayoutManager(new GridLayoutManager(getContext(), 3));
        list_brands.setAdapter(new Adapter_Brands(mAct, Application.getBrands()));

        img_topest = (ImageView) view.findViewById(R.id.img_topest);
        img_topest.setImageDrawable(new IconicsDrawable(mAct,
                Entypo.Icon.ent_medal)
                .color(Color.BLACK)
                .sizeDp(20));

        img_nearest = (ImageView) view.findViewById(R.id.img_nearest);
        img_nearest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (latLng == null)
                    LoadMapDialog.loadMap(mAct, new CallBack() {
                        @Override
                        public void callback(LatLng latLng2) {
                            //get brands
                            latLng = latLng2;
                            img_nearest.setImageDrawable(new IconicsDrawable(mAct,
                                    GoogleMaterial.Icon.gmd_gps_fixed)
                                    .color(Color.parseColor("#0BA44A"))
                                    .sizeDp(20));
                        }
                    }, latLng);
                else {
                    latLng = null;
                    img_nearest.setImageDrawable(new IconicsDrawable(mAct,
                            GoogleMaterial.Icon.gmd_gps_fixed)
                            .color(Color.BLACK)
                            .sizeDp(20));
                }
            }
        });

        img_new = (ImageView) view.findViewById(R.id.img_new);
        img_most_liked = (ImageView) view.findViewById(R.id.img_most_liked);
        img_most_stared = (ImageView) view.findViewById(R.id.img_most_stared);


        img_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeColors(1);
                type = (1);

            }
        });

        img_most_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                changeColors(2);
                type = (2);
            }
        });

        img_topest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeColors(3);
                type = (3);
            }
        });

        img_most_stared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeColors(4);
                type = 4;
            }
        });
        return view;
    }

    private void changeColors(int i) {
        if (i == type)
            return;

        switch (i) {
            case 1:
                img_new.setImageDrawable(new IconicsDrawable(mAct,
                        FoundationIcons.Icon.fou_burst_new)
                        .color(Color.parseColor("#5FBDE8"))
                        .sizeDp(20));
                break;
            case 2:
                img_most_liked.setImageDrawable(new IconicsDrawable(mAct,
                        CommunityMaterial.Icon.cmd_heart)
                        .color(Color.parseColor("#F42630"))
                        .sizeDp(20));
                break;
            case 3:
                img_topest.setImageDrawable(new IconicsDrawable(mAct,
                        Entypo.Icon.ent_medal)
                        .color(Color.parseColor("#FF6600"))
                        .sizeDp(20));
                break;
            default:
                img_most_stared.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_star)
                        .color(Color.parseColor("#FFE391"))
                        .sizeDp(20));
        }

        switch (type) {
            case 1:
                img_new.setImageDrawable(new IconicsDrawable(mAct,
                        FoundationIcons.Icon.fou_burst_new)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            case 2:
                img_most_liked.setImageDrawable(new IconicsDrawable(mAct,
                        CommunityMaterial.Icon.cmd_heart)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            case 3:
                img_topest.setImageDrawable(new IconicsDrawable(mAct,
                        Entypo.Icon.ent_medal)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            default:
                img_most_stared.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_star)
                        .color(Color.BLACK)
                        .sizeDp(20));
        }

    }

}
