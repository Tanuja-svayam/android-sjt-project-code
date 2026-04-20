package com.shrikantelectronics;

import android.widget.TextView;

/**
 * Created by Administrator on 10/12/2016.
 */

public class LocationSalesCustomer {

    private String companycd;
    private String custcd;
    private String stocklocation;
    private String custname;
    private String topcategoryname;
    private String brandname;
    private String modelname;
    private String quantity;
    private String unitprice_include_tax;
    private String address;
    private String mobile;
    private String emailid;
    private String outstanding;

    public LocationSalesCustomer(String companycd,
                                 String custcd,
                                 String stocklocation,
                                 String custname,
                                 String topcategoryname,
                                 String brandname,
                                 String modelname,
                                 String quantity,
                                 String unitprice_include_tax,
                                 String address,
                                 String mobile,
                                 String emailid,
                                 String outstanding
    ) {

        this.custcd = custcd;
        this.companycd = companycd;
        this.stocklocation = stocklocation;
        this.custname = custname;
        this.topcategoryname = topcategoryname;
        this.brandname = brandname;
        this.modelname = modelname;
        this.quantity = quantity;
        this.unitprice_include_tax = unitprice_include_tax;
        this.address = address;
        this.mobile = mobile;
        this.emailid = emailid;
        this.outstanding = outstanding;

    }

    public String getcustcd() {
        return this.custcd;
    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getstocklocation() {
        return this.stocklocation;
    }

    public String getcustname() {
        return this.custname;
    }

    public String gettopcategoryname() {
        return this.topcategoryname;
    }

    public String getbrandname() {
        return this.brandname;
    }

    public String getmodelname() {
        return this.modelname;
    }

    public String getquantity() {
        return this.quantity;
    }

    public String getunitprice_include_tax() {
        return this.unitprice_include_tax;
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

}
