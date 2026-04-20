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

public class ListViewAdapter_Dashboard_SchemePurchase extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<SchemePurchase> SchemePurchaseList = null;
    private ArrayList<SchemePurchase> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_SchemePurchase(Context context,
                                                    List<SchemePurchase> SchemePurchaseList) {
        mContext = context;
        this.SchemePurchaseList = SchemePurchaseList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SchemePurchase>();
        this.arraylist.addAll(SchemePurchaseList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView sysschemesno;
        TextView vendorname;
        TextView brandname;
        TextView modelname;
        TextView vpurorderdt;
        TextView schemacode;
        TextView refdocumentno;
        TextView quantity;
        TextView grossamt;
    }

    @Override
    public int getCount() {
        return SchemePurchaseList.size();
    }

    @Override
    public SchemePurchase getItem(int position) {
        return SchemePurchaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_supplierscheme_purchase_view, null);
            holder.vendorname = (TextView) view.findViewById(R.id.vendorname);
            holder.brandname = (TextView) view.findViewById(R.id.brandname);
            holder.modelname = (TextView) view.findViewById(R.id.modelname);
            holder.vpurorderdt = (TextView) view.findViewById(R.id.vpurorderdt);
            holder.schemacode = (TextView) view.findViewById(R.id.schemacode);
            holder.refdocumentno = (TextView) view.findViewById(R.id.refdocumentno);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.grossamt = (TextView) view.findViewById(R.id.grossamt);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.vendorname.setText(SchemePurchaseList.get(position).getvendorname ());
        holder.brandname.setText(SchemePurchaseList.get(position).getbrandname());
        holder.modelname.setText(SchemePurchaseList.get(position).getmodelname());
        holder.vpurorderdt.setText(SchemePurchaseList.get(position).getvpurorderdt());
        holder.schemacode.setText(SchemePurchaseList.get(position).getschemacode());
        holder.refdocumentno.setText(SchemePurchaseList.get(position).getrefdocumentno());
        holder.quantity.setText(SchemePurchaseList.get(position).getquantity());
        holder.grossamt.setText(SchemePurchaseList.get(position).getgrossamt());

/*
       view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, model_view_single.class);
                intent.putExtra("sysschemesno",
                        (SchemePurchaseList.get(position).getsysschemesno()));
                intent.putExtra("vendorname",
                        (SchemePurchaseList.get(position).getvendorname()));
                intent.putExtra("vsupschfromdate",
                        (SchemePurchaseList.get(position).getvsupschfromdate()));
                intent.putExtra("creditcard",
                        (SchemePurchaseList.get(position).getcreditcard()));
                intent.putExtra("finnance",
                        (SchemePurchaseList.get(position).getfinnance()));
                intent.putExtra("passforslc",
                        (SchemePurchaseList.get(position).getpassforslc()));
                intent.putExtra("days_61_90",
                        (SchemePurchaseList.get(position).getdays_61_90()));
                intent.putExtra("days_91_180",
                        (SchemePurchaseList.get(position).getdays_91_180()));
                intent.putExtra("days_180_above",
                        (SchemePurchaseList.get(position).getdays_180_above()));
                mContext.startActivity(intent);
            }
        });

*/
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SchemePurchaseList.clear();
        if (charText.length() == 0) {
            SchemePurchaseList.addAll(arraylist);
        } else {
            for (SchemePurchase wp : arraylist) {
                if (wp.getschemacode().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    SchemePurchaseList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}