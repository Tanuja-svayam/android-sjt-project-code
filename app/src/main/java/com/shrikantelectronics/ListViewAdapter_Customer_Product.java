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

public class ListViewAdapter_Customer_Product extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Customer_Product> customerdetails = null;
    private ArrayList<Customer_Product> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Customer_Product(Context context,
                                            List<Customer_Product> customerdetails) {
        mContext = context;
        this.customerdetails = customerdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Customer_Product>();
        this.arraylist.addAll(customerdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView custcd;
        TextView invoiceno;
        TextView invoicedt;
        TextView brandname;
        TextView modelname;
        TextView parentcategoryname;
        TextView categoryname;
        TextView serialno;
        TextView quantity;
        TextView netamt;
        TextView manufacturer_warranty_tenure;
        TextView main_manufacturer_warranty_tenure;
        TextView warranty_start_date;
        TextView warranty_end_date;
    }

    @Override
    public int getCount() {
        return customerdetails.size();
    }

    @Override
    public Customer_Product getItem(int position) {
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
            view = inflater.inflate(R.layout.activity_customer_product_list, null);

            holder.invoiceno= (TextView) view.findViewById(R.id.invoiceno);
            holder.invoicedt= (TextView) view.findViewById(R.id.invoicedt);
            holder.brandname= (TextView) view.findViewById(R.id.brandname);
            holder.modelname= (TextView) view.findViewById(R.id.modelname);
            holder.parentcategoryname= (TextView) view.findViewById(R.id.parentcategoryname);
            holder.categoryname= (TextView) view.findViewById(R.id.categoryname);
            holder.serialno= (TextView) view.findViewById(R.id.serialno);
            holder.netamt= (TextView) view.findViewById(R.id.netamt);
            holder.manufacturer_warranty_tenure= (TextView) view.findViewById(R.id.manufacturer_warranty_tenure);
            holder.warranty_start_date= (TextView) view.findViewById(R.id.warranty_start_date);
            holder.warranty_end_date= (TextView) view.findViewById(R.id.warranty_end_date);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.invoiceno.setText(customerdetails.get(position).getinvoiceno());
        holder.invoicedt.setText(customerdetails.get(position).getinvoicedt());
        holder.brandname.setText(customerdetails.get(position).getbrandname());
        holder.modelname.setText(customerdetails.get(position).getmodelname());
        holder.parentcategoryname.setText(customerdetails.get(position).getparentcategoryname());
        holder.categoryname.setText(customerdetails.get(position).getcategoryname());
        holder.serialno.setText(customerdetails.get(position).getserialno());
        holder.netamt.setText(customerdetails.get(position).getnetamt());
        holder.manufacturer_warranty_tenure.setText(customerdetails.get(position).getmanufacturer_warranty_tenure());
        holder.warranty_start_date.setText(customerdetails.get(position).getwarranty_start_date());
        holder.warranty_end_date.setText(customerdetails.get(position).getwarranty_end_date());


        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, Invoice_view_single.class);

                intent.putExtra("sysinvno",(customerdetails.get(position).getsysinvno()));
                intent.putExtra("custname","");
                intent.putExtra("netinvoiceamt","");
                intent.putExtra("vinvoicedt","");
                intent.putExtra("invoiceno","");
                intent.putExtra("PURPOSE","DOWNLOAD");

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
            for (Customer_Product wp : arraylist) {
                if (wp.getparentcategoryname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    customerdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}