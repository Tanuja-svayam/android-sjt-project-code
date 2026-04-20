package com.shrikantelectronics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_OrderApprovalPending extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<OrderApprovalPending> Invoicelist = null;
    private ArrayList<OrderApprovalPending> arraylist;

    public ListViewAdapter_OrderApprovalPending(Context context,
                                                List<OrderApprovalPending> Invoicelist) {
        mContext = context;
        this.Invoicelist = Invoicelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<OrderApprovalPending>();
        this.arraylist.addAll(Invoicelist);

    }

    public class ViewHolder {
        TextView custname;
        TextView netinvoiceamt;
        TextView vinvoicedt;
        TextView invoiceno;
        TextView dbd_paid_by_customer;
        TextView dbd_amount;
        TextView approved_by_manager;
        TextView gross_slc_amount;
        TextView approvalreason;
        TextView gross_slc_deficit;
        TextView trntype;
        TextView sysinvorderno_pk;
    }

    @Override
    public int getCount() {
        return Invoicelist.size();
    }

    @Override
    public OrderApprovalPending getItem(int position) {
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
            holder.vinvoicedt = (TextView) view.findViewById(R.id.vinvoicedt);
            holder.invoiceno = (TextView) view.findViewById(R.id.invoiceno);
            holder.approvalreason = (TextView) view.findViewById(R.id.approvalreason);
            holder.netinvoiceamt = (TextView) view.findViewById(R.id.netinvoiceamt);
            holder.gross_slc_amount = (TextView) view.findViewById(R.id.gross_slc_amount);
            holder.gross_slc_deficit = (TextView) view.findViewById(R.id.gross_slc_deficit);
           // holder.trntype = (TextView) view.findViewById(R.id.trntype);
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
        holder.approvalreason.setText(Invoicelist.get(position).getapprovalreason());
        holder.gross_slc_amount.setText(Invoicelist.get(position).getgross_slc_amount());
        holder.gross_slc_deficit.setText(Invoicelist.get(position).getgross_slc_deficit());
       // holder.trntype.setText(Invoicelist.get(position).gettrntype());

        final double deficitamount = Double.valueOf(Invoicelist.get(position).getgross_slc_deficit());

        if(deficitamount<0)
        {
            holder.gross_slc_deficit.setTextColor(Color.RED);

        }
        else
        {
            holder.gross_slc_deficit.setTextColor(Color.GREEN);

        }

        //holder.dbd_paid_by_customer.setText(Invoicelist.get(position).getdbd_paid_by_customer());
        //holder.dbd_amount.setText(Invoicelist.get(position).getdbd_amount());
        //holder.approved_by_manager.setText(Invoicelist.get(position).getapproved_by_manager());


        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               if (Invoicelist.get(position).gettrntype().toString().equals("SLC"))
                {

                    Intent intent = new Intent(mContext, OrderApprovalPending_view_single.class);

                    intent.putExtra("sysinvorderno",(Invoicelist.get(position).getsysinvno()));
                    intent.putExtra("custname",(Invoicelist.get(position).getcustname()));
                    intent.putExtra("vinvoicedt",(Invoicelist.get(position).getvinvoicedt()));
                    intent.putExtra("netinvoiceamt",(Invoicelist.get(position).getnetinvoiceamt()));
                    intent.putExtra("invoiceno",(Invoicelist.get(position).getinvoiceno()));

                    intent.putExtra("dbd_paid_by_customer",(Invoicelist.get(position).getdbd_paid_by_customer()));
                    intent.putExtra("dbd_amount",(Invoicelist.get(position).getdbd_amount()));
                    intent.putExtra("approved_by_manager",(Invoicelist.get(position).getapproved_by_manager()));
                    intent.putExtra("gross_slc_amount",(Invoicelist.get(position).getgross_slc_amount()));
                    intent.putExtra("remarks",(Invoicelist.get(position).getremarks()));
                    intent.putExtra("approvalreason",(Invoicelist.get(position).getapprovalreason()));
                    intent.putExtra("gross_slc_deficit",(Invoicelist.get(position).getgross_slc_deficit()));

                    mContext.startActivity(intent);

                }
                if (Invoicelist.get(position).gettrntype().toString().equals("SRN"))
                {

                    Intent intent = new Intent(mContext, OrderApprovalPending_view_srn.class);

                    intent.putExtra("sysinvorderno",(Invoicelist.get(position).getsysinvno()));
                    intent.putExtra("custname",(Invoicelist.get(position).getcustname()));
                    intent.putExtra("vinvoicedt",(Invoicelist.get(position).getvinvoicedt()));
                    intent.putExtra("netinvoiceamt",(Invoicelist.get(position).getnetinvoiceamt()));
                    intent.putExtra("invoiceno",(Invoicelist.get(position).getinvoiceno()));

                    intent.putExtra("dbd_paid_by_customer",(Invoicelist.get(position).getdbd_paid_by_customer()));
                    intent.putExtra("dbd_amount",(Invoicelist.get(position).getdbd_amount()));
                    intent.putExtra("approved_by_manager",(Invoicelist.get(position).getapproved_by_manager()));
                    intent.putExtra("gross_slc_amount",(Invoicelist.get(position).getgross_slc_amount()));
                    intent.putExtra("remarks",(Invoicelist.get(position).getremarks()));
                    intent.putExtra("approvalreason",(Invoicelist.get(position).getapprovalreason()));
                    intent.putExtra("gross_slc_deficit",(Invoicelist.get(position).getgross_slc_deficit()));

                    mContext.startActivity(intent);
                }
                if (Invoicelist.get(position).gettrntype().toString().equals("COD"))
                {

                    Intent intent = new Intent(mContext, OrderApprovalPending_view_cod.class);

                    intent.putExtra("sysreceiptno",(Invoicelist.get(position).getsysinvno()));
                    intent.putExtra("custname",(Invoicelist.get(position).getcustname()));
                    intent.putExtra("vinvoicedt",(Invoicelist.get(position).getvinvoicedt()));
                    intent.putExtra("netinvoiceamt",(Invoicelist.get(position).getnetinvoiceamt()));
                    intent.putExtra("invoiceno",(Invoicelist.get(position).getinvoiceno()));

                    intent.putExtra("dbd_paid_by_customer",(Invoicelist.get(position).getdbd_paid_by_customer()));
                    intent.putExtra("dbd_amount",(Invoicelist.get(position).getdbd_amount()));
                    intent.putExtra("approved_by_manager",(Invoicelist.get(position).getapproved_by_manager()));
                    intent.putExtra("gross_slc_amount",(Invoicelist.get(position).getgross_slc_amount()));
                    intent.putExtra("remarks",(Invoicelist.get(position).getremarks()));
                    intent.putExtra("approvalreason",(Invoicelist.get(position).getapprovalreason()));
                    intent.putExtra("gross_slc_deficit",(Invoicelist.get(position).getgross_slc_deficit()));
                    intent.putExtra("sysinvorderno",(Invoicelist.get(position).getsysinvorderno_pk()));


                    mContext.startActivity(intent);
                }

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
            for (OrderApprovalPending wp : arraylist) {
                if (wp.getsearchfield().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Invoicelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}