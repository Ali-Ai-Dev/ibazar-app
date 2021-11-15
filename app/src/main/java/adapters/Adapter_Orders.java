package adapters;

import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Basket;
import com.tnt.ibazaar.R;

import java.util.ArrayList;
import java.util.Locale;

import models.Order;

/**
 * Created by Omid on 9/12/2018.
 */

public class Adapter_Orders extends RecyclerView.Adapter<Adapter_Orders.VH> {


    private Act_Basket mAct;
    private ArrayList<Order> data;

    public Adapter_Orders(Act_Basket mAct, ArrayList<Order> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Order item = data.get(position);

        //if price has been changed, then show txt_price_changed.
        String title = "";
        if (item.getProduct() != null) {
            title = item.getProduct().getTitle();
        } else if (item.getService() != null) {
            title = item.getService().getTitle();
        }
        holder.txt_title.setText(title);

        if (item.getProduct() != null && item.getProduct().getProductColor() != null) {
            holder.img_color.setBackgroundColor(android.graphics.Color.parseColor(
                    item.getProduct().getProductColor().getColorCode()));
            holder.txt_color_name.setText(item.getProduct().getProductColor().getColorName());
        } else {
            holder.layout_color.setVisibility(View.GONE);
            holder.txt_color_name.setText("");
        }

        int price = 0, discount = 0;
        if (item.getProduct() != null) {
            price = item.getProduct().getPrice();
            discount = item.getProduct().getDiscount();
        } else if (item.getService() != null) {
            price = item.getService().getPrice();
            discount = item.getService().getDiscount();
        }
        if (discount < 100)
            discount = price * discount / 100;
        if (discount > 0) {
//            int final_price = item.getPrice() - item.getDiscount();
            String p = String.format(Locale.ENGLISH, "%,d",
                    price);
            holder.txt_price.setText(p, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) holder.txt_price.getText();
            int l = (p).length();
            spannable.setSpan(new StrikethroughSpan(), 0,
                    l, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        String p = String.format(Locale.ENGLISH, "%,d\n%s",
                (price - discount), mAct.getString(R.string.currency));
        holder.txt_final_price.setText(p);

        String logoUrl = null;
        if (item.getProduct() != null)
            logoUrl = item.getProduct().getMain_image();
        else if (item.getService() != null)
            logoUrl = item.getService().getIcon();

        if (logoUrl != null)
            Picasso.with(mAct).load(logoUrl).into(holder.img);

        int maxOrder = item.getMaxOrder();
        int orderQuantity = item.getOrderQuantity();

        ArrayList<String> nums = new ArrayList<>();
        for (int i = 1; i <= maxOrder; i++) {
            nums.add("" + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mAct,
                R.layout.item_spnr_quantity, nums);
        holder.spnr_quantity.setAdapter(adapter);
        holder.spnr_quantity.setSelection(orderQuantity - 1);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView txt_price_changed;
        TextView txt_title;
        TextView txt_color_name;
        TextView txt_price;
        TextView txt_final_price;

        ImageView img_color;
        ImageView img;

        LinearLayout layout_color;

        AppCompatSpinner spnr_quantity;

        public VH(View itemView) {
            super(itemView);
            txt_price_changed = (TextView) itemView.findViewById(R.id.txt_price_changed);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_color_name = (TextView) itemView.findViewById(R.id.txt_color_name);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_final_price = (TextView) itemView.findViewById(R.id.txt_final_price);
            img_color = (ImageView) itemView.findViewById(R.id.img_color);
            img = (ImageView) itemView.findViewById(R.id.img);
            layout_color = (LinearLayout) itemView.findViewById(R.id.layout_color);
            spnr_quantity = (AppCompatSpinner) itemView.findViewById(R.id.spnr_quantity);
        }
    }
}
