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

public class ListViewAdapter_SalesOrder extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<SalesOrder_Models> SalesOrder_Modelslist = null;
    private ArrayList<SalesOrder_Models> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_SalesOrder(Context context,
                                      List<SalesOrder_Models> SalesOrder_Modelslist) {
        mContext = context;
        this.SalesOrder_Modelslist = SalesOrder_Modelslist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SalesOrder_Models>();
        this.arraylist.addAll(SalesOrder_Modelslist);

        imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView modelcode;
        TextView mrp;
        TextView dp;
        TextView stock;
        ImageView modelimage;
    }

    @Override
    public int getCount() {
        return SalesOrder_Modelslist.size();
    }

    @Override
    public SalesOrder_Models getItem(int position) {
        return SalesOrder_Modelslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_model_view_list, null);
            // Locate the TextViews in listview_item.xml
            holder.modelcode = (TextView) view.findViewById(R.id.modelcode);
            holder.mrp = (TextView) view.findViewById(R.id.mrp);
            holder.dp = (TextView) view.findViewById(R.id.dp);
            holder.stock = (TextView) view.findViewById(R.id.stock);
            holder.modelimage = (ImageView) view.findViewById(R.id.modelimage);

            // Locate the ImageView in listview_item.xml

           // holder.modelimage = (ImageView) view.findViewById(R.id.modelimage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.modelcode.setText(SalesOrder_Modelslist.get(position).getmodelcode());
        holder.mrp.setText(SalesOrder_Modelslist.get(position).getmrp());
        holder.dp.setText(SalesOrder_Modelslist.get(position).getdp());
        holder.stock.setText(SalesOrder_Modelslist.get(position).getstock());

        //holder.modelimage.setImageResource(SalesOrder_Modelslist.get(position).getmodelimageurl());

       // String imageurlpath = "http://ecx.images-amazon.com/images/I/51Xa8eJnFWL._AC_SR160,160_.jpg";

        ImageView image = holder.modelimage;

        //DisplayImage function from ImageLoader Class
       imageLoader.DisplayImage(SalesOrder_Modelslist.get(position).getmodelimageurl(),  image);

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, SalesOrder_add_model.class);

                intent.putExtra("sysinvorderno",(SalesOrder_Modelslist.get(position).getsysinvorderno()));
                intent.putExtra("sysorderdtlno",(SalesOrder_Modelslist.get(position).getsysorderdtlno()));
                intent.putExtra("refbillno",(SalesOrder_Modelslist.get(position).getrefbillno()));
                intent.putExtra("custcd",(SalesOrder_Modelslist.get(position).getcustcd()));
                intent.putExtra("custname",(SalesOrder_Modelslist.get(position).getcustname()));
                intent.putExtra("sysmodelno",(SalesOrder_Modelslist.get(position).getmodelcodepk()));
                intent.putExtra("modelcode",(SalesOrder_Modelslist.get(position).getmodelcode()));
                intent.putExtra("vinvoicedt",(SalesOrder_Modelslist.get(position).getvinvoicedt()));
                intent.putExtra("netinvoiceamt",(SalesOrder_Modelslist.get(position).getnetinvoiceamt()));

                //intent.putExtra("mrp",(SalesOrder_Modelslist.get(position).getmrp()));
                //intent.putExtra("stock",(SalesOrder_Modelslist.get(position).getstock()));
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SalesOrder_Modelslist.clear();
        if (charText.length() == 0) {
            SalesOrder_Modelslist.addAll(arraylist);
        } else {
            for (SalesOrder_Models wp : arraylist) {
                if (wp.getsearchfield().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    SalesOrder_Modelslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}