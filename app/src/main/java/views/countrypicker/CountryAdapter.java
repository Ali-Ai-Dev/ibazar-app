package views.countrypicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.tnt.ibazaar.R;

import java.util.List;


/**
 * Created by mispc1 on 8/29/17.
 */

public class CountryAdapter extends BaseAdapter {

    private Context mContext;
    List<Country> countries;
    LayoutInflater inflater;

    public CountryAdapter(Context context, List<Country> countries) {
        this.mContext = context;
        this.countries = countries;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return countries.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0L;
    }

    public View getView(int position, View view, ViewGroup parent) {
        Country country = countries.get(position);
        if (view == null) {
            view = this.inflater.inflate(R.layout.item_country, null);
        }

        Cell cell = Cell.from(view);
        cell.textView.setText(country.getName());
        country.loadFlagByCode(this.mContext);
        if (country.getFlag() != -1) {
            cell.imageView.setImageResource(country.getFlag());
        }

        return view;
    }

    static class Cell {
        public TextView textView;
        public ImageView imageView;

        Cell() {
        }

        static Cell from(View view) {
            if (view == null) {
                return null;
            } else if (view.getTag() == null) {
                Cell cell = new Cell();
                cell.textView = (TextView) view.findViewById(R.id.row_title);
                cell.imageView = (ImageView) view.findViewById(R.id.row_icon);
                view.setTag(cell);
                return cell;
            } else {
                return (Cell) view.getTag();
            }
        }
    }

}
