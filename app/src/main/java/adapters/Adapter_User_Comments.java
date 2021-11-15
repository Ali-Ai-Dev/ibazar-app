package adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import models.UserComment;
import persiancalendar.JalaliCalendar;

/**
 * Created by Omid on 4/26/2018.
 */

public class Adapter_User_Comments extends RecyclerView.Adapter<Adapter_User_Comments.VH> {

    private Act_Shop mAct;
    private ArrayList<UserComment> data;

    public Adapter_User_Comments(Act_Shop mAct, ArrayList<UserComment> data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater().inflate(
                R.layout.item_user_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        UserComment item = data.get(position);
        holder.txt_user_name.setText(item.getUserName());
        holder.txt_date.setText(
                getDate(Long.parseLong(item.getDate_time()))
        );
        holder.txt_comment_title.setText(item.getTitle());
        holder.txt_comment.setText(item.getComment());
//        holder.txt_dislike_count.setText(item.getDislike_count() + "");
//        holder.img_dislike.setImageDrawable(
//                new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_thumb_down)
//                        .color(Color.BLACK)
//                        .sizeDp(20));
//        holder.txt_like_count.setText(item.getLike_count() + "");
//        holder.img_like.setImageDrawable(
//                new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_thumb_up)
//                        .color(Color.BLACK)
//                        .sizeDp(20));
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
        TextView txt_date;
        TextView txt_user_name;
        TextView txt_comment_title;
        //        ExpandableTextView txt_comment;
        TextView txt_comment;
        TextView txt_dislike_count;
        ImageView img_dislike;
        TextView txt_like_count;
        ImageView img_like;

        public VH(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_user_name = (TextView) itemView.findViewById(R.id.txt_user_name);
            txt_comment_title = (TextView) itemView.findViewById(R.id.txt_comment_title);
//            txt_comment = (ExpandableTextView) itemView.findViewById(R.id.txt_comment);
            txt_comment = (TextView) itemView.findViewById(R.id.expandable_text);
            txt_dislike_count = (TextView) itemView.findViewById(R.id.txt_dislike_count);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            txt_like_count = (TextView) itemView.findViewById(R.id.txt_like_count);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
        }
    }
}
