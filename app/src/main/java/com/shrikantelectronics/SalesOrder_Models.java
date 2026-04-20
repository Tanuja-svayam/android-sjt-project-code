package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class SalesOrder_Models {

    private String sysinvorderno;
    private String sysorderdtlno;
    private String custcd;
    private String custname;
    private String refbillno;
    private String modelcodepk;
    private String modelcode;
    private String mrp;
    private String dp;
    private String stock;
    private String modelimageurl;
    private String searchfield;
    private String vinvoicedt;
    private String netinvoiceamt;

    public SalesOrder_Models(String sysinvorderno, String sysorderdtlno, String refbillno, String custcd, String custname, String vinvoicedt, String netinvoiceamt, String modelcodepk, String modelcode, String mrp, String dp, String stock, String modelimageurl, String searchfield) {
        this.sysinvorderno = sysinvorderno;
        this.sysorderdtlno = sysorderdtlno;
        this.custcd = custcd;
        this.custname = custname;
        this.refbillno = refbillno;

        this.vinvoicedt = vinvoicedt;
        this.netinvoiceamt = netinvoiceamt;

        this.modelcodepk = modelcodepk;
        this.modelcode = modelcode;
        this.mrp = mrp;
        this.dp = dp;
        this.stock = stock;
        this.modelimageurl = modelimageurl;
        this.searchfield = searchfield;
    }

    public String getsysinvorderno() {
        return this.sysinvorderno;
    }

    public String getsysorderdtlno() {
        return this.sysorderdtlno;
    }

    public String getcustcd() {
        return this.custcd;
    }

    public String getcustname() {
        return this.custname;
    }

    public String getvinvoicedt() {
        return this.vinvoicedt;
    }
    public String getnetinvoiceamt() {
        return this.netinvoiceamt;
    }

    public String getmodelcodepk() {
        return this.modelcodepk;
    }

    public String getmodelcode() {
        return this.modelcode;
    }

    public String getrefbillno() {
        return this.refbillno;
    }


    public String getsearchfield() {
        return this.searchfield;
    }


    public String getmrp() {
        return this.mrp;
    }

    public String getdp() {
        return this.dp;
    }

    public String getstock() {
        return this.stock;
    }

    public String getmodelimageurl() {
        return this.modelimageurl;
    }
}
