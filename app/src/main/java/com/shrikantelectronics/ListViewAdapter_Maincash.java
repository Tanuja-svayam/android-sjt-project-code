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

public class ListViewAdapter_Maincash extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Maincash> maincashdetails = null;
    private ArrayList<Maincash> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_Maincash(Context context,
                                    List<Maincash> maincashdetails) {
        mContext = context;
        this.maincashdetails = maincashdetails;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Maincash>();
        this.arraylist.addAll(maincashdetails);

        //imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {

        TextView bankcd;
        TextView name;
        TextView outstanding;
    }

    @Override
    public int getCount() {
        return maincashdetails.size();
    }

    @Override
    public Maincash getItem(int position) {
        return maincashdetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_maincash_view_list, null);

            holder.name= (TextView) view.findViewById(R.id.name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(maincashdetails.get(position).getname());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, Cash_Ledger_View.class);
                intent.putExtra("bankcd",(maincashdetails.get(position).getbankcd()));
                intent.putExtra("name",(maincashdetails.get(position).getname()));
                intent.putExtra("legdertype",(maincashdetails.get(position).getlegdertype()));

                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        maincashdetails.clear();
        if (charText.length() == 0) {
            maincashdetails.addAll(arraylist);
        } else {
            for (Maincash wp : arraylist) {
                if (wp.getname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    maincashdetails.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}