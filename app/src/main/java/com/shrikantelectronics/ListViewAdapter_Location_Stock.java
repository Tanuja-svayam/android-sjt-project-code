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

public class ListViewAdapter_Location_Stock extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<LocationStockSummery> LocationStockSummeryList = null;
    private ArrayList<LocationStockSummery> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Location_Stock(Context context,
                                          List<LocationStockSummery> LocationStockSummeryList) {
        mContext = context;
        this.LocationStockSummeryList = LocationStockSummeryList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<LocationStockSummery>();
        this.arraylist.addAll(LocationStockSummeryList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView from_date		  ;
        TextView to_date		;
        TextView companycd		  ;
        TextView stocklocation		  ;
        TextView op_balquantity		  ;
        TextView op_valueprice		  ;
        TextView pu_quantity		  ;
        TextView pu_valueprice		  ;
        TextView tr_in_quantity		  ;
        TextView tr_in_valueprice	  ;
        TextView tr_out_quantity	;
        TextView tr_out_valueprice	  ;
        TextView sa_in_quantity		  ;
        TextView sa_in_valueprice	  ;
        TextView sa_out_quantity	;
        TextView sa_out_valueprice	  ;
        TextView sa_return_in_quantity	  ;
        TextView sa_return_in_valueprice;
        TextView sa_return_out_quantity	  ;
        TextView sa_return_out_valueprice ;
        TextView pu_return_in_quantity	  ;
        TextView pu_return_in_valueprice;
        TextView pu_return_out_quantity	  ;
        TextView pu_return_out_valueprice ;
        TextView cl_balquantity		  ;
        TextView cl_valueprice		  ;
    }

    @Override
    public int getCount() {
        return LocationStockSummeryList.size();
    }

    @Override
    public LocationStockSummery getItem(int position) {
        return LocationStockSummeryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_location_stock_summary_view_list, null);

            holder.from_date = (TextView) view.findViewById(R.id.from_date);
            holder.to_date = (TextView) view.findViewById(R.id.to_date);
            holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);
            holder.op_balquantity = (TextView) view.findViewById(R.id.op_balquantity);
            holder.pu_quantity = (TextView) view.findViewById(R.id.pu_quantity);
            holder.tr_in_quantity = (TextView) view.findViewById(R.id.tr_in_quantity);
            holder.tr_out_quantity = (TextView) view.findViewById(R.id.tr_out_quantity);
          //  holder.sa_in_quantity = (TextView) view.findViewById(R.id.sa_in_quantity);
            holder.sa_out_quantity = (TextView) view.findViewById(R.id.sa_out_quantity);
            holder.sa_return_in_quantity = (TextView) view.findViewById(R.id.sa_return_in_quantity);
         //   holder.sa_return_out_quantity = (TextView) view.findViewById(R.id.sa_return_out_quantity);
          //  holder.pu_return_in_quantity = (TextView) view.findViewById(R.id.pu_return_in_quantity);
            holder.pu_return_out_quantity = (TextView) view.findViewById(R.id.pu_return_out_quantity);
            holder.cl_balquantity = (TextView) view.findViewById(R.id.cl_balquantity);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.from_date.setText(LocationStockSummeryList.get(position).getfrom_date());
        holder.to_date.setText(LocationStockSummeryList.get(position).getto_date());
        holder.stocklocation.setText(LocationStockSummeryList.get(position).getstocklocation());
        holder.op_balquantity.setText(LocationStockSummeryList.get(position).getop_balquantity());
        holder.pu_quantity.setText(LocationStockSummeryList.get(position).getpu_quantity());
        holder.tr_in_quantity.setText(LocationStockSummeryList.get(position).gettr_in_quantity());
        holder.tr_out_quantity.setText(LocationStockSummeryList.get(position).gettr_out_quantity());
      //  holder.sa_in_quantity.setText(LocationStockSummeryList.get(position).getsa_in_quantity());
        holder.sa_out_quantity.setText(LocationStockSummeryList.get(position).getsa_out_quantity());
        holder.sa_return_in_quantity.setText(LocationStockSummeryList.get(position).getsa_return_in_quantity());
      //  holder.sa_return_out_quantity.setText(LocationStockSummeryList.get(position).getsa_return_out_quantity());
       // holder.pu_return_in_quantity.setText(LocationStockSummeryList.get(position).getpu_return_in_quantity());
        holder.pu_return_out_quantity.setText(LocationStockSummeryList.get(position).getpu_return_out_quantity());
        holder.cl_balquantity.setText(LocationStockSummeryList.get(position).getcl_balquantity());

/*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, model_view_single.class);
                intent.putExtra("companycd",
                        (LocationStockSummeryList.get(position).getcompanycd()));
                intent.putExtra("stocklocation",
                        (LocationStockSummeryList.get(position).getstocklocation()));
                intent.putExtra("purchase_value",
                        (LocationStockSummeryList.get(position).getpurchase_value()));
                intent.putExtra("totalqty",
                        (LocationStockSummeryList.get(position).gettotalqty()));
                intent.putExtra("days_30",
                        (LocationStockSummeryList.get(position).getdays_30()));
                intent.putExtra("days_31_60",
                        (LocationStockSummeryList.get(position).getdays_31_60()));
                intent.putExtra("days_61_90",
                        (LocationStockSummeryList.get(position).getdays_61_90()));
                intent.putExtra("days_91_180",
                        (LocationStockSummeryList.get(position).getdays_91_180()));
                intent.putExtra("days_180_above",
                        (LocationStockSummeryList.get(position).getdays_180_above()));
                mContext.startActivity(intent);
            }
        });*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        LocationStockSummeryList.clear();
        if (charText.length() == 0) {
            LocationStockSummeryList.addAll(arraylist);
        } else {
            for (LocationStockSummery wp : arraylist) {
                if (wp.getstocklocation().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    LocationStockSummeryList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}