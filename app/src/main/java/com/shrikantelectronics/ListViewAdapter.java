package com.shrikantelectronics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Models> Modelslist = null;
    private ArrayList<Models> arraylist;
    public ImageLoader imageLoader;

    private String searchQuery = "";   // 🔑 store current search query

    public ListViewAdapter(Context context,
                           List<Models> Modelslist) {
        mContext = context;
        this.Modelslist = Modelslist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Models>();
        this.arraylist.addAll(Modelslist);

        imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    // allow activity to update the query
    public void setSearchQuery(String query) {
        this.searchQuery = query != null ? query : "";
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
        return Modelslist.size();
    }

    @Override
    public Models getItem(int position) {
        return Modelslist.get(position);
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

        Models model = Modelslist.get(position);

        // highlight helper
        holder.modelcode.setText(getHighlightedText(model.getmodelcode(), searchQuery));
        holder.topcategoryname.setText(getHighlightedText(model.gettopcategoryname(), searchQuery));
        holder.parentcategoryname.setText(getHighlightedText(model.getparentcategoryname(), searchQuery));
        holder.categoryname.setText(getHighlightedText(model.getcategoryname(), searchQuery));

        holder.mrp.setText("Mrp : " + model.getmrp());
        holder.dp.setText("Dp : " + model.getdp());
        holder.slc.setText("SLC : " + model.getslc());

        holder.stock_stk.setText("In Stock : " + model.getstock_stk());
        holder.stock_bkg.setText("Booking : " + model.getstock_bkg());
        holder.stock_avl.setText("Available : " + model.getstock_avl());

        imageLoader.DisplayImage(model.getmodelimageurl(), holder.modelimage);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, model_view_single.class);
                intent.putExtra("sysmodelno", model.getsysmodelno());
                intent.putExtra("modelcode", model.getmodelcode());
                intent.putExtra("dp", model.getdp());
                intent.putExtra("mrp", model.getmrp());
                intent.putExtra("stock", model.getstock_stk());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Highlight function
    private CharSequence getHighlightedText(String originalText, String searchText) {
        if (originalText == null) return "";

        if (searchText != null && !searchText.isEmpty()) {
            String lowerOriginal = originalText.toLowerCase(Locale.getDefault());
            String lowerSearch = searchText.toLowerCase(Locale.getDefault());

            int start = lowerOriginal.indexOf(lowerSearch);
            if (start >= 0) {
                int end = start + lowerSearch.length();
                Spannable spannable = new SpannableString(originalText);
                spannable.setSpan(
                        new ForegroundColorSpan(Color.RED), // highlight color
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                return spannable;
            }
        }
        return originalText;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Modelslist.clear();
        if (charText.length() == 0) {
            Modelslist.addAll(arraylist);
        } else {
            for (Models wp : arraylist) {
                if (wp.getsearchfield().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Modelslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
