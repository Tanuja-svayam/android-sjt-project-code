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

public class ListViewAdapter_Quotation extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Quotation> Invoicelist = null;
    private ArrayList<Quotation> arraylist;

    public ListViewAdapter_Quotation(Context context,
                                     List<Quotation> Invoicelist) {
        mContext = context;
        this.Invoicelist = Invoicelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Quotation>();
        this.arraylist.addAll(Invoicelist);

    }

    public class ViewHolder {
        TextView custname;
        TextView netinvoiceamt;
        TextView vinvorderdt;
        TextView invorderno;
    }

    @Override
    public int getCount() {
        return Invoicelist.size();
    }

    @Override
    public Quotation getItem(int position) {
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
            view = inflater.inflate(R.layout.activity_quotation_view_list, null);
            // Locate the TextViews in listview_item.xml
            holder.custname = (TextView) view.findViewById(R.id.custname);
            holder.netinvoiceamt = (TextView) view.findViewById(R.id.netinvoiceamt);
            holder.vinvorderdt = (TextView) view.findViewById(R.id.vinvorderdt);
            holder.invorderno = (TextView) view.findViewById(R.id.invorderno);

            // Locate the ImageView in listview_item.xml

           // holder.modelimage = (ImageView) view.findViewById(R.id.modelimage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.custname.setText(Invoicelist.get(position).getcustname());
        holder.netinvoiceamt.setText(Invoicelist.get(position).getnetinvoiceamt());
        holder.vinvorderdt.setText(Invoicelist.get(position).getvinvorderdt());
        holder.invorderno.setText(Invoicelist.get(position).getinvorderno());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, Salesmen_SalesQuotation.class);
                intent.putExtra("sysinvorderno",(Invoicelist.get(position).getsysinvorderno()));
                intent.putExtra("custname",(Invoicelist.get(position).getcustname()));
                intent.putExtra("vinvorderdt",(Invoicelist.get(position).getvinvorderdt()));
                intent.putExtra("netinvoiceamt",(Invoicelist.get(position).getnetinvoiceamt()));
                intent.putExtra("invorderno",(Invoicelist.get(position).getinvorderno()));

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
            for (Quotation wp : arraylist) {
                if (wp.getsearchfield().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Invoicelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}