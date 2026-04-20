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

public class ListViewAdapter_StockVerification_PaymentMode extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<PaymentMode> PaymentModelist = null;
    private ArrayList<PaymentMode> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_PaymentMode(Context context,
                                                         List<PaymentMode> PaymentModelist) {
        mContext = context;
        this.PaymentModelist = PaymentModelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<PaymentMode>();
        this.arraylist.addAll(PaymentModelist);

    }

    public class ViewHolder {
        TextView paymodename;
    }

    @Override
    public int getCount() {
        return PaymentModelist.size();
    }

    @Override
    public PaymentMode getItem(int position) {
        return PaymentModelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_paymentmode_view_list, null);
            holder.paymodename = (TextView) view.findViewById(R.id.paymodename);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.paymodename.setText(PaymentModelist.get(position).getpaymodename());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, Salesmen_SalesOrder.class);
               intent.putExtra("bankcd",(PaymentModelist.get(position).getbankcd()));
               intent.putExtra("paymodename",(PaymentModelist.get(position).getpaymodename()));

                ((StockVerificationPaymentMode_view)mContext).setResult(StockVerificationPaymentMode_view.RESULT_OK,intent);
                ((StockVerificationPaymentMode_view)mContext).finish();
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        PaymentModelist.clear();
        if (charText.length() == 0) {
            PaymentModelist.addAll(arraylist);
        } else {
            for (PaymentMode wp : arraylist) {
                if (wp.getpaymodename().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    PaymentModelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}