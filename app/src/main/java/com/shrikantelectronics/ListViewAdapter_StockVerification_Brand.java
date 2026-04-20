package com.shrikantelectronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_StockVerification_Brand extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Brand> Brandlist = null;
    private ArrayList<Brand> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_Brand(Context context,
                                                   List<Brand> Brandlist) {
        mContext = context;
        this.Brandlist = Brandlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Brand>();
        this.arraylist.addAll(Brandlist);

    }

    public class ViewHolder {
        TextView description;
    }

    @Override
    public int getCount() {
        return Brandlist.size();
    }

    @Override
    public Brand getItem(int position) {
        return Brandlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_brand_view_list, null);
            holder.description = (TextView) view.findViewById(R.id.description);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.description.setText(Brandlist.get(position).getdescription());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, StockVerification.class);
               intent.putExtra("sysbrandno",(Brandlist.get(position).getsysbrandno()));
               intent.putExtra("description",(Brandlist.get(position).getdescription()));

                ((StockVerificationBrand_view)mContext).setResult(StockVerificationBrand_view.RESULT_OK,intent);
                ((StockVerificationBrand_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Brandlist.clear();
        if (charText.length() == 0) {
            Brandlist.addAll(arraylist);
        } else {
            for (Brand wp : arraylist) {
                if (wp.getdescription().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Brandlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}