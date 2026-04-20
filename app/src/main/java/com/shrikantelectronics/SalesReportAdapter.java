package com.shrikantelectronics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Intent;
import android.content.Context;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {

    private List<SalesReport> salesReportList;
    private Context mContext;

    public SalesReportAdapter(Context context, List<SalesReport> salesReportList) {
        this.mContext = context;
        this.salesReportList = salesReportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesReport salesReport = salesReportList.get(position);
        holder.tvModelName.setText("" + salesReport.getModelName());
        holder.tvCustomerName.setText("Customer: " + salesReport.getCustomerName());
        holder.tvQuantity.setText("Qty: " + salesReport.getQuantity());
        holder.tvAmount.setText("Amount: " + salesReport.getAmount());
        holder.tvLocation.setText("" + salesReport.getLocation());
       // holder.tvSalesExecutive.setText("Executive: " + salesReport.getSalesExecutive());
        holder.tvinvorderdt.setText("" + salesReport.getinvorderdt());
        holder.tvinvorderno.setText("" + salesReport.getinvorderno());
        holder.tvStock.setText("Stock : " + salesReport.getmodel_instock_qty());
        holder.tvProfit.setText("Proft/Loss : " + salesReport.getprofitloss());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, customer_view_single.class);
                intent.putExtra("custcd",salesReport.getcustcd());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return salesReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvModelName, tvCustomerName, tvQuantity, tvAmount, tvLocation, tvSalesExecutive, tvinvorderdt, tvinvorderno, tvStock,tvProfit ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModelName = itemView.findViewById(R.id.tvModelName);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvLocation = itemView.findViewById(R.id.tvLocation);
           // tvSalesExecutive = itemView.findViewById(R.id.tvSalesExecutive);
            tvinvorderdt= itemView.findViewById(R.id.tvinvorderdt);
            tvinvorderno = itemView.findViewById(R.id.tvinvorderno);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvProfit = itemView.findViewById(R.id.tvProfit);
        }
    }
}
