package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Order {

    private String sysinvno;
    private String custname;
    private String netinvoiceamt;
    private String vinvoicedt;
    private String invoiceno;
    private String searchfield;

    public Order(String sysinvno, String custname, String netinvoiceamt, String vinvoicedt, String invoiceno, String searchfield) {
        this.sysinvno = sysinvno;
        this.custname = custname;
        this.netinvoiceamt = netinvoiceamt;
        this.vinvoicedt = vinvoicedt;
        this.invoiceno = invoiceno;
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
}