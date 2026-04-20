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

public class ListViewAdapter_Model_Location extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Model_Location> customerdetails = null;
    private ArrayList<Model_Location> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Model_Location(Context context,
                                          List<Model_Location> customerdetails) {
        mContext = context;
        this.customerdetails = customerdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Model_Location>();
        this.arraylist.addAll(customerdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView companycd;
        TextView sysmodelno;
        TextView companyname;
        TextView lifetime_sales;
        TextView recent_sales;
        TextView bd_stock;
        TextView delivery_pending;
        TextView balancestock;
    }

    @Override
    public int getCount() {
        return customerdetails.size();
    }

    @Override
    public Model_Location getItem(int position) {
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
            view = inflater.inflate(R.layout.activity_model_location_list, null);

            holder.companyname= (TextView) view.findViewById(R.id.companyname);
            //holder.lifetime_sales= (TextView) view.findViewById(R.id.lifetime_sales);
            //holder.recent_sales= (TextView) view.findViewById(R.id.recent_sales);
           // holder.bd_stock= (TextView) view.findViewById(R.id.bd_stock);
          //  holder.delivery_pending= (TextView) view.findViewById(R.id.delivery_pending);
            holder.balancestock= (TextView) view.findViewById(R.id.balancestock);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.companyname.setText(customerdetails.get(position).getcompanyname());
//        holder.lifetime_sales.setText(customerdetails.get(position).getlifetime_sales());
//        holder.recent_sales.setText(customerdetails.get(position).getrecent_sales());
        //holder.bd_stock.setText(customerdetails.get(position).getbd_stock());
       // holder.delivery_pending.setText(customerdetails.get(position).getdelivery_pending());
        holder.balancestock.setText(customerdetails.get(position).getbalancestock());

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
            for (Model_Location wp : arraylist) {
                if (wp.getcompanyname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    customerdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}