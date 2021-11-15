package adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.tnt.ibazaar.Act_Product_And_Service;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Color;

/**
 * Created by Omid on 9/4/2018.
 */

public class Adapter_Colors extends RecyclerView.Adapter<Adapter_Colors.VH> {

    private Act_Product_And_Service mAct;
    private ArrayList<Color> data;

    public Adapter_Colors(Act_Product_And_Service mAct, ArrayList<Color> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_product_color,
                parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final Color item = data.get(position);

        holder.txt_color_name.setText(item.getColorName());
        holder.img_color.setBackgroundColor(android.graphics.Color.parseColor(item.getColorCode()));
//        holder.img_color.setImageDrawable(new ColorDrawable(android.graphics.Color.parseColor(item.getColorCode())));

        if (item.isSelected())
            holder.itemView.setBackgroundResource(R.drawable.bg_selected_color);
        else
            holder.itemView.setBackgroundDrawable(null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Color colorStock : data) {
                    colorStock.setSelected(false);
                }

                item.setSelected(true);
                notifyDataSetChanged();
//                mAct.updateViews(item.getProductColorId());
                mAct.updatePrice(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class VH extends ViewHolder {
        TextView txt_color_name;
        CircularImageView img_color;

        public VH(View itemView) {
            super(itemView);
            txt_color_name = (TextView) itemView.findViewById(R.id.txt_color_name);
            img_color = (CircularImageView) itemView.findViewById(R.id.img_color);
        }
    }
}
