package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class PurchaseInvoice {

    private String sysinvno;
    private String sysvendorno;
    private String suppliername;
    private String netinvoiceamt;
    private String vinvoicedt;
    private String invoiceno;
    private String companycd;
    private String companyname;
    private String searchfield;

    public PurchaseInvoice(String sysinvno , String  sysvendorno, String suppliername, String netinvoiceamt, String vinvoicedt, String invoiceno, String companycd, String companyname, String searchfield) {
        this.sysinvno = sysinvno;
        this.sysvendorno = sysvendorno;
        this.suppliername = suppliername;
        this.netinvoiceamt = netinvoiceamt;
        this.vinvoicedt = vinvoicedt;
        this.invoiceno = invoiceno;
        this.companycd = companycd;
        this.companyname = companyname;
        this.searchfield = searchfield;
    }

    public String getsysinvno() {
        return this.sysinvno;
    }


    public String getsysvendorno() {
        return this.sysvendorno;
    }


    public String getsuppliername() {
        return this.suppliername;
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


    public String getcompanycd() {
        return this.companycd;
    }

    public String getcompanyname() {
        return this.companyname;
    }
}