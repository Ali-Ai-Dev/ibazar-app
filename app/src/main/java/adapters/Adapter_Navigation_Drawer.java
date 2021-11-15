package adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.tnt.ibazaar.Act_About_Us;
import com.tnt.ibazaar.Act_Bookmarks;
import com.tnt.ibazaar.Act_Edit_Profile;
import com.tnt.ibazaar.Act_Main;
import com.tnt.ibazaar.Act_Previous_Orders;
import com.tnt.ibazaar.Act_Share;
import com.tnt.ibazaar.Act_Support;
import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

/**
 * Created by Omid on 3/3/2018.
 */

public class Adapter_Navigation_Drawer extends RecyclerView.Adapter<Adapter_Navigation_Drawer.VH> {

    private Act_Main mAct;
    private String[] data;

    public Adapter_Navigation_Drawer(Act_Main mAct, String[] data) {
        this.mAct = mAct;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mAct.getLayoutInflater()
                .inflate(R.layout.item_navigationview, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        String item = data[position];

        holder.txt_title.setText(item);
        holder.img_icon.setImageDrawable(getDrawable(position));
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (data[position]) {
                    case "خرید های پیشین":
                        mAct.startActivity(new Intent(mAct, Act_Previous_Orders.class));
                        break;

                    case "نشان شده ها":
                        mAct.startActivity(new Intent(mAct, Act_Bookmarks.class));
                        break;

                    case "معرفی به دوستان":
                        mAct.startActivity(new Intent(mAct, Act_Share.class));
                        break;

                    case "ویرایش اطلاعات کاربری":
                        mAct.startActivity(new Intent(mAct, Act_Edit_Profile.class));
                        break;

                    case "تنظیمات":
                        break;

                    case "پشتیبانی":
                        mAct.startActivity(new Intent(mAct, Act_Support.class));
                        break;

                    case "تماس با ما":
                        break;

                    case "درباره با ما":
                        mAct.startActivity(new Intent(mAct, Act_About_Us.class));
                        break;

                    case "خروج از حساب کاربری":
                        mAct.logOut();
                        break;
                }
            }
        });
    }

    private Drawable getDrawable(int position) {
        Drawable result = null;
        switch (data[position]) {
            case "خرید های پیشین":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_format_list_numbered)
                        .color(Color.parseColor("#F5705F"))
                        .sizeDp(20);
                break;
            case "نشان شده ها":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_bookmark)
                        .color(Color.parseColor("#0BA44A"))
                        .sizeDp(20);
                break;
            case "معرفی به دوستان":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_share)
                        .color(Color.parseColor("#5FBDE8"))
                        .sizeDp(20);
                break;

            case "ویرایش اطلاعات کاربری":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_person)
                        .color(Color.parseColor("#F6AE30"))
                        .sizeDp(20);
                break;

            case "تنظیمات":
                result = new IconicsDrawable(mAct, CommunityMaterial.Icon.cmd_settings)
                        .color(Color.parseColor("#F42630"))
                        .sizeDp(20);
                break;
            case "پشتیبانی":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_help)
                        .color(Color.parseColor("#558DA8"))
                        .sizeDp(20);
                break;
            case "تماس با ما":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_call)
                        .color(Color.parseColor("#2A323E"))
                        .sizeDp(20);
                break;

            case "درباره با ما":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_info)
                        .color(Color.parseColor("#F5705F"))
                        .sizeDp(20);
                break;
            case "خروج از حساب کاربری":
                result = new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_exit_to_app)
                        .color(Color.parseColor("#0BA44A"))
                        .sizeDp(20);
                break;

        }
        return result;
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.length;
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView txt_title;
        private ImageView img_icon;
        private View click;

        public VH(View itemView) {
            super(itemView);

            txt_title = (TextView) itemView.findViewById(R.id.txt);
            img_icon = (ImageView) itemView.findViewById(R.id.img);
            click = itemView;
        }
    }
}
