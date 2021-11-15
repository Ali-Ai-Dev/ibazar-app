package adapters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.security.PrivateKey;
import java.util.ArrayList;

import models.Staff;

/**
 * Created by Omid on 4/30/2018.
 */

public class Adapter_Staff extends RecyclerView.Adapter<Adapter_Staff.VH> {

    private Act_Shop mAct;
    private ArrayList<Staff> data;

    public Adapter_Staff(Act_Shop mAct, ArrayList<Staff> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater()
                .inflate(R.layout.item_staff, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Staff item = data.get(position);
        holder.txt_name.setText(item.getName() + "\n" + item.getPost());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
        String url = prefs.getString("Download_IP", "") +
//                ":" +
//                prefs.getInt("Download_Port", 6000) + "/" +
                prefs.getString("Download_Folder_Seller", "") + "/" +
                item.getImg_name();
        Picasso.with(mAct)
                .load(url)
                .placeholder(R.drawable.placeholder_shop)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class VH extends ViewHolder {
        private TextView txt_name;
        private ImageView img;

        public VH(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_staff_name);
            img = (ImageView) itemView.findViewById(R.id.img_staff);
        }
    }
}
