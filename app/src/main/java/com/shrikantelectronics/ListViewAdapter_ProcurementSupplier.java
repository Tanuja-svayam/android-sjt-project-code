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

public class ListViewAdapter_ProcurementSupplier extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<ProcurementSupplier> ProcurementSupplierlist = null;
    private ArrayList<ProcurementSupplier> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_ProcurementSupplier(Context context,
                                               List<ProcurementSupplier> ProcurementSupplierlist) {
        mContext = context;
        this.ProcurementSupplierlist = ProcurementSupplierlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ProcurementSupplier>();
        this.arraylist.addAll(ProcurementSupplierlist);

    }

    public class ViewHolder {
        TextView description;
    }

    @Override
    public int getCount() {
        return ProcurementSupplierlist.size();
    }

    @Override
    public ProcurementSupplier getItem(int position) {
        return ProcurementSupplierlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_procurementsupplier_view_list, null);
            holder.description = (TextView) view.findViewById(R.id.description);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.description.setText(ProcurementSupplierlist.get(position).getdescription());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, StockVerification.class);
               intent.putExtra("sysvendorno",(ProcurementSupplierlist.get(position).getsysvendorno()));
               intent.putExtra("description",(ProcurementSupplierlist.get(position).getdescription()));

                ((ProcurementSupplier_view)mContext).setResult(ProcurementSupplier_view.RESULT_OK,intent);
                ((ProcurementSupplier_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ProcurementSupplierlist.clear();
        if (charText.length() == 0) {
            ProcurementSupplierlist.addAll(arraylist);
        } else {
            for (ProcurementSupplier wp : arraylist) {
                if (wp.getdescription().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    ProcurementSupplierlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}