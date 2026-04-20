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

public class ListViewAdapter_StockVerification_Model extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Models_Stock_Verification> Models_Stock_Verificationlist = null;
    private ArrayList<Models_Stock_Verification> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_Model(Context context,
                                                   List<Models_Stock_Verification> Models_Stock_Verificationlist) {
        mContext = context;
        this.Models_Stock_Verificationlist = Models_Stock_Verificationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Models_Stock_Verification>();
        this.arraylist.addAll(Models_Stock_Verificationlist);

        imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView modelcode;
        TextView sysproductcategoryno_top;
        TextView sysproductcategoryno_parent;
        TextView sysproductcategoryno;
        TextView topcategoryname;
        TextView parentcategoryname;
        TextView categoryname;
        TextView mrp;
        TextView dp;
        TextView slc;
        ImageView modelimage;
        TextView stock_stk;
        TextView stock_bkg;
        TextView stock_avl;


    }

    @Override
    public int getCount() {
        return Models_Stock_Verificationlist.size();
    }

    @Override
    public Models_Stock_Verification getItem(int position) {
        return Models_Stock_Verificationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_model_stock_verification_view_list, null);
            // Locate the TextViews in listview_item.xml
            holder.modelcode = (TextView) view.findViewById(R.id.modelcode);
            holder.topcategoryname = (TextView) view.findViewById(R.id.topcategoryname);
            holder.parentcategoryname = (TextView) view.findViewById(R.id.parentcategoryname);
            holder.categoryname = (TextView) view.findViewById(R.id.categoryname);

            holder.mrp = (TextView) view.findViewById(R.id.mrp);
            holder.dp = (TextView) view.findViewById(R.id.dp);
            holder.slc = (TextView) view.findViewById(R.id.slc);

            holder.modelimage = (ImageView) view.findViewById(R.id.modelimage);

            holder.stock_stk = (TextView) view.findViewById(R.id.stock_stk);
            holder.stock_bkg = (TextView) view.findViewById(R.id.stock_bkg);
            holder.stock_avl = (TextView) view.findViewById(R.id.stock_avl);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.modelcode.setText(Models_Stock_Verificationlist.get(position).getmodelcode());

        holder.topcategoryname.setText(Models_Stock_Verificationlist.get(position).gettopcategoryname());
        holder.parentcategoryname.setText(Models_Stock_Verificationlist.get(position).getparentcategoryname());
        holder.categoryname.setText(Models_Stock_Verificationlist.get(position).getcategoryname());

        holder.mrp.setText("Mrp : " + Models_Stock_Verificationlist.get(position).getmrp());
        holder.dp.setText("Dp : " + Models_Stock_Verificationlist.get(position).getdp());
        holder.slc.setText("SLC : " + Models_Stock_Verificationlist.get(position).getslc());

        holder.stock_stk.setText("In Stock : " + Models_Stock_Verificationlist.get(position).getstock_stk());
        holder.stock_bkg.setText("Booking : " + Models_Stock_Verificationlist.get(position).getstock_bkg());
        holder.stock_avl.setText("Available : " + Models_Stock_Verificationlist.get(position).getstock_avl());

        ImageView image = holder.modelimage;
        imageLoader.DisplayImage(Models_Stock_Verificationlist.get(position).getmodelimageurl(),  image);

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //

               Intent intent = new Intent(mContext, Salesmen_SalesOrder.class);
               intent.putExtra("sysmodelno",(Models_Stock_Verificationlist.get(position).getsysmodelno()));
               intent.putExtra("modelcode",(Models_Stock_Verificationlist.get(position).getmodelcode()));
               intent.putExtra("sysproductcategoryno_top",(Models_Stock_Verificationlist.get(position).getsysproductcategoryno_top()));
               intent.putExtra("sysproductcategoryno_parent",(Models_Stock_Verificationlist.get(position).getsysproductcategoryno_parent()));
               intent.putExtra("sysproductcategoryno",(Models_Stock_Verificationlist.get(position).getsysproductcategoryno()));
               intent.putExtra("topcategoryname",(Models_Stock_Verificationlist.get(position).gettopcategoryname()));
               intent.putExtra("parentcategoryname",(Models_Stock_Verificationlist.get(position).getparentcategoryname()));
               intent.putExtra("categoryname",(Models_Stock_Verificationlist.get(position).getcategoryname()));

               ((StockVerificationModel_view)mContext).setResult(StockVerificationModel_view.RESULT_OK,intent);
               ((StockVerificationModel_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Models_Stock_Verificationlist.clear();
        if (charText.length() == 0) {
            Models_Stock_Verificationlist.addAll(arraylist);
        } else {
            for (Models_Stock_Verification wp : arraylist) {
                if (wp.getsearchfield().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Models_Stock_Verificationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}