package adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.Act_Shops_List;
import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Category;
import models.shop.Shop;

/**
 * Created by Omid on 3/26/2018.
 */

public class Adapter_Shops extends RecyclerView.Adapter<Adapter_Shops.VH> {

    private Activity mAct;
    private ArrayList<Shop> data;

    public Adapter_Shops(Activity mAct, ArrayList<Shop> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final Shop item = data.get(position);
        try {
            if (item.getImg_main() != null && !item.getImg_main().isEmpty()) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
                String url = prefs.getString("Download_IP", "") +
//                        ":" +
//                        prefs.getInt("Download_Port", 6000) + "/" +
                        prefs.getString("Download_Folder_Shop", "") + "/" +
                        item.getImg_main();
//                Log.e("shop icon", url);
                Picasso.with(mAct)
                        .load(url)
                        .placeholder(R.drawable.placeholder)
                        .into(holder.img_shop);
            } else {
                holder.img_shop.setImageResource(R.drawable.placeholder);
                Log.e("no image", "s");
            }
        } catch (Exception e) {
            Log.e("img error", " " + e.getMessage());
        }
        holder.txt_title.setText(item.getTitle());
        switch (item.getMedal()) {
            case 1:
                holder.img_medal.setImageResource(R.drawable.ic_gold_medal);
                break;
            case 2:
                holder.img_medal.setImageResource(R.drawable.ic_silver_medal);
                break;
            case 3:
                holder.img_medal.setImageResource(R.drawable.ic_bronze_medal);
                break;
        }
//        Category cat = DataBase.selectCategory(item.getCat_id());
        Category cat = Application.getCategory(item.getCat_id());

        if (cat != null)
            holder.txt_cat.setText(cat.getTitle());

        holder.rate.setRating(item.getScore());
        holder.txt_address.setText(item.getAddress());

//        if (position == data.size() - 25) {
////            Log.e("req", "next data");
//            if (mAct instanceof Act_Shops_List)
//                ((Act_Shops_List) mAct).getNextShops(position,
//                        data.get(data.size() - 1).getId());
//        }
        Log.e("1", "" + position);
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Shop.class);
                intent.putExtra("shop_id", item.getId());
                intent.putExtra("shop_title", item.getTitle());
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

    public void addData(ArrayList<Shop> data) {
        int start = this.data.size() - 1;
        this.data.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView img_shop;
        private TextView txt_title;
        private TextView txt_cat;
        private RatingBar rate;
        private TextView txt_address;
        private ImageView img_medal;
        private View click;

        public VH(View itemView) {
            super(itemView);
            img_shop = (ImageView) itemView.findViewById(R.id.img_shop);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_cat = (TextView) itemView.findViewById(R.id.txt_cat);
            rate = (RatingBar) itemView.findViewById(R.id.rate);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            img_medal = (ImageView) itemView.findViewById(R.id.img_medal);
            click = itemView;
        }
    }
}
