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

public class ListViewAdapter_StockVerification_FinanceCompany extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<FinanceCompany> FinanceCompanylist = null;
    private ArrayList<FinanceCompany> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_FinanceCompany(Context context,
                                                            List<FinanceCompany> FinanceCompanylist) {
        mContext = context;
        this.FinanceCompanylist = FinanceCompanylist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<FinanceCompany>();
        this.arraylist.addAll(FinanceCompanylist);

    }

    public class ViewHolder {
        TextView financename;
    }

    @Override
    public int getCount() {
        return FinanceCompanylist.size();
    }

    @Override
    public FinanceCompany getItem(int position) {
        return FinanceCompanylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_financecompany_view_list, null);
            holder.financename = (TextView) view.findViewById(R.id.financename);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.financename.setText(FinanceCompanylist.get(position).getfinancename());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, Salesmen_SalesOrder.class);
               intent.putExtra("fincompanycd",(FinanceCompanylist.get(position).getfincompanycd()));
               intent.putExtra("financename",(FinanceCompanylist.get(position).getfinancename()));

                ((StockVerificationFinance_view)mContext).setResult(StockVerificationFinance_view.RESULT_OK,intent);
                ((StockVerificationFinance_view)mContext).finish();
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        FinanceCompanylist.clear();
        if (charText.length() == 0) {
            FinanceCompanylist.addAll(arraylist);
        } else {
            for (FinanceCompany wp : arraylist) {
                if (wp.getfinancename().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    FinanceCompanylist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}