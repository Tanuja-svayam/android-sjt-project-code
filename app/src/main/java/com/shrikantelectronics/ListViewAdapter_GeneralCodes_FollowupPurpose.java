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

public class ListViewAdapter_GeneralCodes_FollowupPurpose extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<GeneralCodes> GeneralCodeslist = null;
    private ArrayList<GeneralCodes> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_GeneralCodes_FollowupPurpose(Context context,
                                                        List<GeneralCodes> GeneralCodeslist) {
        mContext = context;
        this.GeneralCodeslist = GeneralCodeslist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<GeneralCodes>();
        this.arraylist.addAll(GeneralCodeslist);

    }

    public class ViewHolder {
        TextView gcname;
    }

    @Override
    public int getCount() {
        return GeneralCodeslist.size();
    }

    @Override
    public GeneralCodes getItem(int position) {
        return GeneralCodeslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_customer_type_view_list, null);
            holder.gcname = (TextView) view.findViewById(R.id.gcname);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.gcname.setText(GeneralCodeslist.get(position).getgcname());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, Salesmen_Followup_Customer_Single.class);
               intent.putExtra("gccode",(GeneralCodeslist.get(position).getgccode()));
               intent.putExtra("gcname",(GeneralCodeslist.get(position).getgcname()));

                ((GeneralCodeFollowupPurpose_view)mContext).setResult(GeneralCodeFollowupPurpose_view.RESULT_OK,intent);
                ((GeneralCodeFollowupPurpose_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        GeneralCodeslist.clear();
        if (charText.length() == 0) {
            GeneralCodeslist.addAll(arraylist);
        } else {
            for (GeneralCodes wp : arraylist) {
                if (wp.getgcname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    GeneralCodeslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}