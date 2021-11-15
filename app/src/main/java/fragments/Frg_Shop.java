package fragments;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Main;
import com.tnt.ibazaar.R;

import org.json.JSONArray;
import org.json.JSONException;

import adapters.Adapter_Elevator;
import database.DataBase;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Shop extends Fragment {


    public Frg_Shop() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg_shop, container, false);
        final RecyclerView list_elevator = (RecyclerView) view.findViewById(R.id.list_elevator);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String motto =
                prefs.getString("motto", "");
        ImageView img_banner = (ImageView) view.findViewById(R.id.img_banner);

        try {
            JSONArray jsonArray = new JSONArray(motto);
            motto = jsonArray.getJSONObject(0).getString("ImageName");
        } catch (JSONException e) {
            Log.e("motto error", "" + e.getMessage());
        }
        if (!motto.isEmpty()) {
            motto =
                    prefs.getString("Download_IP", "") +
//                            ":" +
//                            prefs.getInt("Download_Port", 6000) + "/" +
                            prefs.getString("Download_Folder_Motto", "") + "/" +
                            motto;
            Log.e("motto", " " + motto);
            Picasso.with(getContext()).load(motto).into(img_banner, new Callback() {
                @Override
                public void onSuccess() {
                    list_elevator.scrollToPosition(0);
                }

                @Override
                public void onError() {

                }
            });
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        list_elevator.setLayoutManager(linearLayoutManager);
//        list_elevator.setAdapter(new Adapter_Elevator(
//                (Act_Main) getActivity(), Application.getCategoryChildren(0)));
        list_elevator.setAdapter(new Adapter_Elevator(
                (Act_Main) getActivity(), DataBase.selectCategories(0)));
        final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    list_elevator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    list_elevator.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                list_elevator.scrollToPosition(0);

            }
        };
        list_elevator.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        return view;
    }

}
