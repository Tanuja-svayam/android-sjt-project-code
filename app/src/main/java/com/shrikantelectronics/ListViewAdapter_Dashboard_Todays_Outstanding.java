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

public class ListViewAdapter_Dashboard_Todays_Outstanding extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Todays_Outstanding> Todays_OutstandingList = null;
    private ArrayList<Todays_Outstanding> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_Todays_Outstanding(Context context,
                                                        List<Todays_Outstanding> Todays_OutstandingList) {
        mContext = context;
        this.Todays_OutstandingList = Todays_OutstandingList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Todays_Outstanding>();
        this.arraylist.addAll(Todays_OutstandingList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView companycd;
        TextView stocklocation;

        TextView cash_lastdate;
        TextView cash;

        TextView cheque_lastdate;
        TextView cheque;

        TextView clearcheque_lastdate;
        TextView clearcheque;

        TextView creditcard_lastdate;
        TextView creditcard;

        TextView finnance_lastdate;
        TextView finnance;

        TextView total;
    }

    @Override
    public int getCount() {
        return Todays_OutstandingList.size();
    }

    @Override
    public Todays_Outstanding getItem(int position) {
        return Todays_OutstandingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dashboard_todays_outstanding_view, null);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);

            holder.cash_lastdate = (TextView) view.findViewById(R.id.cash_lastdate);
            holder.cash = (TextView) view.findViewById(R.id.cash);

            holder.creditcard_lastdate = (TextView) view.findViewById(R.id.creditcard_lastdate);
            holder.creditcard = (TextView) view.findViewById(R.id.creditcard);

            holder.finnance_lastdate = (TextView) view.findViewById(R.id.finnance_lastdate);
            holder.finnance = (TextView) view.findViewById(R.id.finnance);

            holder.total = (TextView) view.findViewById(R.id.total);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.stocklocation.setText(Todays_OutstandingList.get(position).getstocklocation ());

        holder.cash_lastdate.setText(Todays_OutstandingList.get(position).getcash_lastdate());
        holder.cash.setText(Todays_OutstandingList.get(position).getcash());

        holder.creditcard_lastdate.setText(Todays_OutstandingList.get(position).getcreditcard_lastdate());
        holder.creditcard.setText(Todays_OutstandingList.get(position).getcreditcard());

        holder.finnance_lastdate.setText(Todays_OutstandingList.get(position).getfinnance_lastdate());
        holder.finnance.setText(Todays_OutstandingList.get(position).getfinnance());

        holder.total.setText(Todays_OutstandingList.get(position).gettotal());

        /*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, model_view_single.class);
                intent.putExtra("companycd",
                        (Todays_OutstandingList.get(position).getcompanycd()));
                intent.putExtra("stocklocation",
                        (Todays_OutstandingList.get(position).getstocklocation()));
                intent.putExtra("cash",
                        (Todays_OutstandingList.get(position).getcash()));
                intent.putExtra("creditcard",
                        (Todays_OutstandingList.get(position).getcreditcard()));
                intent.putExtra("finnance",
                        (Todays_OutstandingList.get(position).getfinnance()));
                intent.putExtra("total",
                        (Todays_OutstandingList.get(position).gettotal()));
                intent.putExtra("days_61_90",
                        (Todays_OutstandingList.get(position).getdays_61_90()));
                intent.putExtra("days_91_180",
                        (Todays_OutstandingList.get(position).getdays_91_180()));
                intent.putExtra("days_180_above",
                        (Todays_OutstandingList.get(position).getdays_180_above()));
                mContext.startActivity(intent);
            }
        });*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Todays_OutstandingList.clear();
        if (charText.length() == 0) {
            Todays_OutstandingList.addAll(arraylist);
        } else {
            for (Todays_Outstanding wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Todays_OutstandingList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}