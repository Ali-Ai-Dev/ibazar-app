package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tnt.ibazaar.Application;
import com.tnt.ibazaar.R;

import fragments.Frg_Bookmark_Products;
import fragments.Frg_Bookmark_Shops;

/**
 * Created by Omid on 4/26/2018.
 */

public class Adapter_Pager_Bookmarks extends FragmentStatePagerAdapter {

    private String[] titles = new String[]{Application.getContext().getString(R.string.products), Application.getContext().getString(R.string.shops)};

    public Adapter_Pager_Bookmarks(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Frg_Bookmark_Products();
            default:
                return new Frg_Bookmark_Shops();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
