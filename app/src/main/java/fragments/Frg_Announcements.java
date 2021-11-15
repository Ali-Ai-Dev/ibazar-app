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
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import adapters.Adapter_Announcements;
import models.shop.Announcement;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Announcements extends Fragment {

    private Act_Shop mAct;

    public Frg_Announcements() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAct = (Act_Shop) getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg__announcements, container, false);
        RecyclerView list_announcements =
                (RecyclerView) view.findViewById(R.id.list_announcements);
        list_announcements.setLayoutManager(new LinearLayoutManager(mAct));
        ArrayList<Announcement> announcements = mAct.getAnnouncements();
        list_announcements.setAdapter(new Adapter_Announcements(mAct, announcements));
        if (announcements == null) {
            LinearLayout layout_no_announcements =
                    (LinearLayout) view.findViewById(R.id.layout_no_announcements);
            layout_no_announcements.setVisibility(View.VISIBLE);
            ImageView img_no_announcements = (ImageView) view.findViewById(R.id.img_no_announcements);
            img_no_announcements.setImageDrawable(new IconicsDrawable(mAct,
                    GoogleMaterial.Icon.gmd_announcement)
                    .color(Color.GRAY)
                    .sizeDp(30));
        }
        return view;
    }

}
