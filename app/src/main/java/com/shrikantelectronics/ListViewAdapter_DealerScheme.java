package com.shrikantelectronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter_DealerScheme extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<DealerScheme> DealerSchemeList = null;
    private ArrayList<DealerScheme> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_DealerScheme(Context context,
                                        List<DealerScheme> DealerSchemeList) {
        mContext = context;
        this.DealerSchemeList = DealerSchemeList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<DealerScheme>();
        this.arraylist.addAll(DealerSchemeList);

       // imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public class ViewHolder {
        TextView sysschemesno;
        TextView custname;
        TextView brandname;
        TextView vsupschfromdate;
        TextView vsupschtodate;
        TextView schemacode;
        TextView schemval;
        TextView valuationmethod;
        TextView targetvalue;
        TextView soldvalue;
        TextView returnvalue;
        TextView achievedvalue;
        TextView diffvalue;
        TextView value_net;
        TextView schemval_achieved;

    }

    @Override
    public int getCount() {
        return DealerSchemeList.size();
    }

    @Override
    public DealerScheme getItem(int position) {
        return DealerSchemeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_dealer_scheme_view, null);
            holder.custname = (TextView) view.findViewById(R.id.custname);
            holder.brandname = (TextView) view.findViewById(R.id.brandname);
            holder.vsupschfromdate = (TextView) view.findViewById(R.id.vsupschfromdate);
         //   holder.vsupschtodate = (TextView) view.findViewById(R.id.vsupschtodate);
            holder.schemacode = (TextView) view.findViewById(R.id.schemacode);
            holder.schemval = (TextView) view.findViewById(R.id.schemval);
           // holder.valuationmethod = (TextView) view.findViewById(R.id.valuationmethod);
            holder.targetvalue = (TextView) view.findViewById(R.id.targetvalue);
            holder.soldvalue= (TextView) view.findViewById(R.id.soldvalue);
            holder.returnvalue = (TextView) view.findViewById(R.id.returnvalue);
            holder.achievedvalue = (TextView) view.findViewById(R.id.achievedvalue);
            holder.diffvalue= (TextView) view.findViewById(R.id.diffvalue);
            holder.value_net= (TextView) view.findViewById(R.id.value_net);
            holder.schemval_achieved= (TextView) view.findViewById(R.id.schemval_achieved);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.custname.setText(DealerSchemeList.get(position).getcustname ());
        holder.brandname.setText(DealerSchemeList.get(position).getbrandname());
        holder.vsupschfromdate.setText(DealerSchemeList.get(position).getvsupschfromdate() + "-" + DealerSchemeList.get(position).getvsupschtodate());
       // holder.vsupschtodate.setText(DealerSchemeList.get(position).getvsupschtodate());
        holder.schemacode.setText(DealerSchemeList.get(position).getschemacode());
        holder.schemval.setText(DealerSchemeList.get(position).getschemval());
        holder.targetvalue.setText("(" + DealerSchemeList.get(position).getvaluationmethod() + ") " +  DealerSchemeList.get(position).gettargetvalue());
        holder.soldvalue.setText(DealerSchemeList.get(position).getsoldvalue());
        holder.returnvalue.setText(DealerSchemeList.get(position).getreturnvalue());
        holder.achievedvalue.setText(DealerSchemeList.get(position).getachievedvalue());
        holder.diffvalue.setText(DealerSchemeList.get(position).getdiffvalue());
        holder.value_net.setText(DealerSchemeList.get(position).getvalue_net());
        holder.schemval_achieved.setText(DealerSchemeList.get(position).getschemval_achieved());

      //  view.setOnClickListener(new View.OnClickListener() {
//
  //          @Override
    //        public void onClick(View arg0) {
//
  //              Intent intent = new Intent(mContext, SchemePurchaseList.class);
    //            intent.putExtra("sysschemesno",(DealerSchemeList.get(position).getsysschemesno()));
      //          mContext.startActivity(intent);
        //    }
        //});

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        DealerSchemeList.clear();
        if (charText.length() == 0) {
            DealerSchemeList.addAll(arraylist);
        } else {
            for (DealerScheme wp : arraylist) {
                if (wp.getcustname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    DealerSchemeList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}