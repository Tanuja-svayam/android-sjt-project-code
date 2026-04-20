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

public class ListViewAdapter_StockVerification_Transporter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Transporter> Transporterlist = null;
    private ArrayList<Transporter> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_Transporter(Context context,
                                                         List<Transporter> Transporterlist) {
        mContext = context;
        this.Transporterlist = Transporterlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Transporter>();
        this.arraylist.addAll(Transporterlist);
    }

    public class ViewHolder {
        TextView transportername;
    }

    @Override
    public int getCount() {
        return Transporterlist.size();
    }

    @Override
    public Transporter getItem(int position) {
        return Transporterlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_transporter_view_list, null);
            holder.transportername = (TextView) view.findViewById(R.id.transportername);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.transportername.setText(Transporterlist.get(position).gettransportername());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, StockVerification.class);
               intent.putExtra("transportercd",(Transporterlist.get(position).gettransportercd()));
               intent.putExtra("transportername",(Transporterlist.get(position).gettransportername()));

                ((StockVerificationTransporter_view)mContext).setResult(StockVerificationTransporter_view.RESULT_OK,intent);
                ((StockVerificationTransporter_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Transporterlist.clear();
        if (charText.length() == 0) {
            Transporterlist.addAll(arraylist);
        } else {
            for (Transporter wp : arraylist) {
                if (wp.gettransportername().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Transporterlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}