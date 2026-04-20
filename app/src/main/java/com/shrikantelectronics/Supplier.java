package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Supplier {

    private String sysvendorno;
    private String name;
    private String address;
    private String mobile;
    private String emailid;
    private String outstanding;
    private String invoiceno;
    private String vinvoicedt;
    private String netinvoiceamt;

    public Supplier(String name, String address, String mobile, String emailid, String outstanding, String invoiceno, String vinvoicedt, String netinvoiceamt, String sysvendorno ) {

        this.sysvendorno = sysvendorno;
        this.name = name;
        this.address = address;
        this.mobile= mobile;
        this.emailid = emailid;
        this.outstanding = outstanding;
        this.invoiceno = invoiceno;
        this.vinvoicedt= vinvoicedt;
        this.netinvoiceamt = netinvoiceamt;
    }

    public String getsysvendorno() {
        return this.sysvendorno;
    }

    public String getname() {
        return this.name;
    }

    public String getaddress() {
        return this.address;
    }

    public String getmobile() {
        return this.mobile;
    }

    public String getemailid() {
        return this.emailid;
    }

    public String getoutstanding() {
        return this.outstanding;
    }

    public String getinvoiceno() {
        return this.invoiceno;
    }

    public String getvinvoicedt() {
        return this.vinvoicedt;
    }

    public String getnetinvoiceamt() {
        return this.netinvoiceamt;
    }
}
