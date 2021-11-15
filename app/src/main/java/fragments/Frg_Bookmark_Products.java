package fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.tnt.ibazaar.Act_Bookmarks;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import adapters.Adapter_Products_Or_Services_Full;
import database.DataBase;
import models.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Bookmark_Products extends Fragment {

    private Act_Bookmarks mAct;

    public Frg_Bookmark_Products() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAct = (Act_Bookmarks) getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg__bookmarks, container, false);
        RecyclerView list_bookmarks = (RecyclerView) view.findViewById(R.id.list_bookmarks);
        list_bookmarks.setLayoutManager(new LinearLayoutManager(mAct));
        ArrayList<Product> data = DataBase.selectProducts();
        if (data == null || data.size() == 0) {
            LinearLayout layout_no_bookmarks = (LinearLayout) view.findViewById(R.id.layout_no_bookmarks);
            layout_no_bookmarks.setVisibility(View.VISIBLE);
            TextView txt_no_bookmark = (TextView) view.findViewById(R.id.txt_no_bookmark);
            txt_no_bookmark.setText(R.string.no_bookmark_products);
            ImageView img_no_bookmark = (ImageView) view.findViewById(R.id.img_no_bookmark);
            img_no_bookmark.setImageDrawable(new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_bookmark)
                    .color(Color.GRAY).sizeDp(30));
        } else {
//            list_bookmarks.setAdapter(new Adapter_Products_Or_Services_Full(mAct, data));
        }
        return view;
    }

}
