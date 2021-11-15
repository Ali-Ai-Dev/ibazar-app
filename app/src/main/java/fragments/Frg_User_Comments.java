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

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import adapters.Adapter_User_Comments;
import models.UserComment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_User_Comments extends Fragment {

    private Act_Shop mAct;

    public Frg_User_Comments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAct = (Act_Shop) getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg__user__comments, container, false);


        ArrayList<UserComment> userComments = mAct.getUserComments();


        if (userComments == null) {
            LinearLayout layout_no_comments =
                    (LinearLayout) view.findViewById(R.id.layout_no_comments);
            layout_no_comments.setVisibility(View.VISIBLE);
            ImageView img_no_comments = (ImageView) view.findViewById(R.id.img_no_comments);
            img_no_comments.setImageDrawable(new IconicsDrawable(mAct,
                    GoogleMaterial.Icon.gmd_comment)
                    .color(Color.GRAY)
                    .sizeDp(30));
        } else {
            RecyclerView list_comments = (RecyclerView) view.findViewById(
                    R.id.list_comments);
            list_comments.setLayoutManager(new LinearLayoutManager(mAct));
            list_comments.setAdapter(new Adapter_User_Comments(mAct, userComments));

            ArcProgress score1 = (ArcProgress) view.findViewById(R.id.score1);
            score1.setMax(100);
            int p1 = 0;
            for (int i = 0; i < userComments.size(); i++) {
                p1 += userComments.get(i).getP1();
            }

            try {
                p1 /= userComments.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            score1.setProgress(p1);
            ArcProgress score2 = (ArcProgress) view.findViewById(R.id.score2);
            score2.setMax(100);
            int p2 = 0;
            for (int i = 0; i < userComments.size(); i++) {
                p2 += userComments.get(i).getP2();
            }
            try {
                p2 /= userComments.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            score2.setProgress(p2);
            ArcProgress score3 = (ArcProgress) view.findViewById(R.id.score3);
            score3.setMax(100);
            int p3 = 0;
            for (int i = 0; i < userComments.size(); i++) {
                p3 += userComments.get(i).getP3();
            }
            try {
                p3 /= userComments.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            score3.setProgress(p3);


        }
        return view;
    }

}
