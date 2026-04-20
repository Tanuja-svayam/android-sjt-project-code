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

public class ListViewAdapter_StockTransferReceipt extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Serial_Stock_Transfer> Serial_Stock_Transferlist = null;
    private ArrayList<Serial_Stock_Transfer> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockTransferReceipt(Context context,
                                                List<Serial_Stock_Transfer> Serial_Stock_Transferlist) {
        mContext = context;
        this.Serial_Stock_Transferlist = Serial_Stock_Transferlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Serial_Stock_Transfer>();
        this.arraylist.addAll(Serial_Stock_Transferlist);

      //  imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView serialno;
        TextView barcodeno;
        TextView modelname;
        TextView brandname;
        TextView source_companyname;
        TextView sysproductno;
        TextView sysbrandno;

    }

    @Override
    public int getCount() {
        return Serial_Stock_Transferlist.size();
    }

    @Override
    public Serial_Stock_Transfer getItem(int position) {
        return Serial_Stock_Transferlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_transfer_stock_pending_list, null);
            holder.serialno = (TextView) view.findViewById(R.id.serialno);
            holder.modelname = (TextView) view.findViewById(R.id.modelname);
            holder.source_companyname = (TextView) view.findViewById(R.id.source_companyname);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.serialno.setText(Serial_Stock_Transferlist.get(position).getserialno());
        holder.modelname.setText(Serial_Stock_Transferlist.get(position).getmodelname());
        holder.source_companyname.setText(Serial_Stock_Transferlist.get(position).getstocklocation());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, TransferStockReceipt.class);
                intent.putExtra("systrfdtlno",(Serial_Stock_Transferlist.get(position).getsystrfdtlno()));
                intent.putExtra("systrfno",(Serial_Stock_Transferlist.get(position).getsystrfno()));
               intent.putExtra("sysproductno",(Serial_Stock_Transferlist.get(position).getsysproductno()));
               intent.putExtra("serialno",(Serial_Stock_Transferlist.get(position).getserialno()));
               intent.putExtra("barcodeno",(Serial_Stock_Transferlist.get(position).getbarcodeno()));

                mContext.startActivity(intent);

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Serial_Stock_Transferlist.clear();
        if (charText.length() == 0) {
            Serial_Stock_Transferlist.addAll(arraylist);
        } else {
            for (Serial_Stock_Transfer wp : arraylist) {
                if (wp.getsearchserial().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Serial_Stock_Transferlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}