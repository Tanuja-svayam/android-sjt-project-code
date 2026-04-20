package com.shrikantelectronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_Dashboard_Stock extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<LocationStock> LocationStockList = null;
    private ArrayList<LocationStock> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_Stock(Context context,
                                           List<LocationStock> LocationStockList) {
        mContext = context;
        this.LocationStockList = LocationStockList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<LocationStock>();
        this.arraylist.addAll(LocationStockList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView companycd;
        TextView stocklocation;
        TextView purchase_value;
        TextView totalqty;

        TextView days_30;
        TextView days_31_60;
        TextView days_61_90;
        TextView days_91_180;
        TextView days_180_above;
    }

    @Override
    public int getCount() {
        return LocationStockList.size();
    }

    @Override
    public LocationStock getItem(int position) {
        return LocationStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dashboard_stock_view, null);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);
            holder.totalqty = (TextView) view.findViewById(R.id.totalqty);
            holder.days_30 = (TextView) view.findViewById(R.id.days_30);
            holder.days_31_60 = (TextView) view.findViewById(R.id.days_31_60);
            holder.days_61_90 = (TextView) view.findViewById(R.id.days_61_90);
            holder.days_91_180 = (TextView) view.findViewById(R.id.days_91_180);
            holder.days_180_above = (TextView) view.findViewById(R.id.days_180_above);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        //holder.companycd.setText(LocationStockList.get(position).getcompanycd());
        holder.stocklocation.setText(LocationStockList.get(position).getstocklocation ());
        //holder.purchase_value.setText(LocationStockList.get(position).getpurchase_value());
        holder.totalqty.setText(LocationStockList.get(position).gettotalqty());

        holder.days_30.setText(LocationStockList.get(position).getdays_30());
        holder.days_31_60.setText(LocationStockList.get(position).getdays_31_60());
        holder.days_61_90.setText(LocationStockList.get(position).getdays_61_90());
        holder.days_91_180.setText(LocationStockList.get(position).getdays_91_180());
        holder.days_180_above.setText(LocationStockList.get(position).getdays_180_above());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, StockSummaryAgeingListViewTab.class);
                intent.putExtra("companycd",(LocationStockList.get(position).getcompanycd()));
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        LocationStockList.clear();
        if (charText.length() == 0) {
            LocationStockList.addAll(arraylist);
        } else {
            for (LocationStock wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    LocationStockList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}