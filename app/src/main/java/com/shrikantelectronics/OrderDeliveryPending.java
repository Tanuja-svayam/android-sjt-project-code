package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class OrderDeliveryPending {

    private String sysinvno;
    private String custname;
    private String netinvoiceamt;
    private String vinvoicedt;
    private String invoiceno;

    private String dbd_paid_by_customer;
    private String dbd_amount;
    private String approved_by_manager;
    private String gross_slc_amount;
    private String remarks;
    private String searchfield;

    public OrderDeliveryPending(String sysinvno, String custname, String netinvoiceamt, String vinvoicedt, String invoiceno, String searchfield,
                                String dbd_paid_by_customer, String dbd_amount, String approved_by_manager, String gross_slc_amount, String remarks) {
        this.sysinvno = sysinvno;
        this.custname = custname;
        this.netinvoiceamt = netinvoiceamt;
        this.vinvoicedt = vinvoicedt;
        this.invoiceno = invoiceno;
        this.dbd_paid_by_customer = dbd_paid_by_customer;
        this.dbd_amount = dbd_amount;
        this.approved_by_manager = approved_by_manager;
        this.gross_slc_amount = gross_slc_amount;
        this.remarks = remarks;
        this.searchfield = searchfield;
    }

    public String getsysinvno() {
        return this.sysinvno;
    }

    public String getcustname() {
        return this.custname;
    }

    public String getsearchfield() {
        return this.searchfield;
    }

    public String getnetinvoiceamt() {
        return this.netinvoiceamt;
    }

    public String getvinvoicedt() {
        return this.vinvoicedt;
    }

    public String getinvoiceno() {
        return this.invoiceno;
    }

    public String getdbd_paid_by_customer() {
        return this.dbd_paid_by_customer;
    }

    public String getdbd_amount() {
        return this.dbd_amount;
    }

    public String getapproved_by_manager() {
        return this.approved_by_manager;
    }

    public String getgross_slc_amount() {
        return this.gross_slc_amount;
    }

    public String getremarks() {
        return this.remarks;
    }


}