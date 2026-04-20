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

public class ListViewAdapter_StockVerification_Employee extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Employee> Employeelist = null;
    private ArrayList<Employee> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_Employee(Context context,
                                                      List<Employee> Employeelist) {
        mContext = context;
        this.Employeelist = Employeelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Employee>();
        this.arraylist.addAll(Employeelist);

    }

    public class ViewHolder {
        TextView fullname;
    }

    @Override
    public int getCount() {
        return Employeelist.size();
    }

    @Override
    public Employee getItem(int position) {
        return Employeelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_employee_view_list, null);
            holder.fullname = (TextView) view.findViewById(R.id.fullname);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.fullname.setText(Employeelist.get(position).getfullname());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, Salesmen_SalesOrder.class);
               intent.putExtra("sysemployeeno",(Employeelist.get(position).getsysemployeeno()));
               intent.putExtra("fullname",(Employeelist.get(position).getfullname()));

                ((StockVerificationEmployee_view)mContext).setResult(StockVerificationEmployee_view.RESULT_OK,intent);
                ((StockVerificationEmployee_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Employeelist.clear();
        if (charText.length() == 0) {
            Employeelist.addAll(arraylist);
        } else {
            for (Employee wp : arraylist) {
                if (wp.getfullname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Employeelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}