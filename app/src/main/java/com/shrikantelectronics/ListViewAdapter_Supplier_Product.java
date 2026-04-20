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

public class ListViewAdapter_Supplier_Product extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Supplier_Product> supplierdetails = null;
    private ArrayList<Supplier_Product> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Supplier_Product(Context context,
                                            List<Supplier_Product> supplierdetails) {
        mContext = context;
        this.supplierdetails = supplierdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Supplier_Product>();
        this.arraylist.addAll(supplierdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView sysvendorno;
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
        return supplierdetails.size();
    }

    @Override
    public Supplier_Product getItem(int position) {
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
            view = inflater.inflate(R.layout.activity_supplier_product_list, null);

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

        holder.invoiceno.setText(supplierdetails.get(position).getinvoiceno());
        holder.invoicedt.setText(supplierdetails.get(position).getinvoicedt());
        holder.brandname.setText(supplierdetails.get(position).getbrandname());
        holder.modelname.setText(supplierdetails.get(position).getmodelname());
        holder.parentcategoryname.setText(supplierdetails.get(position).getparentcategoryname());
        holder.categoryname.setText(supplierdetails.get(position).getcategoryname());
        holder.serialno.setText(supplierdetails.get(position).getserialno());
        holder.netamt.setText(supplierdetails.get(position).getnetamt());
        holder.manufacturer_warranty_tenure.setText(supplierdetails.get(position).getmanufacturer_warranty_tenure());
        holder.warranty_start_date.setText(supplierdetails.get(position).getwarranty_start_date());
        holder.warranty_end_date.setText(supplierdetails.get(position).getwarranty_end_date());


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
            for (Supplier_Product wp : arraylist) {
                if (wp.getparentcategoryname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    supplierdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}