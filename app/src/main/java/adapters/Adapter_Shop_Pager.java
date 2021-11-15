package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import fragments.Frg_Announcements;
import fragments.Frg_Shop_Coordinates;
import fragments.Frg_Shop_Info;
import fragments.Frg_Shop_Products_And_Services;
import fragments.Frg_User_Comments;

/**
 * Created by Omid on 4/24/2018.
 */

public class Adapter_Shop_Pager extends FragmentStatePagerAdapter {

    private Act_Shop mAct;

    private int[] titles = new int[]{R.string.position, R.string.user_comments,
            R.string.announcements, R.string.Service, R.string.products, R.string.info
    };

    public Adapter_Shop_Pager(FragmentManager fm, Act_Shop mAct) {
        super(fm);
        this.mAct = mAct;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 5:

                return new Frg_Shop_Info();

            case 4:
                return Frg_Shop_Products_And_Services.getInstance(0);
            case 3:
                return Frg_Shop_Products_And_Services.getInstance(1);
            case 2:
                return new Frg_Announcements();

            case 1:
                return new Frg_User_Comments();

            case 0:
                return new Frg_Shop_Coordinates();

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAct.getString(titles[position]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
