package com.shrikantelectronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_Dashboard_Sales_Customer extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<LocationSalesCustomer> LocationSalesCustomerList = null;
    private ArrayList<LocationSalesCustomer> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_Sales_Customer(Context context,
                                                    List<LocationSalesCustomer> LocationSalesCustomerList) {
        mContext = context;
        this.LocationSalesCustomerList = LocationSalesCustomerList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<LocationSalesCustomer>();
        this.arraylist.addAll(LocationSalesCustomerList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView companycd;
        TextView stocklocation;
        TextView custname;
        TextView topcategoryname;
        TextView brandname;
        TextView modelname;
        TextView quantity;
        TextView unitprice_include_tax;

    }

    @Override
    public int getCount() {
        return LocationSalesCustomerList.size();
    }

    @Override
    public LocationSalesCustomer getItem(int position) {
        return LocationSalesCustomerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dashboard_sales_customer_view, null);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);
            holder.custname = (TextView) view.findViewById(R.id.custname);
            holder.topcategoryname = (TextView) view.findViewById(R.id.topcategoryname);
            holder.brandname = (TextView) view.findViewById(R.id.brandname);
            holder.modelname = (TextView) view.findViewById(R.id.modelname);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.unitprice_include_tax = (TextView) view.findViewById(R.id.unitprice_include_tax);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        //holder.companycd.setText(LocationSalesCustomerList.get(position).getcompanycd());
        holder.stocklocation.setText(LocationSalesCustomerList.get(position).getstocklocation ());
        holder.custname.setText(LocationSalesCustomerList.get(position).getcustname ());
        holder.topcategoryname.setText(LocationSalesCustomerList.get(position).gettopcategoryname());
        holder.brandname.setText(LocationSalesCustomerList.get(position).getbrandname());
        holder.modelname.setText(LocationSalesCustomerList.get(position).getmodelname());
        holder.quantity.setText(LocationSalesCustomerList.get(position).getquantity());
        holder.unitprice_include_tax.setText(LocationSalesCustomerList.get(position).getunitprice_include_tax());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, customer_view_single.class);

                intent.putExtra("custcd",
                        (LocationSalesCustomerList.get(position).getcustcd()));
                intent.putExtra("name",
                        (LocationSalesCustomerList.get(position).getcustname()));
                intent.putExtra("address",
                        (LocationSalesCustomerList.get(position).getaddress()));
                intent.putExtra("mobile",
                        (LocationSalesCustomerList.get(position).getmobile()));
                intent.putExtra("emailid",
                        (LocationSalesCustomerList.get(position).getemailid()));
                intent.putExtra("outstanding",
                        (LocationSalesCustomerList.get(position).getoutstanding()));
                intent.putExtra("address",
                        (LocationSalesCustomerList.get(position).getaddress()));
                intent.putExtra("invoiceno",
                        (""));
                intent.putExtra("vinvoicedt",
                        (""));
                intent.putExtra("netinvoiceamt",
                        (""));

                mContext.startActivity(intent);

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        LocationSalesCustomerList.clear();
        if (charText.length() == 0) {
            LocationSalesCustomerList.addAll(arraylist);
        } else {
            for (LocationSalesCustomer wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    LocationSalesCustomerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}