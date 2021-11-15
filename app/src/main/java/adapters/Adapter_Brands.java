package adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Brand;

/**
 * Created by Omid on 3/13/2018.
 */

public class Adapter_Brands extends RecyclerView.Adapter<Adapter_Brands.VH> {
    private Activity mAct;
    private ArrayList<Brand> data;

    public Adapter_Brands(Activity mAct, ArrayList<Brand> data) {
        this.mAct = mAct;
        this.data = data;
//        Log.e("brand size", "" + data.size());
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_brand, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Brand item = data.get(position);
        holder.txt.setText(item.getTitle());
        if (item.getImage_name() != null && !item.getImage_name().isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
            String url = prefs.getString("Download_IP", "") +
//                    ":" +
//                    prefs.getInt("Download_Port", 6000) + "/" +
                    prefs.getString("Download_Folder_Brand", "") + "/" +
                    item.getImage_name();
            Log.e("url brand", url);
            Picasso.with(mAct)
                    .load(url)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        //        private CircularImageView img;
        private ImageView img;
        private TextView txt;

        public VH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
