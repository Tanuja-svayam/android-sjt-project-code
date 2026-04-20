package com.shrikantelectronics;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;

public class RecyclerAdapter_Invoice extends RecyclerView.Adapter<RecyclerAdapter_Invoice.ViewHolder> {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<ProfitabilityInvoice> Invoicelist = null;
    private ArrayList<ProfitabilityInvoice> arraylist;
    public ImageLoader imageLoader;
    Dialog dialog;


    public RecyclerAdapter_Invoice(Context context, List<ProfitabilityInvoice> Invoicelist) {
        mContext = context;
        this.Invoicelist = Invoicelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ProfitabilityInvoice>();
        this.arraylist.addAll(Invoicelist);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;

        public TextView model;
        public TextView serialno;
        public TextView customer_name;
        public TextView invoice_date;
        public TextView invoice_no;
        public TextView executive_name;


        public TextView unitprice_include_tax;
        public TextView purchaseprice		;
        public TextView gp			;
        public TextView invoiceorder_creditnote_amount  ;
        public TextView transportcharges;
        public TextView        installationcharges	      ;
        public TextView bankcharges		      ;
        public TextView overheads		;
        public TextView exchangeshortexcess	      ;
        public TextView salesexpenses		      ;
        public TextView pricedropvalue		      ;
        public TextView cdsupport		;
        public TextView selloutsupport		      ;
        public TextView general_creditnote	;
        public TextView scheme_creditnote	;
        public TextView purchasediscount	;
        public TextView netsalesamount		      ;
        public TextView net_nlc_including_pd_so	      ;
        public TextView profitloss		;

        public Button download_invoice;

        public ViewHolder(View itemView) {
            super(itemView);

            model = (TextView)itemView.findViewById(R.id.model);
            serialno= (TextView)itemView.findViewById(R.id.serialno);
           // customer_name = (TextView)itemView.findViewById(R.id.customer_name);
            invoice_date= (TextView)itemView.findViewById(R.id.invoice_date);
            invoice_no = (TextView)itemView.findViewById(R.id.invoice_no);
            executive_name = (TextView)itemView.findViewById(R.id.executive_name);

            unitprice_include_tax	      = (TextView)itemView.findViewById(R.id.unitprice_include_tax);
            purchaseprice		      = (TextView)itemView.findViewById(R.id.purchaseprice);
            gp				      = (TextView)itemView.findViewById(R.id.gp);
            invoiceorder_creditnote_amount   = (TextView)itemView.findViewById(R.id.invoiceorder_creditnote_amount);
            transportcharges		      = (TextView)itemView.findViewById(R.id.transportcharges);
            installationcharges	      = (TextView)itemView.findViewById(R.id.installationcharges);
            bankcharges		      = (TextView)itemView.findViewById(R.id.bankcharges);
            overheads			      = (TextView)itemView.findViewById(R.id.overheads);
            exchangeshortexcess	      = (TextView)itemView.findViewById(R.id.exchangeshortexcess);
            salesexpenses		      = (TextView)itemView.findViewById(R.id.salesexpenses);
            pricedropvalue		      = (TextView)itemView.findViewById(R.id.pricedropvalue);
            cdsupport			      = (TextView)itemView.findViewById(R.id.cdsupport);
            selloutsupport		      = (TextView)itemView.findViewById(R.id.selloutsupport);
            general_creditnote		      = (TextView)itemView.findViewById(R.id.general_creditnote);
            scheme_creditnote		      = (TextView)itemView.findViewById(R.id.scheme_creditnote);
            purchasediscount		      = (TextView)itemView.findViewById(R.id.purchasediscount);
            netsalesamount		      = (TextView)itemView.findViewById(R.id.netsalesamount);
            net_nlc_including_pd_so	      = (TextView)itemView.findViewById(R.id.net_nlc_including_pd_so);
            profitloss			      = (TextView)itemView.findViewById(R.id.profitloss);

           // download_invoice= (Button)itemView.findViewById(R.id.download_invoice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    //dialog.show();

                    /*
                    Intent intent = new Intent(mContext, Invoice_view_single.class);

                    intent.putExtra("sysinvno",(Invoicelist.get(position).getid()));
                    intent.putExtra("custname", "");
                    intent.putExtra("netinvoiceamt", "");
                    intent.putExtra("vinvoicedt", "");
                    intent.putExtra("invoiceno", "");
                    intent.putExtra("PURPOSE","DOWNLOAD");
                   // mContext.startActivity(intent);
                    */
                }
            });
/*
          download_invoice.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(mContext, Invoice_view_single.class);
                    intent.putExtra("sysinvno",(Invoicelist.get(position).getid()));
                    intent.putExtra("custname", "");
                    intent.putExtra("netinvoiceamt", "");
                    intent.putExtra("vinvoicedt", "");
                    intent.putExtra("invoiceno", "");
                    intent.putExtra("PURPOSE","DOWNLOAD");
                    mContext.startActivity(intent);

                }
            });
          */
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout_invoice, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.model.setText(Invoicelist.get(i).getmodel());
        viewHolder.serialno.setText(Invoicelist.get(i).getserial_no());
      //  viewHolder.customer_name.setText(Invoicelist.get(i).getcustomer_name());
        viewHolder.invoice_no.setText(Invoicelist.get(i).getinvoice_no());
        viewHolder.invoice_date.setText(Invoicelist.get(i).getinvoice_date());
        viewHolder.executive_name.setText(Invoicelist.get(i).getexecutive_name());
        viewHolder.unitprice_include_tax.setText(Invoicelist.get(i).getunitprice_include_tax());
        viewHolder.purchaseprice.setText(Invoicelist.get(i).getpurchaseprice());
        viewHolder.gp.setText(Invoicelist.get(i).getgp());
        viewHolder.invoiceorder_creditnote_amount.setText(Invoicelist.get(i).getinvoiceorder_creditnote_amount());
        viewHolder.transportcharges.setText(Invoicelist.get(i).gettransportcharges());
        viewHolder.installationcharges.setText(Invoicelist.get(i).getinstallationcharges());
        viewHolder.bankcharges.setText(Invoicelist.get(i).getbankcharges());
        viewHolder.overheads.setText(Invoicelist.get(i).getoverheads());
        viewHolder.exchangeshortexcess.setText(Invoicelist.get(i).getexchangeshortexcess());
        viewHolder.salesexpenses.setText(Invoicelist.get(i).getsalesexpenses());
        viewHolder.pricedropvalue.setText(Invoicelist.get(i).getpricedropvalue());
        viewHolder.cdsupport.setText(Invoicelist.get(i).getcdsupport());
        viewHolder.selloutsupport.setText(Invoicelist.get(i).getselloutsupport());
        viewHolder.general_creditnote.setText(Invoicelist.get(i).getgeneral_creditnote());
        viewHolder.scheme_creditnote.setText(Invoicelist.get(i).getscheme_creditnote());
        viewHolder.purchasediscount.setText(Invoicelist.get(i).getpurchasediscount());
        viewHolder.netsalesamount.setText(Invoicelist.get(i).getnetsalesamount());
        viewHolder.net_nlc_including_pd_so.setText(Invoicelist.get(i).getnet_nlc_including_pd_so());
        viewHolder.profitloss.setText(Invoicelist.get(i).getprofitloss());
    }

    @Override
    public int getItemCount() {
        return Invoicelist.size();
    }

}

