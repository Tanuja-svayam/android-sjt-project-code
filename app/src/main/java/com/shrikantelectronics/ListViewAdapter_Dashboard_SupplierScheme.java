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

public class ListViewAdapter_Dashboard_SupplierScheme extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<SupplierScheme> SupplierSchemeList = null;
    private ArrayList<SupplierScheme> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_SupplierScheme(Context context,
                                                    List<SupplierScheme> SupplierSchemeList) {
        mContext = context;
        this.SupplierSchemeList = SupplierSchemeList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SupplierScheme>();
        this.arraylist.addAll(SupplierSchemeList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView sysschemesno;
        TextView vendorname;
        TextView brandname;
        TextView vsupschfromdate;
        TextView vsupschtodate;
        TextView schemacode;
        TextView schematype;
        TextView schemval;
        TextView passforslc;
        TextView statusdesc;
        TextView product_added;
        TextView targetvalue;
        TextView quantity_purchased;
        TextView value_purchased;
        TextView quantity_sold;
        TextView value_sold;
        TextView valuationmethod;
        TextView pending_target_value;
        TextView pending_target_percent;
    }

    @Override
    public int getCount() {
        return SupplierSchemeList.size();
    }

    @Override
    public SupplierScheme getItem(int position) {
        return SupplierSchemeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dashboard_supplierscheme_view, null);
            holder.vendorname = (TextView) view.findViewById(R.id.vendorname);
            holder.brandname = (TextView) view.findViewById(R.id.brandname);
            holder.vsupschfromdate = (TextView) view.findViewById(R.id.vsupschfromdate);
            //holder.vsupschtodate = (TextView) view.findViewById(R.id.vsupschtodate);
            holder.schemacode = (TextView) view.findViewById(R.id.schemacode);
            holder.schematype = (TextView) view.findViewById(R.id.schematype);
            holder.schemval = (TextView) view.findViewById(R.id.schemval);
            holder.passforslc = (TextView) view.findViewById(R.id.passforslc);
            holder.product_added = (TextView) view.findViewById(R.id.product_added);
            holder.targetvalue = (TextView) view.findViewById(R.id.targetvalue);
            holder.quantity_purchased = (TextView) view.findViewById(R.id.quantity_purchased);
            holder.value_purchased= (TextView) view.findViewById(R.id.value_purchased);
            holder.quantity_sold = (TextView) view.findViewById(R.id.quantity_sold);
            holder.value_sold= (TextView) view.findViewById(R.id.value_sold);
            holder.pending_target_value= (TextView) view.findViewById(R.id.pending_target_value);
            holder.pending_target_percent= (TextView) view.findViewById(R.id.pending_target_percent);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.vendorname.setText(SupplierSchemeList.get(position).getvendorname ());
        holder.brandname.setText(SupplierSchemeList.get(position).getbrandname());
        holder.vsupschfromdate.setText(SupplierSchemeList.get(position).getvsupschfromdate() + "-" + SupplierSchemeList.get(position).getvsupschtodate());
        //holder.vsupschtodate.setText(SupplierSchemeList.get(position).getvsupschtodate());
        holder.schemacode.setText(SupplierSchemeList.get(position).getschemacode());
        holder.schematype.setText(SupplierSchemeList.get(position).getschematype());
        holder.schemval.setText(SupplierSchemeList.get(position).getschemval());
        holder.passforslc.setText(SupplierSchemeList.get(position).getpassforslc());
        holder.product_added.setText(SupplierSchemeList.get(position).getproduct_added());
        holder.targetvalue.setText("(" + SupplierSchemeList.get(position).getvaluationmethod() + ") " +  SupplierSchemeList.get(position).gettargetvalue());
        holder.quantity_purchased.setText(SupplierSchemeList.get(position).getquantity_purchased());
        holder.value_purchased.setText(SupplierSchemeList.get(position).getvalue_purchased());
        holder.quantity_sold.setText(SupplierSchemeList.get(position).getquantity_sold());
        holder.value_sold.setText(SupplierSchemeList.get(position).getvalue_sold());
        holder.pending_target_value.setText(SupplierSchemeList.get(position).getpending_target_value());
        holder.pending_target_percent.setText(SupplierSchemeList.get(position).getpending_target_percent());


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, SchemePurchaseList.class);
                intent.putExtra("sysschemesno",(SupplierSchemeList.get(position).getsysschemesno()));
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SupplierSchemeList.clear();
        if (charText.length() == 0) {
            SupplierSchemeList.addAll(arraylist);
        } else {
            for (SupplierScheme wp : arraylist) {
                if (wp.getschemacode().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    SupplierSchemeList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}