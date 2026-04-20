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

public class ListViewAdapter_Order extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Order> Invoicelist = null;
    private ArrayList<Order> arraylist;

    public ListViewAdapter_Order(Context context,
                                 List<Order> Invoicelist) {
        mContext = context;
        this.Invoicelist = Invoicelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Order>();
        this.arraylist.addAll(Invoicelist);

    }

    public class ViewHolder {
        TextView custname;
        TextView netinvoiceamt;
        TextView vinvoicedt;
        TextView invoiceno;
    }

    @Override
    public int getCount() {
        return Invoicelist.size();
    }

    @Override
    public Order getItem(int position) {
        return Invoicelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_order_view_list, null);
            // Locate the TextViews in listview_item.xml
            holder.custname = (TextView) view.findViewById(R.id.custname);
            holder.netinvoiceamt = (TextView) view.findViewById(R.id.netinvoiceamt);
            holder.vinvoicedt = (TextView) view.findViewById(R.id.vinvoicedt);
            holder.invoiceno = (TextView) view.findViewById(R.id.invoiceno);

            // Locate the ImageView in listview_item.xml

           // holder.modelimage = (ImageView) view.findViewById(R.id.modelimage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.custname.setText(Invoicelist.get(position).getcustname());
        holder.netinvoiceamt.setText(Invoicelist.get(position).getnetinvoiceamt());
        holder.vinvoicedt.setText(Invoicelist.get(position).getvinvoicedt());
        holder.invoiceno.setText(Invoicelist.get(position).getinvoiceno());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, Salesmen_SalesOrder.class);
                intent.putExtra("sysinvorderno",(Invoicelist.get(position).getsysinvno()));
                intent.putExtra("custname",(Invoicelist.get(position).getcustname()));
                intent.putExtra("vinvoicedt",(Invoicelist.get(position).getvinvoicedt()));
                intent.putExtra("netinvoiceamt",(Invoicelist.get(position).getnetinvoiceamt()));
                intent.putExtra("invorderno",(Invoicelist.get(position).getinvoiceno()));

                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Invoicelist.clear();
        if (charText.length() == 0) {
            Invoicelist.addAll(arraylist);
        } else {
            for (Order wp : arraylist) {
                if (wp.getsearchfield().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Invoicelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}