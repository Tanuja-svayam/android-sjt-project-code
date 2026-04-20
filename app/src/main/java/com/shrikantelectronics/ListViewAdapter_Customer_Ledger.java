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

public class ListViewAdapter_Customer_Ledger extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Customer_Ledger> customerdetails = null;
    private ArrayList<Customer_Ledger> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Customer_Ledger(Context context,
                                           List<Customer_Ledger> customerdetails) {
        mContext = context;
        this.customerdetails = customerdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Customer_Ledger>();
        this.arraylist.addAll(customerdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView custcd;
        TextView sysaccvoucherno;
        TextView voucherdt;
        TextView trndesc;
        TextView debit;
        TextView credit;
        TextView refno;
        TextView drcr;
    }

    @Override
    public int getCount() {
        return customerdetails.size();
    }

    @Override
    public Customer_Ledger getItem(int position) {
        return customerdetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_customer_ledger_view_list, null);

            holder.voucherdt= (TextView) view.findViewById(R.id.voucherdt);
            holder.trndesc= (TextView) view.findViewById(R.id.trndesc);
            holder.debit= (TextView) view.findViewById(R.id.debit);
            holder.credit= (TextView) view.findViewById(R.id.credit);
            holder.refno= (TextView) view.findViewById(R.id.refno);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.voucherdt.setText(customerdetails.get(position).getvoucherdt());
        holder.trndesc.setText(customerdetails.get(position).gettrndesc());
        holder.debit.setText(customerdetails.get(position).getdebit());
        holder.credit.setText(customerdetails.get(position).getcredit());
        holder.refno.setText(customerdetails.get(position).getrefno());

/*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, customer_view_single.class);

                intent.putExtra("custcd",
                        (customerdetails.get(position).getcustcd()));
                intent.putExtra("name",
                        (customerdetails.get(position).getname()));
                intent.putExtra("address",
                        (customerdetails.get(position).getaddress()));
                intent.putExtra("mobile",
                        (customerdetails.get(position).getmobile()));
                intent.putExtra("emailid",
                        (customerdetails.get(position).getemailid()));
                intent.putExtra("outstanding",
                        (customerdetails.get(position).getoutstanding()));
                intent.putExtra("address",
                        (customerdetails.get(position).getaddress()));
                intent.putExtra("invoiceno",
                        (customerdetails.get(position).getinvoiceno()));
                intent.putExtra("vinvoicedt",
                        (customerdetails.get(position).getvinvoicedt()));
                intent.putExtra("netinvoiceamt",
                        (customerdetails.get(position).getnetinvoiceamt()));

                mContext.startActivity(intent);


            }
        });

        */

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        customerdetails.clear();
        if (charText.length() == 0) {
            customerdetails.addAll(arraylist);
        } else {
            for (Customer_Ledger wp : arraylist) {
                if (wp.gettrndesc().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    customerdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}