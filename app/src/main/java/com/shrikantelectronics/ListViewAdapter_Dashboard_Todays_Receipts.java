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

public class ListViewAdapter_Dashboard_Todays_Receipts extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Todays_Receipts> Todays_ReceiptsList = null;
    private ArrayList<Todays_Receipts> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_Todays_Receipts(Context context,
                                                     List<Todays_Receipts> Todays_ReceiptsList) {
        mContext = context;
        this.Todays_ReceiptsList = Todays_ReceiptsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Todays_Receipts>();
        this.arraylist.addAll(Todays_ReceiptsList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView companycd;
        TextView stocklocation;

        TextView cash_lastdate;
        TextView cash;

        TextView cheque_lastdate;
        TextView cheque;

        TextView clearcheque_lastdate;
        TextView clearcheque;

        TextView creditcard_lastdate;
        TextView creditcard;

        TextView finnance_lastdate;
        TextView finnance;

        TextView total;
    }

    @Override
    public int getCount() {
        return Todays_ReceiptsList.size();
    }

    @Override
    public Todays_Receipts getItem(int position) {
        return Todays_ReceiptsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dashboard_todays_receipts_view, null);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);

            holder.cash_lastdate = (TextView) view.findViewById(R.id.cash_lastdate);
            holder.cash = (TextView) view.findViewById(R.id.cash);

            holder.cheque_lastdate = (TextView) view.findViewById(R.id.cheque_lastdate);
            holder.cheque = (TextView) view.findViewById(R.id.cheque);

            holder.clearcheque_lastdate = (TextView) view.findViewById(R.id.clearcheque_lastdate);
            holder.clearcheque = (TextView) view.findViewById(R.id.clearcheque);

            holder.creditcard_lastdate = (TextView) view.findViewById(R.id.creditcard_lastdate);
            holder.creditcard = (TextView) view.findViewById(R.id.creditcard);

            holder.finnance_lastdate = (TextView) view.findViewById(R.id.finnance_lastdate);
            holder.finnance = (TextView) view.findViewById(R.id.finnance);

            holder.total = (TextView) view.findViewById(R.id.total);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.stocklocation.setText(Todays_ReceiptsList.get(position).getstocklocation ());

        holder.cash_lastdate.setText(Todays_ReceiptsList.get(position).getcash_lastdate());
        holder.cash.setText(Todays_ReceiptsList.get(position).getcash());

        holder.cheque_lastdate.setText(Todays_ReceiptsList.get(position).getcheque_lastdate());
        holder.cheque.setText(Todays_ReceiptsList.get(position).getcheque());

        holder.clearcheque_lastdate.setText(Todays_ReceiptsList.get(position).getclearcheque_lastdate());
        holder.clearcheque.setText(Todays_ReceiptsList.get(position).getclearcheque());

        holder.creditcard_lastdate.setText(Todays_ReceiptsList.get(position).getcreditcard_lastdate());
        holder.creditcard.setText(Todays_ReceiptsList.get(position).getcreditcard());

        holder.finnance_lastdate.setText(Todays_ReceiptsList.get(position).getfinnance_lastdate());
        holder.finnance.setText(Todays_ReceiptsList.get(position).getfinnance());

        holder.total.setText(Todays_ReceiptsList.get(position).gettotal());

        /*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, model_view_single.class);
                intent.putExtra("companycd",
                        (Todays_ReceiptsList.get(position).getcompanycd()));
                intent.putExtra("stocklocation",
                        (Todays_ReceiptsList.get(position).getstocklocation()));
                intent.putExtra("cash",
                        (Todays_ReceiptsList.get(position).getcash()));
                intent.putExtra("creditcard",
                        (Todays_ReceiptsList.get(position).getcreditcard()));
                intent.putExtra("finnance",
                        (Todays_ReceiptsList.get(position).getfinnance()));
                intent.putExtra("total",
                        (Todays_ReceiptsList.get(position).gettotal()));
                intent.putExtra("days_61_90",
                        (Todays_ReceiptsList.get(position).getdays_61_90()));
                intent.putExtra("days_91_180",
                        (Todays_ReceiptsList.get(position).getdays_91_180()));
                intent.putExtra("days_180_above",
                        (Todays_ReceiptsList.get(position).getdays_180_above()));
                mContext.startActivity(intent);
            }
        });*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Todays_ReceiptsList.clear();
        if (charText.length() == 0) {
            Todays_ReceiptsList.addAll(arraylist);
        } else {
            for (Todays_Receipts wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Todays_ReceiptsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}