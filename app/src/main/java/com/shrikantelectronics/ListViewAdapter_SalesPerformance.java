package com.shrikantelectronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_SalesPerformance extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<SalesPerformance> SalesPerformancelist = null;
    private ArrayList<SalesPerformance> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_SalesPerformance(Context context,
                                            List<SalesPerformance> SalesPerformancelist) {
        mContext = context;
        this.SalesPerformancelist = SalesPerformancelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SalesPerformance>();
        this.arraylist.addAll(SalesPerformancelist);

        imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView sysemployeeno;
        TextView fullname;
        TextView sale_month_1;
        TextView sale_monthname_1;
        TextView sale_month_2;
        TextView sale_monthname_2;
        TextView sale_month_3;
        TextView sale_monthname_3;
        TextView rank;
        ImageView imageurl;
    }

    @Override
    public int getCount() {
        return SalesPerformancelist.size();
    }

    @Override
    public SalesPerformance getItem(int position) {
        return SalesPerformancelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_salesperformance_view_list, null);
            // Locate the TextViews in listview_item.xml

            //holder.sysemployeeno = (TextView) view.findViewById(R.id.sysemployeeno);
            holder.fullname = (TextView) view.findViewById(R.id.fullname);
            holder.sale_month_1 = (TextView) view.findViewById(R.id.sale_month_1);
            holder.sale_monthname_1 = (TextView) view.findViewById(R.id.sale_monthname_1);
            holder.sale_month_2 = (TextView) view.findViewById(R.id.sale_month_2);
            holder.sale_monthname_2 = (TextView) view.findViewById(R.id.sale_monthname_2);
            holder.sale_month_3 = (TextView) view.findViewById(R.id.sale_month_3);
            holder.sale_monthname_3 = (TextView) view.findViewById(R.id.sale_monthname_3);
            holder.rank = (TextView) view.findViewById(R.id.rank);
            holder.imageurl = (ImageView) view.findViewById(R.id.imageurl);


            // Locate the ImageView in listview_item.xml

           // holder.modelimage = (ImageView) view.findViewById(R.id.modelimage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


      //  holder.sysemployeeno.setText(SalesPerformancelist.get(position).getsysemployeeno());
        holder.fullname.setText(SalesPerformancelist.get(position).getfullname());
        holder.sale_month_1.setText(SalesPerformancelist.get(position).getsale_month_1());
        holder.sale_monthname_1.setText(SalesPerformancelist.get(position).getsale_monthname_1());
        holder.sale_month_2.setText(SalesPerformancelist.get(position).getsale_month_2());
        holder.sale_monthname_2.setText(SalesPerformancelist.get(position).getsale_monthname_2());
        holder.sale_month_3.setText(SalesPerformancelist.get(position).getsale_month_3());
        holder.sale_monthname_3.setText(SalesPerformancelist.get(position).getsale_monthname_3());
        holder.rank.setText(SalesPerformancelist.get(position).getrank());


        //holder.modelimage.setImageResource(SalesPerformancelist.get(position).getmodelimageurl());
       // String imageurlpath = "http://ecx.images-amazon.com/images/I/51Xa8eJnFWL._AC_SR160,160_.jpg";

        ImageView image = holder.imageurl;

        //DisplayImage function from ImageLoader Class
       imageLoader.DisplayImage(SalesPerformancelist.get(position).getimageurl(),  image);

/*
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, model_view_single.class);

                intent.putExtra("sysmodelno",
                        (SalesPerformancelist.get(position).getmodelcodepk()));
                // Pass all data modelcode
                intent.putExtra("modelcode",
                        (SalesPerformancelist.get(position).getmodelcode()));
                // Pass all data dp
                intent.putExtra("dp",
                        (SalesPerformancelist.get(position).getdp()));
                // Pass all data mrp
                intent.putExtra("mrp",
                        (SalesPerformancelist.get(position).getmrp()));
                // Pass all data stock
                intent.putExtra("stock",
                        (SalesPerformancelist.get(position).getstock()));
                // Pass all data modelimage

                //intent.putExtra("modelimage",
                  //      (SalesPerformancelist.get(position).getmodelimage()));

                mContext.startActivity(intent);
            }
        });*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SalesPerformancelist.clear();
        if (charText.length() == 0) {
            SalesPerformancelist.addAll(arraylist);
        } else {
            for (SalesPerformance wp : arraylist) {
                if (wp.getfullname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    SalesPerformancelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}