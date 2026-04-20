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

public class ListViewAdapter_Dashboard_Sales extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<LocationSales> LocationSalesList = null;
    private ArrayList<LocationSales> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_Sales(Context context,
                                           List<LocationSales> LocationSalesList) {
        mContext = context;
        this.LocationSalesList = LocationSalesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<LocationSales>();
        this.arraylist.addAll(LocationSalesList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView companycd;
        TextView stocklocation;
        TextView days_30;
        TextView days_31_60;
    }

    @Override
    public int getCount() {
        return LocationSalesList.size();
    }

    @Override
    public LocationSales getItem(int position) {
        return LocationSalesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dashboard_sales_view, null);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);
            holder.days_30 = (TextView) view.findViewById(R.id.days_30);
            holder.days_31_60 = (TextView) view.findViewById(R.id.days_31_60);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        //holder.companycd.setText(LocationSalesList.get(position).getcompanycd());
        holder.stocklocation.setText(LocationSalesList.get(position).getstocklocation ());
        holder.days_30.setText(LocationSalesList.get(position).getdays_30());
        holder.days_31_60.setText(LocationSalesList.get(position).getdays_31_60());

        /*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, StockSummaryAgeingListViewTab.class);
                intent.putExtra("companycd",(LocationSalesList.get(position).getcompanycd()));
                mContext.startActivity(intent);
            }
        });
        */

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        LocationSalesList.clear();
        if (charText.length() == 0) {
            LocationSalesList.addAll(arraylist);
        } else {
            for (LocationSales wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    LocationSalesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}