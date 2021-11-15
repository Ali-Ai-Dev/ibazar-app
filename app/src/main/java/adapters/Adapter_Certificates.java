package adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Image;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Certificate;

/**
 * Created by Omid on 4/30/2018.
 */

public class Adapter_Certificates extends RecyclerView.Adapter<Adapter_Certificates.VH> {

    private Act_Shop mAct;
    private ArrayList<Certificate> data;

    public Adapter_Certificates(Act_Shop mAct, ArrayList<Certificate> data) {
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
        Certificate item = data.get(position);
        holder.txt_name.setText(item.getTitle());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
        final String url = prefs.getString("Download_IP", "") +
//                ":" +
//                prefs.getInt("Download_Port", 6000) + "/" +
                prefs.getString("Download_Folder_Shop", "") + "/" +
                item.getImg_name();
        Picasso.with(mAct)
                .load(url)
                .placeholder(R.drawable.placeholder_shop)
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Image.class);
                intent.putExtra("url", url);
                mAct.startActivity(intent);
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
        private TextView txt_name;
        private ImageView img;

        public VH(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_staff_name);
            img = (ImageView) itemView.findViewById(R.id.img_staff);
        }
    }
}
