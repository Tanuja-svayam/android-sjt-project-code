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

public class ListViewAdapter_StockVerification_Location extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Location> Locationlist = null;
    private ArrayList<Location> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_Location(Context context,
                                                      List<Location> Locationlist) {
        mContext = context;
        this.Locationlist = Locationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Location>();
        this.arraylist.addAll(Locationlist);

    }

    public class ViewHolder {
        TextView companyname;
    }

    @Override
    public int getCount() {
        return Locationlist.size();
    }

    @Override
    public Location getItem(int position) {
        return Locationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_location_view_list, null);
            holder.companyname = (TextView) view.findViewById(R.id.companyname);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.companyname.setText(Locationlist.get(position).getcompanyname());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, StockVerification.class);
               intent.putExtra("companycd",(Locationlist.get(position).getcompanycd()));
               intent.putExtra("companyname",(Locationlist.get(position).getcompanyname()));

                ((StockVerificationLocation_view)mContext).setResult(StockVerificationLocation_view.RESULT_OK,intent);
                ((StockVerificationLocation_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Locationlist.clear();
        if (charText.length() == 0) {
            Locationlist.addAll(arraylist);
        } else {
            for (Location wp : arraylist) {
                if (wp.getcompanyname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Locationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}