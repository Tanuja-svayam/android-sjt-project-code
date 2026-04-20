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

public class ListViewAdapter_Dashboard_Todays_Sales extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Todays_Sales> Todays_SalesList = null;
    private ArrayList<Todays_Sales> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Dashboard_Todays_Sales(Context context,
                                                  List<Todays_Sales> Todays_SalesList) {
        mContext = context;
        this.Todays_SalesList = Todays_SalesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Todays_Sales>();
        this.arraylist.addAll(Todays_SalesList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView companycd;
        TextView stocklocation;
        TextView sales_target;
        TextView sales_mtd_target;
        TextView order_sales_date;
        TextView order_sales_ach;
        TextView op_sales_date;
        TextView op_sales_ach;
        TextView today_sales_ach;
        TextView today_sales_quantity;
        TextView sales_ach;
        TextView sales_total_gap;
        TextView sales_mtd_gap;
        TextView sales_kitty;
        TextView sales_ach_percent;

        TextView sales_month_name;
        TextView sales_month_target;
        TextView month_sales_ach;
        TextView sales_month_gap;
    }

    @Override
    public int getCount() {
        return Todays_SalesList.size();
    }

    @Override
    public Todays_Sales getItem(int position) {
        return Todays_SalesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dashboard_todays_sales_view, null);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);
            holder.sales_target = (TextView) view.findViewById(R.id.sales_target);
            holder.sales_mtd_target = (TextView) view.findViewById(R.id.sales_mtd_target);

            holder.order_sales_date = (TextView) view.findViewById(R.id.order_sales_date);
            holder.order_sales_ach = (TextView) view.findViewById(R.id.order_sales_ach);

            holder.op_sales_date = (TextView) view.findViewById(R.id.op_sales_date);
            holder.op_sales_ach = (TextView) view.findViewById(R.id.op_sales_ach);
            holder.today_sales_ach = (TextView) view.findViewById(R.id.today_sales_ach);
            holder.today_sales_quantity = (TextView) view.findViewById(R.id.today_sales_quantity);
            holder.sales_ach = (TextView) view.findViewById(R.id.sales_ach);
            holder.sales_total_gap = (TextView) view.findViewById(R.id.sales_total_gap);
            holder.sales_mtd_gap = (TextView) view.findViewById(R.id.sales_mtd_gap);
            holder.sales_kitty = (TextView) view.findViewById(R.id.sales_kitty);
            holder.sales_ach_percent= (TextView) view.findViewById(R.id.sales_ach_percent);

            holder.sales_month_name= (TextView) view.findViewById(R.id.sales_month_name);
            holder.sales_month_target= (TextView) view.findViewById(R.id.sales_month_target);
            holder.month_sales_ach= (TextView) view.findViewById(R.id.month_sales_ach);
            holder.sales_month_gap= (TextView) view.findViewById(R.id.sales_month_gap);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.stocklocation.setText(Todays_SalesList.get(position).getstocklocation ());
        holder.sales_target.setText(Todays_SalesList.get(position).getsales_target());
        holder.sales_mtd_target.setText(Todays_SalesList.get(position).getsales_mtd_target());
        holder.order_sales_date.setText(Todays_SalesList.get(position).getorder_sales_date());
        holder.order_sales_ach.setText(Todays_SalesList.get(position).getorder_sales_ach());
        holder.op_sales_date.setText(Todays_SalesList.get(position).getop_sales_date());
        holder.op_sales_ach.setText(Todays_SalesList.get(position).getop_sales_ach());
        holder.today_sales_ach.setText(Todays_SalesList.get(position).gettoday_sales_ach());
        holder.today_sales_quantity.setText(Todays_SalesList.get(position).gettoday_sales_quantity());
        holder.sales_ach.setText(Todays_SalesList.get(position).getsales_ach());
        holder.sales_total_gap.setText(Todays_SalesList.get(position).getsales_total_gap());
        holder.sales_mtd_gap.setText(Todays_SalesList.get(position).getsales_mtd_gap());
        holder.sales_kitty.setText(Todays_SalesList.get(position).getsales_kitty());
        holder.sales_ach_percent.setText(Todays_SalesList.get(position).getsales_ach_percent());

        holder.sales_month_name.setText(Todays_SalesList.get(position).getsales_month_name());
        holder.sales_month_target.setText(Todays_SalesList.get(position).getsales_month_target());
        holder.month_sales_ach.setText(Todays_SalesList.get(position).getmonth_sales_ach());
        holder.sales_month_gap.setText(Todays_SalesList.get(position).getsales_month_gap());
/*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, model_view_single.class);
                intent.putExtra("companycd",
                        (Todays_SalesList.get(position).getcompanycd()));
                intent.putExtra("stocklocation",
                        (Todays_SalesList.get(position).getstocklocation()));
                intent.putExtra("sales_mtd_target",
                        (Todays_SalesList.get(position).getsales_mtd_target()));
                intent.putExtra("creditcard",
                        (Todays_SalesList.get(position).getcreditcard()));
                intent.putExtra("finnance",
                        (Todays_SalesList.get(position).getfinnance()));
                intent.putExtra("today_sales_ach",
                        (Todays_SalesList.get(position).gettoday_sales_ach()));
                intent.putExtra("days_61_90",
                        (Todays_SalesList.get(position).getdays_61_90()));
                intent.putExtra("days_91_180",
                        (Todays_SalesList.get(position).getdays_91_180()));
                intent.putExtra("days_180_above",
                        (Todays_SalesList.get(position).getdays_180_above()));
                mContext.startActivity(intent);
            }
        });*/

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, SalesSummaryListViewTab.class);
                intent.putExtra("companycd",(Todays_SalesList.get(position).getcompanycd()));
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Todays_SalesList.clear();
        if (charText.length() == 0) {
            Todays_SalesList.addAll(arraylist);
        } else {
            for (Todays_Sales wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Todays_SalesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}