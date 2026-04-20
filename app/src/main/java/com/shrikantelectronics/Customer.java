package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Customer {

    private String custcd;
    private String name;
    private String address;
    private String mobile;
    private String emailid;
    private String outstanding;
    private String invoiceno;
    private String vinvoicedt;
    private String netinvoiceamt;


    public Customer(String name, String address, String mobile, String emailid, String outstanding, String invoiceno, String vinvoicedt, String netinvoiceamt, String custcd ) {

        this.custcd = custcd;
        this.name = name;
        this.address = address;
        this.mobile= mobile;
        this.emailid = emailid;
        this.outstanding = outstanding;
        this.invoiceno = invoiceno;
        this.vinvoicedt= vinvoicedt;
        this.netinvoiceamt = netinvoiceamt;
    }

    public String getcustcd() {
        return this.custcd;
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
