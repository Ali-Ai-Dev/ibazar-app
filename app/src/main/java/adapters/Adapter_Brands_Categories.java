package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Category;

/**
 * Created by Omid on 4/9/2018.
 */

public class Adapter_Brands_Categories extends RecyclerView.Adapter<Adapter_Brands_Categories.VH> {

    private ArrayList<Category> data;
    private Activity mAct;

    public Adapter_Brands_Categories(ArrayList<Category> data, Activity context) {
        this.data = data;
        this.mAct = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_brand_category, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Category item = data.get(position);
        holder.txt_cat_title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView txt_cat_title;

        public VH(View itemView) {
            super(itemView);
            txt_cat_title = (TextView) itemView.findViewById(R.id.txt_cat_title);
        }
    }
}
