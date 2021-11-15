package adapters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
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

import javax.crypto.Mac;

import models.Category;

/**
 * Created by Omid on 3/13/2018.
 */

public class Adapter_Elevator extends RecyclerView.Adapter<Adapter_Elevator.VH> {

    private Act_Main mAct;
    private ArrayList<Category> data;

    public Adapter_Elevator(Act_Main mAct, ArrayList<Category> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final Category item = data.get(position);
        holder.txt_title.setText(item.getTitle());

        if (item.getImage_name() != null && !item.getImage_name().isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
            String url = prefs.getString("Download_IP", "") +
//                    ":" +
//                    prefs.getInt("Download_Port", 6000) + "/" +
                    prefs.getString("Download_Folder_Category", "") + "/" +
                    item.getImage_name();
            Log.e("url image", url);
            Picasso.with(mAct)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img);
        }
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mAct, Act_Category_Gallery.class);
//                intent.putExtra("id", item.getId());
//                mAct.startActivity(intent);
                Application.setLevel(2);
                mAct.loadCategory(item.getId(), item.getTitle());
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
        private TextView txt_title;
        private ImageView img;
        private View click;

        public VH(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            img = (ImageView) itemView.findViewById(R.id.img);
            click = itemView;
        }
    }
}
