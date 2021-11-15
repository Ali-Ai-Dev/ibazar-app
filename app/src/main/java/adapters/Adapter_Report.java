package adapters;

import android.app.Activity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.ReportItem;

/**
 * Created by Omid on 4/30/2018.
 */

public class Adapter_Report extends RecyclerView.Adapter<Adapter_Report.VH> {

    private Activity mAct;
    private ArrayList<ReportItem> data;

    public Adapter_Report(Activity mAct, ArrayList<ReportItem> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater()
                .inflate(R.layout.item_report, parent, false));
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        ReportItem item = data.get(position);
        holder.rbtn.setSelected(item.isSelected());
        holder.txt_title.setText(item.getTitle());
        holder.rbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < data.size(); i++) {
                    if (i == position) {
                        data.get(i).setSelected(true);

                    } else
                        data.get(i).setSelected(false);
//                    notifyItemChanged(position);
                }
                notifyDataSetChanged();
            }
        });
        holder.txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rbtn.setChecked(true);
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
        TextView txt_title;
        AppCompatRadioButton rbtn;

        public VH(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            rbtn = (AppCompatRadioButton) itemView.findViewById(R.id.rbtn);
        }
    }
}
