package adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Specs;

/**
 * Created by Omid on 8/4/2018.
 */

public class Adapter_Specs extends RecyclerView.Adapter<Adapter_Specs.VH> {

    private Activity mAct;
    private ArrayList<Specs> data;

    public Adapter_Specs(Activity mAct, ArrayList<Specs> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_specs, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Specs item = data.get(position);
        holder.txt_name.setText(item.getName());
        holder.txt_value.setText(item.getValue());
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView txt_name;
        TextView txt_value;

        public VH(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_value = (TextView) itemView.findViewById(R.id.txt_value);
        }
    }
}
