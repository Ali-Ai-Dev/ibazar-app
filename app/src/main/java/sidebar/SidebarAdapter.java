package sidebar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.Act_Main;
import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

import java.util.ArrayList;

import models.Category;


public class SidebarAdapter extends RecyclerView.Adapter<SidebarAdapter.VH> {

    private Act_Main mAct;
    private ArrayList<Category> data;
//    private View.OnClickListener mOnClickListener;

    public SidebarAdapter(Act_Main mAct, ArrayList<Category> data) {
        this.mAct = mAct;
        this.data = data;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_sidebar, parent, false));

    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final Category item = data.get(position % data.size());
        holder.text.setText(item.getTitle());
        holder.image.setImageBitmap(null);
        if (item.getImage_name() != null && !item.getImage_name().isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
            String url = prefs.getString("Download_IP", "") +
//                    ":" +
//                    prefs.getInt("Download_Port", 6000) + "/" +
                    prefs.getString("Download_Folder_Category", "") + "/" +
                    item.getImage_name();
            Log.e("sidebar image url", url);
            Picasso.with(mAct)
                    .load(url)
                    .into(holder.image);
        } else {
            holder.image.setImageDrawable(null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item.getId()", "" + item.getId());
                mAct.setCategory(item.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return Integer.MAX_VALUE;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        View click;

        VH(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.ivIcon);
            text = (TextView) itemView.findViewById(R.id.tvTitle);
            click = itemView;
        }
    }
}