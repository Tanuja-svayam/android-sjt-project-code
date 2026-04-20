package com.shrikantelectronics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_CrmActivityStatus extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<CrmActivityStatus> CrmActivityStatusList = null;
    private ArrayList<CrmActivityStatus> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_CrmActivityStatus(Context context,
                                             List<CrmActivityStatus> CrmActivityStatusList) {
        mContext = context;
        this.CrmActivityStatusList = CrmActivityStatusList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CrmActivityStatus>();
        this.arraylist.addAll(CrmActivityStatusList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView companycd;
        TextView stocklocation;
        TextView op_pending;
        TextView op_inprocess;
        TextView added_during_month;
        TextView cl_currentyear;
        TextView cl_currentmonth;
        TextView total_in_open_in_hand;
    }

    @Override
    public int getCount() {
        return CrmActivityStatusList.size();
    }

    @Override
    public CrmActivityStatus getItem(int position) {
        return CrmActivityStatusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_crmactivity_view, null);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);
            holder.op_pending = (TextView) view.findViewById(R.id.op_pending);
            holder.op_inprocess = (TextView) view.findViewById(R.id.op_inprocess);
            holder.added_during_month = (TextView) view.findViewById(R.id.added_during_month);
            holder.cl_currentyear = (TextView) view.findViewById(R.id.cl_currentyear);
            holder.cl_currentmonth = (TextView) view.findViewById(R.id.cl_currentmonth);
            holder.total_in_open_in_hand = (TextView) view.findViewById(R.id.total_in_open_in_hand);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        //holder.companycd.setText(CrmActivityStatusList.get(position).getcompanycd());
        holder.stocklocation.setText(CrmActivityStatusList.get(position).getstocklocation ());
        holder.op_pending.setText(CrmActivityStatusList.get(position).getop_pending());
        holder.op_inprocess.setText(CrmActivityStatusList.get(position).getop_inprocess());
        holder.added_during_month.setText(CrmActivityStatusList.get(position).getadded_during_month());
        holder.cl_currentyear.setText(CrmActivityStatusList.get(position).getcl_currentyear());
        holder.cl_currentmonth.setText(CrmActivityStatusList.get(position).getcl_currentmonth());
        holder.total_in_open_in_hand.setText(CrmActivityStatusList.get(position).gettotal_in_open_in_hand());

/*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, model_view_single.class);
                intent.putExtra("companycd",
                        (CrmActivityStatusList.get(position).getcompanycd()));
                intent.putExtra("stocklocation",
                        (CrmActivityStatusList.get(position).getstocklocation()));
                intent.putExtra("op_pending",
                        (CrmActivityStatusList.get(position).getop_pending()));
                intent.putExtra("total_in_open_in_hand",
                        (CrmActivityStatusList.get(position).gettotal_in_open_in_hand()));
                intent.putExtra("op_inprocess",
                        (CrmActivityStatusList.get(position).getop_inprocess()));
                intent.putExtra("added_during_month",
                        (CrmActivityStatusList.get(position).getadded_during_month()));
                intent.putExtra("cl_currentyear",
                        (CrmActivityStatusList.get(position).getcl_currentyear()));
                intent.putExtra("cl_currentmonth",
                        (CrmActivityStatusList.get(position).getcl_currentmonth()));
                intent.putExtra("days_180_above",
                        (CrmActivityStatusList.get(position).getdays_180_above()));
                mContext.startActivity(intent);
            }
        });*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        CrmActivityStatusList.clear();
        if (charText.length() == 0) {
            CrmActivityStatusList.addAll(arraylist);
        } else {
            for (CrmActivityStatus wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    CrmActivityStatusList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}