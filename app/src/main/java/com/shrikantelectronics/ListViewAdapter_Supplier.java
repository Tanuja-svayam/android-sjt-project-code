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

public class ListViewAdapter_Supplier extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Supplier> Supplierdetails = null;
    private ArrayList<Supplier> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Supplier(Context context,
                                    List<Supplier> Supplierdetails) {
        mContext = context;
        this.Supplierdetails = Supplierdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Supplier>();
        this.arraylist.addAll(Supplierdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView sysvendorno;
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
        return Supplierdetails.size();
    }

    @Override
    public Supplier getItem(int position) {
        return Supplierdetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_supplier_view_list, null);

            holder.name= (TextView) view.findViewById(R.id.name);
            holder.mobile= (TextView) view.findViewById(R.id.mobile);
            holder.emailid= (TextView) view.findViewById(R.id.emailid);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews

       // holder.sysvendorno.setText(Supplierdetails.get(position).getsysvendorno());
        holder.name.setText(Supplierdetails.get(position).getname());
       holder.mobile.setText(Supplierdetails.get(position).getmobile());
        holder.emailid.setText(Supplierdetails.get(position).getemailid());

       // holder.invoiceno.setText(Supplierdetails.get(position).getinvoiceno());
        //holder.vinvoicedt.setText(Supplierdetails.get(position).getvinvoicedt());
        //holder.netinvoiceamt.setText(Supplierdetails.get(position).getnetinvoiceamt());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, supplier_view_single.class);

                intent.putExtra("sysvendorno",
                        (Supplierdetails.get(position).getsysvendorno()));
                intent.putExtra("name",
                        (Supplierdetails.get(position).getname()));
                intent.putExtra("address",
                        (Supplierdetails.get(position).getaddress()));
                intent.putExtra("mobile",
                        (Supplierdetails.get(position).getmobile()));
                intent.putExtra("emailid",
                        (Supplierdetails.get(position).getemailid()));
                intent.putExtra("outstanding",
                        (Supplierdetails.get(position).getoutstanding()));
                intent.putExtra("address",
                        (Supplierdetails.get(position).getaddress()));
                intent.putExtra("invoiceno",
                        (Supplierdetails.get(position).getinvoiceno()));
                intent.putExtra("vinvoicedt",
                        (Supplierdetails.get(position).getvinvoicedt()));
                intent.putExtra("netinvoiceamt",
                        (Supplierdetails.get(position).getnetinvoiceamt()));

                mContext.startActivity(intent);


            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Supplierdetails.clear();
        if (charText.length() == 0) {
            Supplierdetails.addAll(arraylist);
        } else {
            for (Supplier wp : arraylist) {
                if (wp.getname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Supplierdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}