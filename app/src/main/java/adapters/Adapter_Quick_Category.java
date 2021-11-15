package adapters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Category;

/**
 * Created by Omid on 3/13/2018.
 */

public class Adapter_Quick_Category extends RecyclerView.Adapter<Adapter_Quick_Category.VH> {

    private Act_Shop mAct;
    private ArrayList<Category> data;

    public Adapter_Quick_Category(Act_Shop mAct, ArrayList<Category> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater()
                .inflate(R.layout.item_shop_cat_quick, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final Category item = data.get(position - 1);
        holder.txt_title.setText(item.getTitle());
        if (item.getImage_name() != null && !item.getImage_name().isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
            String url = prefs.getString("Download_IP", "") +
//                    ":" +
//                    prefs.getInt("Download_Port", 6000) + "/" +
                    prefs.getString("Download_Folder_Category", "") + "/" +
                    item.getImage_name();
            Log.e("url", url);
            Picasso.with(mAct)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img);
        } else {
            holder.img.setImageDrawable(null);
        }
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAct.loadQuickCategories(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView txt_title;
        private View click;

        public VH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            click = itemView;
        }
    }

}
