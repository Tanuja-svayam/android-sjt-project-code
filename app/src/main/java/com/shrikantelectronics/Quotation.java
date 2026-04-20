package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Quotation {

    private String sysinvorderno;
    private String custname;
    private String netinvoiceamt;
    private String vinvorderdt;
    private String invorderno;
    private String searchfield;

    public Quotation(String sysinvorderno, String custname, String netinvoiceamt, String vinvorderdt, String invorderno, String searchfield) {
        this.sysinvorderno = sysinvorderno;
        this.custname = custname;
        this.netinvoiceamt = netinvoiceamt;
        this.vinvorderdt = vinvorderdt;
        this.invorderno = invorderno;
        this.searchfield = searchfield;
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

    public String getvinvorderdt() {
        return this.vinvorderdt;
    }

    public String getinvorderno() {
        return this.invorderno;
    }
}