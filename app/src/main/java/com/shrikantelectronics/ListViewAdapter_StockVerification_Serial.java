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

public class ListViewAdapter_StockVerification_Serial extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Serial_Stock_Verification> Serial_Stock_Verificationlist = null;
    private ArrayList<Serial_Stock_Verification> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_Serial(Context context,
                                                    List<Serial_Stock_Verification> Serial_Stock_Verificationlist) {
        mContext = context;
        this.Serial_Stock_Verificationlist = Serial_Stock_Verificationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Serial_Stock_Verification>();
        this.arraylist.addAll(Serial_Stock_Verificationlist);

      //  imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView serialno;
        TextView barcodeno;
        TextView modelname;
        TextView brandname;
        TextView stocklocation;
        TextView sysproductno;
        TextView sysbrandno;

    }

    @Override
    public int getCount() {
        return Serial_Stock_Verificationlist.size();
    }

    @Override
    public Serial_Stock_Verification getItem(int position) {
        return Serial_Stock_Verificationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_serial_stock_verification_view_list, null);
            // Locate the TextViews in listview_item.xml
            holder.serialno = (TextView) view.findViewById(R.id.serialno);
        //    holder.barcodeno = (TextView) view.findViewById(R.id.barcodeno);
            //holder.barcodeno = (TextView) view.findViewById(R.id.barcodeno);
            holder.modelname = (TextView) view.findViewById(R.id.modelname);
            //holder.brandname = (TextView) view.findViewById(R.id.brandname);
            //holder.stocklocation = (TextView) view.findViewById(R.id.stocklocation);
            //holder.sysproductno = (ImageView) view.findViewById(R.id.sysproductno);

            // Locate the ImageView in listview_item.xml

            // holder.sysproductno = (ImageView) view.findViewById(R.id.sysproductno);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.serialno.setText(Serial_Stock_Verificationlist.get(position).getserialno());
      //  holder.barcodeno.setText(Serial_Stock_Verificationlist.get(position).getbarcodeno());
       // holder.barcodeno.setText(Serial_Stock_Verificationlist.get(position).getbarcodeno());
        holder.modelname.setText(Serial_Stock_Verificationlist.get(position).getmodelname());
        //holder.brandname.setText(Serial_Stock_Verificationlist.get(position).getbrandname());
        //holder.stocklocation.setText(Serial_Stock_Verificationlist.get(position).getstocklocation());
        ///holder.sysproductno.setText(Serial_Stock_Verificationlist.get(position).getsysproductno());
        //holder.sysbrandno.setText(Serial_Stock_Verificationlist.get(position).getsysbrandno());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //

               Intent intent = new Intent(mContext, StockVerificationSerial_view.class);
               intent.putExtra("sysproductno",(Serial_Stock_Verificationlist.get(position).getsysproductno()));
               intent.putExtra("serialno",(Serial_Stock_Verificationlist.get(position).getserialno()));
               intent.putExtra("barcodeno",(Serial_Stock_Verificationlist.get(position).getbarcodeno()));
               intent.putExtra("modelname",(Serial_Stock_Verificationlist.get(position).getmodelname()));
               intent.putExtra("brandname",(Serial_Stock_Verificationlist.get(position).getbrandname()));
               intent.putExtra("stocklocation",(Serial_Stock_Verificationlist.get(position).getstocklocation()));
               intent.putExtra("sysproductno",(Serial_Stock_Verificationlist.get(position).getsysproductno()));
               intent.putExtra("sysbrandno",(Serial_Stock_Verificationlist.get(position).getsysbrandno()));

               ((StockVerificationSerial_view)mContext).setResult(StockVerificationSerial_view.RESULT_OK,intent);
               ((StockVerificationSerial_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Serial_Stock_Verificationlist.clear();
        if (charText.length() == 0) {
            Serial_Stock_Verificationlist.addAll(arraylist);
        } else {
            for (Serial_Stock_Verification wp : arraylist) {
                if (wp.getsearchserial().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Serial_Stock_Verificationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}