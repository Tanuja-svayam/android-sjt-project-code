package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Invoice {

    private String sysinvno;
    private String custname;
    private String netinvoiceamt;
    private String vinvoicedt;
    private String invoiceno;
    private String searchfield;
    private String sysinvorderno;


    public Invoice(String sysinvno, String custname, String netinvoiceamt, String vinvoicedt, String invoiceno, String searchfield, String sysinvorderno) {
        this.sysinvno = sysinvno;
        this.custname = custname;
        this.netinvoiceamt = netinvoiceamt;
        this.vinvoicedt = vinvoicedt;
        this.invoiceno = invoiceno;
        this.searchfield = searchfield;
        this.sysinvorderno = sysinvorderno;
    }

    public String getsysinvno() {
        return this.sysinvno;
    }

    public String getsysinvorderno() {
        return this.sysinvorderno;
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