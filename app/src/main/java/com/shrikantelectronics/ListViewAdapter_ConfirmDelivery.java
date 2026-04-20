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

public class ListViewAdapter_ConfirmDelivery extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<PendingDelivery> PendingDeliverylist = null;
    private ArrayList<PendingDelivery> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_ConfirmDelivery(Context context,
                                           List<PendingDelivery> PendingDeliverylist) {
        mContext = context;
        this.PendingDeliverylist = PendingDeliverylist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<PendingDelivery>();
        this.arraylist.addAll(PendingDeliverylist);

        imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView sysorderdtlno;
        TextView vinvorderdt;
        TextView invorderno;
        TextView custname;
        TextView custaddress;
        TextView custdeliveryaddress;
        TextView customermobileno;
        TextView custtelephone;
        TextView custemailid;
        TextView topcategoryname;
        TextView parentcategoryname;
        TextView categoryname;
        TextView brandname;
        TextView modelname;
        TextView serialno;
        TextView picklocationname;
        TextView vexpected_delivery_date;
        TextView delorderno;
        TextView transportcharges;
        TextView deliveryinstruction;
        TextView delivery_status;
    }

    @Override
    public int getCount() {
        return PendingDeliverylist.size();
    }

    @Override
    public PendingDelivery getItem(int position) {
        return PendingDeliverylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_pendingdelivery_view_list, null);

            holder.sysorderdtlno = (TextView) view.findViewById(R.id.sysorderdtlno);
            holder.vinvorderdt = (TextView) view.findViewById(R.id.vinvorderdt);
            holder.invorderno = (TextView) view.findViewById(R.id.invorderno);
            holder.custname = (TextView) view.findViewById(R.id.custname);
            holder.custaddress = (TextView) view.findViewById(R.id.custaddress);
            holder.custdeliveryaddress = (TextView) view.findViewById(R.id.custdeliveryaddress);
            holder.customermobileno = (TextView) view.findViewById(R.id.customermobileno);
            holder.custtelephone = (TextView) view.findViewById(R.id.custtelephone);
            holder.custemailid = (TextView) view.findViewById(R.id.custemailid);
            holder.topcategoryname = (TextView) view.findViewById(R.id.topcategoryname);
            holder.parentcategoryname = (TextView) view.findViewById(R.id.parentcategoryname);
            holder.categoryname = (TextView) view.findViewById(R.id.categoryname);
            holder.brandname = (TextView) view.findViewById(R.id.brandname);
            holder.modelname = (TextView) view.findViewById(R.id.modelname);
            holder.serialno = (TextView) view.findViewById(R.id.serialno);
            holder.picklocationname = (TextView) view.findViewById(R.id.picklocationname);
            holder.vexpected_delivery_date = (TextView) view.findViewById(R.id.vexpected_delivery_date);
            holder.delorderno = (TextView) view.findViewById(R.id.delorderno);
            holder.transportcharges = (TextView) view.findViewById(R.id.transportcharges);
            holder.deliveryinstruction = (TextView) view.findViewById(R.id.deliveryinstruction);
            holder.delivery_status= (TextView) view.findViewById(R.id.delivery_status);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews

       holder.sysorderdtlno.setText(PendingDeliverylist.get(position).getsysorderdtlno());
        holder.vinvorderdt.setText(PendingDeliverylist.get(position).getvinvorderdt());
        holder.invorderno.setText(PendingDeliverylist.get(position).getinvorderno());
        holder.custname.setText(PendingDeliverylist.get(position).getcustname());
        holder.custaddress.setText(PendingDeliverylist.get(position).getcustaddress());
        holder.custdeliveryaddress.setText(PendingDeliverylist.get(position).getcustdeliveryaddress());
        holder.customermobileno.setText(PendingDeliverylist.get(position).getcustomermobileno());
        holder.custtelephone.setText(PendingDeliverylist.get(position).getcusttelephone());
        holder.custemailid.setText(PendingDeliverylist.get(position).getcustemailid());
        holder.topcategoryname.setText(PendingDeliverylist.get(position).gettopcategoryname());
        holder.parentcategoryname.setText(PendingDeliverylist.get(position).getparentcategoryname());
        holder.categoryname.setText(PendingDeliverylist.get(position).getcategoryname());
        holder.brandname.setText(PendingDeliverylist.get(position).getbrandname());
        holder.modelname.setText(PendingDeliverylist.get(position).getmodelname());
        holder.serialno.setText(PendingDeliverylist.get(position).getserialno());
        holder.picklocationname.setText(PendingDeliverylist.get(position).getpicklocationname());
        holder.vexpected_delivery_date.setText(PendingDeliverylist.get(position).getvexpected_delivery_date());
        holder.delorderno.setText(PendingDeliverylist.get(position).getdelorderno());
        holder.transportcharges.setText(PendingDeliverylist.get(position).gettransportcharges());
        holder.deliveryinstruction.setText(PendingDeliverylist.get(position).getdeliveryinstruction());
        holder.delivery_status.setText(PendingDeliverylist.get(position).getdelivery_status());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, confirmdeliveryreceipt.class);

                intent.putExtra("sysorderdtlno",
                        (PendingDeliverylist.get(position).getsysorderdtlno()));
                // Pass all data modelcode
                intent.putExtra("custname",
                        (PendingDeliverylist.get(position).getcustname()));

                intent.putExtra("deliverycharges",
                        (PendingDeliverylist.get(position).gettransportcharges()));

                intent.putExtra("serialno",
                        (PendingDeliverylist.get(position).getserialno()));

                intent.putExtra("modelname",
                        (PendingDeliverylist.get(position).getmodelname()));

                intent.putExtra("brandname",
                        (PendingDeliverylist.get(position).getbrandname()));

                intent.putExtra("topcategoryname",
                        (PendingDeliverylist.get(position).gettopcategoryname()));

                intent.putExtra("invorderno",
                        (PendingDeliverylist.get(position).getinvorderno()));

                intent.putExtra("sysmodelno",
                        (PendingDeliverylist.get(position).getsysmodelno()));

                intent.putExtra("sysinvorderno",
                        (PendingDeliverylist.get(position).getsysinvorderno()));

                // Pass all data dp
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        PendingDeliverylist.clear();
        if (charText.length() == 0) {
            PendingDeliverylist.addAll(arraylist);
        } else {
            for (PendingDelivery wp : arraylist) {
                if (wp.getseachfield().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    PendingDeliverylist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}