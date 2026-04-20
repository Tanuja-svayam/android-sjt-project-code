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

public class ListViewAdapter_StockVerification_WalletCompany extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<WalletCompany> WalletCompanylist = null;
    private ArrayList<WalletCompany> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_WalletCompany(Context context,
                                                           List<WalletCompany> WalletCompanylist) {
        mContext = context;
        this.WalletCompanylist = WalletCompanylist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<WalletCompany>();
        this.arraylist.addAll(WalletCompanylist);

    }

    public class ViewHolder {
        TextView digitalcompanyname;
    }

    @Override
    public int getCount() {
        return WalletCompanylist.size();
    }

    @Override
    public WalletCompany getItem(int position) {
        return WalletCompanylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_walletcompany_view_list, null);
            holder.digitalcompanyname = (TextView) view.findViewById(R.id.digitalcompanyname);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.digitalcompanyname.setText(WalletCompanylist.get(position).getdigitalcompanyname());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, Salesmen_SalesOrder.class);
               intent.putExtra("digitalcompanycd",(WalletCompanylist.get(position).getdigitalcompanycd()));
               intent.putExtra("digitalcompanyname",(WalletCompanylist.get(position).getdigitalcompanyname()));

                ((StockVerificationWallet_view)mContext).setResult(StockVerificationWallet_view.RESULT_OK,intent);
                ((StockVerificationWallet_view)mContext).finish();
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        WalletCompanylist.clear();
        if (charText.length() == 0) {
            WalletCompanylist.addAll(arraylist);
        } else {
            for (WalletCompany wp : arraylist) {
                if (wp.getdigitalcompanyname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    WalletCompanylist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}