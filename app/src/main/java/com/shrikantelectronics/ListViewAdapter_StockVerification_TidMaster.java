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

public class ListViewAdapter_StockVerification_TidMaster extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<TidMaster> TidMasterlist = null;
    private ArrayList<TidMaster> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_TidMaster(Context context,
                                                       List<TidMaster> TidMasterlist) {
        mContext = context;
        this.TidMasterlist = TidMasterlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<TidMaster>();
        this.arraylist.addAll(TidMasterlist);

    }

    public class ViewHolder {
        TextView tiddescription;
    }

    @Override
    public int getCount() {
        return TidMasterlist.size();
    }

    @Override
    public TidMaster getItem(int position) {
        return TidMasterlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_tidmaster_view_list, null);
            holder.tiddescription = (TextView) view.findViewById(R.id.tiddescription);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tiddescription.setText(TidMasterlist.get(position).gettiddescription());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, StockVerificationTidMaster_view.class);
               intent.putExtra("systidno",(TidMasterlist.get(position).getsystidno()));
               intent.putExtra("tiddescription",(TidMasterlist.get(position).gettiddescription()));

                ((StockVerificationTidMaster_view)mContext).setResult(StockVerificationTidMaster_view.RESULT_OK,intent);
                ((StockVerificationTidMaster_view)mContext).finish();
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        TidMasterlist.clear();
        if (charText.length() == 0) {
            TidMasterlist.addAll(arraylist);
        } else {
            for (TidMaster wp : arraylist) {
                if (wp.gettiddescription().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    TidMasterlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}