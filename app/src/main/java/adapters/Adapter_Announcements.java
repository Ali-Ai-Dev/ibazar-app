package adapters;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import models.shop.Announcement;
import persiancalendar.JalaliCalendar;

/**
 * Created by Omid on 4/26/2018.
 */

public class Adapter_Announcements extends RecyclerView.Adapter<Adapter_Announcements.VH> {

    private Act_Shop mAct;
    private ArrayList<Announcement> data;

    public Adapter_Announcements(Act_Shop mAct, ArrayList<Announcement> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(
                R.layout.item_announcement, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Announcement item = data.get(position);
        if (item.getIcon_url() != null && !item.getIcon_url().isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
            String url =
                    prefs.getString("Download_IP", "") +
//                            ":" +
//                            prefs.getInt("Download_Port", 6000) +
                            "/" +
                            prefs.getString("Download_Folder_Shop", "") + "/" +
                            item.getIcon_url();

            Picasso.with(mAct)
                    .load(url)
                    .placeholder(R.drawable.placeholder_shop)
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.placeholder_shop);
        }

        holder.img_icon.setImageDrawable(new IconicsDrawable(mAct,
                GoogleMaterial.Icon.gmd_info_outline)
                .sizeDp(20)
                .color(Color.GRAY));
        holder.txt_title.setText(item.getTitle());
//        Log.e("received milis", item.getDate_time());

        holder.txt_date.setText(
                getDate(Long.parseLong(item.getDate_time()))
        );

        holder.txt_message.setText(item.getMessage());
    }

    private String getDate(long milliSeconds) {
        Date date = new java.util.Date(milliSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return JalaliCalendar.gregorianToJalali(sdf.format(date));
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txt_title;
        TextView txt_date;
        TextView txt_message;
        ImageView img_icon;

        public VH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_message = (TextView) itemView.findViewById(R.id.txt_message);
            img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }
}
