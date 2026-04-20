package com.shrikantelectronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_FA_Ledger extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<FA_Ledger> customerdetails = null;
    private ArrayList<FA_Ledger> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_FA_Ledger(Context context,
                                     List<FA_Ledger> customerdetails) {
        mContext = context;
        this.customerdetails = customerdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<FA_Ledger>();
        this.arraylist.addAll(customerdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView sysaccledgerno;
        TextView name;
        TextView address;
        TextView mobile;
        TextView emailid;
        TextView outstanding;

        TextView invoiceno;
        TextView vinvoicedt;
        TextView netinvoiceamt;
        ImageView modelimage;
    }

    @Override
    public int getCount() {
        return customerdetails.size();
    }

    @Override
    public FA_Ledger getItem(int position) {
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
            view = inflater.inflate(R.layout.activity_customer_view_list, null);

            holder.name= (TextView) view.findViewById(R.id.name);
            holder.mobile= (TextView) view.findViewById(R.id.mobile);
            holder.emailid= (TextView) view.findViewById(R.id.emailid);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews

       // holder.sysaccledgerno.setText(customerdetails.get(position).getsysaccledgerno());
        holder.name.setText(customerdetails.get(position).getname());
      //  holder.address.setText(customerdetails.get(position).getaddress());
       holder.mobile.setText(customerdetails.get(position).getmobile());
        holder.emailid.setText(customerdetails.get(position).getemailid());
       // holder.invoiceno.setText(customerdetails.get(position).getinvoiceno());
        //holder.vinvoicedt.setText(customerdetails.get(position).getvinvoicedt());
        //holder.netinvoiceamt.setText(customerdetails.get(position).getnetinvoiceamt());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, customer_view_single.class);

                intent.putExtra("sysaccledgerno",
                        (customerdetails.get(position).getsysaccledgerno()));
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

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        customerdetails.clear();
        if (charText.length() == 0) {
            customerdetails.addAll(arraylist);
        } else {
            for (FA_Ledger wp : arraylist) {
                if (wp.getname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    customerdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}