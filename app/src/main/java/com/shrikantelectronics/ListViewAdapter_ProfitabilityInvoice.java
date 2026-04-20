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

public class ListViewAdapter_ProfitabilityInvoice extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<ProfitabilityInvoice> ProfitabilityInvoicelist = null;
    private ArrayList<ProfitabilityInvoice> arraylist;

    public ListViewAdapter_ProfitabilityInvoice(Context context,
                                                List<ProfitabilityInvoice> ProfitabilityInvoicelist) {
        mContext = context;
        this.ProfitabilityInvoicelist = ProfitabilityInvoicelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ProfitabilityInvoice>();
        this.arraylist.addAll(ProfitabilityInvoicelist);

    }

    public class ViewHolder {
        TextView custname;
        TextView netinvoiceamt;
        TextView vinvoicedt;
        TextView invoiceno;
    }

    @Override
    public int getCount() {
        return ProfitabilityInvoicelist.size();
    }

    @Override
    public ProfitabilityInvoice getItem(int position) {
        return ProfitabilityInvoicelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_invoice_view_list, null);
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
        //holder.custname.setText(ProfitabilityInvoicelist.get(position).getcustname());
        //holder.netinvoiceamt.setText(ProfitabilityInvoicelist.get(position).getnetinvoiceamt());
        //holder.vinvoicedt.setText(ProfitabilityInvoicelist.get(position).getvinvoicedt());
        //holder.invoiceno.setText(ProfitabilityInvoicelist.get(position).getinvoiceno());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, Invoice_view_single.class);
               // intent.putExtra("sysinvno",(ProfitabilityInvoicelist.get(position).getsysinvno()));
                //intent.putExtra("custname",(ProfitabilityInvoicelist.get(position).getcustname()));
                //intent.putExtra("vinvoicedt",(ProfitabilityInvoicelist.get(position).getvinvoicedt()));
                //intent.putExtra("netinvoiceamt",(ProfitabilityInvoicelist.get(position).getnetinvoiceamt()));
                //intent.putExtra("invoiceno",(ProfitabilityInvoicelist.get(position).getinvoiceno()));
                //intent.putExtra("PURPOSE","");
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ProfitabilityInvoicelist.clear();
        if (charText.length() == 0) {
            ProfitabilityInvoicelist.addAll(arraylist);
        } else {
            for (ProfitabilityInvoice wp : arraylist) {
                if (wp.getcustomer_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    ProfitabilityInvoicelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}