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

public class ListViewAdapter_StockVerification_State extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<State> Statelist = null;
    private ArrayList<State> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_State(Context context,
                                                   List<State> Statelist) {
        mContext = context;
        this.Statelist = Statelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<State>();
        this.arraylist.addAll(Statelist);

    }

    public class ViewHolder {
        TextView statename;
    }

    @Override
    public int getCount() {
        return Statelist.size();
    }

    @Override
    public State getItem(int position) {
        return Statelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_state_view_list, null);
            holder.statename = (TextView) view.findViewById(R.id.statename);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.statename.setText(Statelist.get(position).getstatename());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, Salesmen_SalesOrder.class);
               intent.putExtra("sysstateno",(Statelist.get(position).getsysstateno()));
               intent.putExtra("statename",(Statelist.get(position).getstatename()));

                ((StockVerificationState_view)mContext).setResult(StockVerificationState_view.RESULT_OK,intent);
                ((StockVerificationState_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Statelist.clear();
        if (charText.length() == 0) {
            Statelist.addAll(arraylist);
        } else {
            for (State wp : arraylist) {
                if (wp.getstatename().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Statelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}