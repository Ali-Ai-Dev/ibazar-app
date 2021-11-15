package fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Main;
import com.tnt.ibazaar.Act_Shops_List;
import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

import database.DataBase;
import models.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Category extends Fragment {

    private Category category;

    public Frg_Category() {
        // Required empty public constructor
    }

    public static Frg_Category newInstance(Category category) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("cat", category);
        Frg_Category frg_category = new Frg_Category();
        frg_category.setArguments(bundle);
        return frg_category;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            category = getArguments().getParcelable("cat");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg__category, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = prefs.getString("Download_IP", "") +
//                ":" +
//                prefs.getInt("Download_Port", 6000) + "/" +
                prefs.getString("Download_Folder_Category", "") + "/" +
                category.getImage_name();
        if (category.getImage_name() != null && !category.getImage_name().isEmpty())
            Picasso.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);


        TextView address4 = (TextView) view.findViewById(R.id.address4);
        address4.setText(category.getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLevel3()) {
                    Intent intent = new Intent(getContext(), Act_Shops_List.class);
                    intent.putExtra("category", category.getId());
                    startActivity(intent);
                } else {
                    Application.setLevel(Application.getLevel() + 1);
                    ((Act_Main) getActivity()).loadCategory(category.getId(), category.getTitle());
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Application.setLevel(Application.getLevel() - 1);
    }

    private boolean isLevel3() {
//        int level = Application.getLevel();
//        Log.e("frg_category", "isLevel3");
//        return Application.getLevel() == 3;
        Category cat = DataBase.selectCategory(category.getParent());
        return cat != null && cat.getParent() != 0;

    }
}
