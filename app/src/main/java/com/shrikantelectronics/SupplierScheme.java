package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class SupplierScheme {

    private String sysschemesno;
    private String vendorname;
    private String brandname;
    private String vsupschfromdate;
    private String vsupschtodate;
    private String schemacode;
    private String schematype;
    private String schemval;
    private String passforslc;
    private String statusdesc;
    private String product_added;
    private String targetvalue;
    private String quantity_purchased;
    private String value_purchased;
    private String quantity_sold;
    private String value_sold;
    private String valuationmethod;
    private String pending_target_value;
    private String pending_target_percent;


    public SupplierScheme(String sysschemesno,
                          String vendorname,
                          String brandname,
                          String vsupschfromdate,
                          String vsupschtodate,
                          String schemacode,
                          String schematype,
                          String schemval,
                          String passforslc,
                          String statusdesc,
                          String product_added,
                          String targetvalue,
                          String quantity_purchased,
                          String value_purchased,
                          String quantity_sold,
                          String value_sold,
                          String valuationmethod,
                          String pending_target_value,
    String pending_target_percent) {

        this.sysschemesno = sysschemesno;
        this.vendorname = vendorname;
        this.brandname = brandname;
        this.vsupschfromdate = vsupschfromdate;
        this.vsupschtodate = vsupschtodate;
        this.schemacode = schemacode;
        this.schematype = schematype;
        this.schemval = schemval;
        this.passforslc = passforslc;
        this.statusdesc = statusdesc;
        this.product_added = product_added;
        this.targetvalue = targetvalue;
        this.quantity_purchased = quantity_purchased;
        this.value_purchased = value_purchased;
        this.quantity_sold = quantity_sold;
        this.value_sold = value_sold;
        this.valuationmethod = valuationmethod;

        this.pending_target_value = pending_target_value;
        this.pending_target_percent = pending_target_percent;


    }

    public String getsysschemesno() {
        return this.sysschemesno;
    }

    public String getvendorname() {
        return this.vendorname;
    }

    public String getbrandname() {
        return this.brandname;
    }

    public String getvsupschfromdate() {
        return this.vsupschfromdate;
    }

    public String getvsupschtodate() {
        return this.vsupschtodate;
    }

    public String getschemacode() {
        return this.schemacode;
    }

    public String getschematype() {
        return this.schematype;
    }

    public String getschemval() {
        return this.schemval;
    }

    public String getpassforslc() {
        return this.passforslc;
    }

    public String getstatusdesc() {
        return this.statusdesc;
    }

    public String getproduct_added() {
        return this.product_added;
    }

    public String gettargetvalue() {
        return this.targetvalue;
    }

    public String getquantity_purchased() {
        return this.quantity_purchased;
    }

    public String getvalue_purchased() {
        return this.value_purchased;
    }

    public String getquantity_sold() {
        return this.quantity_sold;
    }

    public String getvalue_sold() {
        return this.value_sold;
    }

    public String getvaluationmethod() {
        return this.valuationmethod;
    }

    public String getpending_target_value() {
        return this.pending_target_value;
    }

    public String getpending_target_percent() {
        return this.pending_target_percent;
    }

}
