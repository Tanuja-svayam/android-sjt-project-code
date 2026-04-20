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

public class ListViewAdapter_StockVerification_ParentCategory extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<ProductCategory> ProductCategorylist = null;
    private ArrayList<ProductCategory> arraylist;
    public ImageLoader imageLoader;

    public ListViewAdapter_StockVerification_ParentCategory(Context context,
                                                            List<ProductCategory> ProductCategorylist) {
        mContext = context;
        this.ProductCategorylist = ProductCategorylist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ProductCategory>();
        this.arraylist.addAll(ProductCategorylist);

    }

    public class ViewHolder {
        TextView description;
    }

    @Override
    public int getCount() {
        return ProductCategorylist.size();
    }

    @Override
    public ProductCategory getItem(int position) {
        return ProductCategorylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_productcategory_view_list, null);
            holder.description = (TextView) view.findViewById(R.id.description);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.description.setText(ProductCategorylist.get(position).getdescription());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

               Intent intent = new Intent(mContext, StockVerification.class);
               intent.putExtra("sysproductcategoryno",(ProductCategorylist.get(position).getsysproductcategoryno()));
               intent.putExtra("description",(ProductCategorylist.get(position).getdescription()));

                ((StockVerificationParentCategory_view)mContext).setResult(StockVerificationParentCategory_view.RESULT_OK,intent);
                ((StockVerificationParentCategory_view)mContext).finish();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ProductCategorylist.clear();
        if (charText.length() == 0) {
            ProductCategorylist.addAll(arraylist);
        } else {
            for (ProductCategory wp : arraylist) {
                if (wp.getdescription().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    ProductCategorylist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}