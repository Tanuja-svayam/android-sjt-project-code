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

public class ListViewAdapter_Loan_EMI extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Loan_Emi> loanemidetails = null;
    private ArrayList<Loan_Emi> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Loan_EMI(Context context,
                                    List<Loan_Emi> loanemidetails) {
        mContext = context;
        this.loanemidetails = loanemidetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Loan_Emi>();
        this.arraylist.addAll(loanemidetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView sysemino;
        TextView sysloanno;
        TextView emino;
        TextView vemidt;
        TextView vemipaiddt;
        TextView vreceiptdt;
        TextView emiamt;
        TextView adjamt;
        TextView balamt;
        TextView overduedays;
        TextView overdue;
    }

    @Override
    public int getCount() {
        return loanemidetails.size();
    }

    @Override
    public Loan_Emi getItem(int position) {
        return loanemidetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_loan_emi_list, null);



           // holder.emino= (TextView) view.findViewById(R.id.emino);
            holder.vemidt= (TextView) view.findViewById(R.id.vemidt);
            holder.vemipaiddt= (TextView) view.findViewById(R.id.vemipaiddt);
            holder.vreceiptdt= (TextView) view.findViewById(R.id.vreceiptdt);
            holder.emiamt= (TextView) view.findViewById(R.id.emiamt);
            holder.adjamt= (TextView) view.findViewById(R.id.adjamt);
            holder.balamt= (TextView) view.findViewById(R.id.balamt);
            holder.overduedays= (TextView) view.findViewById(R.id.overduedays);
          //  holder.overdue= (TextView) view.findViewById(R.id.overdue);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        if(!loanemidetails.get(position).getoverduedays().equals("0")) {
            holder.overduedays.setBackgroundResource(R.color.pink);
        }

       // holder.sysemino.setText(loanemidetails.get(position).getsysemino());
       // holder.emino.setText(loanemidetails.get(position).getemino());
        holder.vemidt.setText(loanemidetails.get(position).getvemidt());
        holder.vemipaiddt.setText(loanemidetails.get(position).getvemipaiddt());
        holder.vreceiptdt.setText(loanemidetails.get(position).getvreceiptdt());
        holder.emiamt.setText(loanemidetails.get(position).getemiamt());
        holder.adjamt.setText(loanemidetails.get(position).getadjamt());
        holder.balamt.setText(loanemidetails.get(position).getbalamt());
        holder.overduedays.setText(loanemidetails.get(position).getoverduedays());


/*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, Invoice_view_single.class);

                intent.putExtra("sysemino",(loanemidetails.get(position).getsysemino()));
                intent.putExtra("custname","");
                intent.putExtra("netinvoiceamt","");
                intent.putExtra("vemino","");
                intent.putExtra("sysloanno","");
                intent.putExtra("PURPOSE","DOWNLOAD");

                mContext.startActivity(intent);
            }
        });
*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        loanemidetails.clear();
        if (charText.length() == 0) {
            loanemidetails.addAll(arraylist);
        } else {
            for (Loan_Emi wp : arraylist) {
                if (wp.getvreceiptdt().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    loanemidetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}