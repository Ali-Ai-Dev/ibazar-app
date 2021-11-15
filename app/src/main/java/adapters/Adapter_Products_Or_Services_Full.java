package adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Product_And_Service;
import com.tnt.ibazaar.R;

import java.util.ArrayList;
import java.util.Locale;

import models.Product;
import models.Service;

/**
 * Created by Omid on 4/28/2018.
 */

public class Adapter_Products_Or_Services_Full extends RecyclerView.Adapter<Adapter_Products_Or_Services_Full.VH> {

    private Activity mAct;
    private ArrayList<Product> products;
    private ArrayList<Service> services;
    private int isService = -1;
    private int shopId;

    public Adapter_Products_Or_Services_Full(
            Activity mAct, ArrayList<Product> products,
            ArrayList<Service> services, int isService, int shopId) {
        this.mAct = mAct;
        this.products = products;
        this.services = services;
        this.isService = isService;
        this.shopId = shopId;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(
                R.layout.item_product_full, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (isService == 0) {
            final Product item = products.get(position);
            if (item.getMain_image() != null && !item.getMain_image().isEmpty()) {
                Picasso.with(mAct)
                        .load(item.getMain_image())
                        .placeholder(R.drawable.placeholder_shop)
                        .into(holder.img);
            } else {
                holder.img.setImageResource(R.drawable.placeholder_shop);
            }

            holder.txt_title.setText(item.getTitle());

            if (item.getDiscount() > 0) {
//            int final_price = item.getPrice() - item.getDiscount();
                String p = String.format(Locale.ENGLISH, "%,d",
                        item.getPrice());
                holder.txt_price.setText(p, TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) holder.txt_price.getText();
                int l = (String.format(Locale.ENGLISH, "%,d",
                        item.getPrice())).length();
                spannable.setSpan(new StrikethroughSpan(), 0,
                        l, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            String p = String.format(Locale.ENGLISH, "%,d\n%s",
                    (item.getPrice() - item.getDiscount()), mAct.getString(R.string.currency));
            holder.txt_final_price.setText(p);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mAct, Act_Product_And_Service.class);
                    intent.putExtra("item", item);
                    intent.putExtra("shopId", shopId);
                    mAct.startActivity(intent);
                }
            });
        } else {
            final Service item = services.get(position);
            if (item.getIcon() != null && !item.getIcon().isEmpty()) {
                Picasso.with(mAct)
                        .load(item.getIcon())
                        .placeholder(R.drawable.placeholder_shop)
                        .into(holder.img);
            } else {
                holder.img.setImageResource(R.drawable.placeholder_shop);
            }

            holder.txt_title.setText(item.getTitle());

            if (item.getDiscount() > 0) {
//            int final_price = item.getPrice() - item.getDiscount();
                String p = String.format(Locale.ENGLISH, "%,d",
                        item.getPrice());
                holder.txt_price.setText(p, TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) holder.txt_price.getText();
                int l = (String.format(Locale.ENGLISH, "%,d",
                        item.getPrice())).length();
                spannable.setSpan(new StrikethroughSpan(), 0,
                        l, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            String p = String.format(Locale.ENGLISH, "%,d\n%s",
                    (item.getPrice() - item.getDiscount()), mAct.getString(R.string.currency));
            holder.txt_final_price.setText(p);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mAct, Act_Product_And_Service.class);
                    intent.putExtra("item", item);
                    intent.putExtra("isService", true);
                    intent.putExtra("shopId", shopId);
                    mAct.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isService == 0) {
            if (products == null)
                return 0;
            return products.size();
        } else if (isService == 1) {
            if (services == null)
                return 0;
            return services.size();
        }
        return 0;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView txt_title;
        TextView txt_price;
        TextView txt_final_price;
        ImageView img;

        public VH(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_final_price = (TextView) itemView.findViewById(R.id.txt_final_price);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
