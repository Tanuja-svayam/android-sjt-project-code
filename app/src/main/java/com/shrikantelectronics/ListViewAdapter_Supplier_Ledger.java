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

public class ListViewAdapter_Supplier_Ledger extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Supplier_Ledger> supplierdetails = null;
    private ArrayList<Supplier_Ledger> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Supplier_Ledger(Context context,
                                           List<Supplier_Ledger> supplierdetails) {
        mContext = context;
        this.supplierdetails = supplierdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Supplier_Ledger>();
        this.arraylist.addAll(supplierdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView sysvendorno;
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
        return supplierdetails.size();
    }

    @Override
    public Supplier_Ledger getItem(int position) {
        return supplierdetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_supplier_ledger_view_list, null);

            holder.voucherdt= (TextView) view.findViewById(R.id.voucherdt);
            holder.trndesc= (TextView) view.findViewById(R.id.trndesc);
            holder.debit= (TextView) view.findViewById(R.id.debit);
            holder.credit= (TextView) view.findViewById(R.id.credit);
            holder.refno= (TextView) view.findViewById(R.id.refno);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.voucherdt.setText(supplierdetails.get(position).getvoucherdt());
        holder.trndesc.setText(supplierdetails.get(position).gettrndesc());
        holder.debit.setText(supplierdetails.get(position).getdebit());
        holder.credit.setText(supplierdetails.get(position).getcredit());
        holder.refno.setText(supplierdetails.get(position).getrefno());

/*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, supplier_view_single.class);

                intent.putExtra("sysvendorno",
                        (supplierdetails.get(position).getsysvendorno()));
                intent.putExtra("name",
                        (supplierdetails.get(position).getname()));
                intent.putExtra("address",
                        (supplierdetails.get(position).getaddress()));
                intent.putExtra("mobile",
                        (supplierdetails.get(position).getmobile()));
                intent.putExtra("emailid",
                        (supplierdetails.get(position).getemailid()));
                intent.putExtra("outstanding",
                        (supplierdetails.get(position).getoutstanding()));
                intent.putExtra("address",
                        (supplierdetails.get(position).getaddress()));
                intent.putExtra("invoiceno",
                        (supplierdetails.get(position).getinvoiceno()));
                intent.putExtra("vinvoicedt",
                        (supplierdetails.get(position).getvinvoicedt()));
                intent.putExtra("netinvoiceamt",
                        (supplierdetails.get(position).getnetinvoiceamt()));

                mContext.startActivity(intent);


            }
        });

        */

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        supplierdetails.clear();
        if (charText.length() == 0) {
            supplierdetails.addAll(arraylist);
        } else {
            for (Supplier_Ledger wp : arraylist) {
                if (wp.gettrndesc().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    supplierdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}