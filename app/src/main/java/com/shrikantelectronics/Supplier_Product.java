package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Supplier_Product {

    private String sysvendorno;
    private String invoiceno;
    private String invoicedt;
    private String brandname;
    private String modelname;
    private String parentcategoryname;
    private String categoryname;
    private String serialno;
    private String quantity;
    private String netamt;
    private String manufacturer_warranty_tenure;
    private String main_manufacturer_warranty_tenure;
    private String warranty_start_date;
    private String warranty_end_date;

    public Supplier_Product(String sysvendorno, String invoiceno, String invoicedt, String brandname, String modelname, String parentcategoryname, String categoryname, String serialno, String quantity, String netamt, String manufacturer_warranty_tenure, String main_manufacturer_warranty_tenure, String warranty_start_date, String warranty_end_date)
     {

        this.sysvendorno = sysvendorno ;
        this.invoiceno = invoiceno;
        this.invoicedt = invoicedt;
        this.brandname = brandname;
        this.modelname = modelname;
        this.parentcategoryname = parentcategoryname;
        this.categoryname = categoryname;
        this.serialno = serialno;
        this.quantity = quantity;
        this.netamt = netamt;
        this.manufacturer_warranty_tenure = manufacturer_warranty_tenure;
        this.main_manufacturer_warranty_tenure = main_manufacturer_warranty_tenure;
        this.warranty_start_date = warranty_start_date;
        this.warranty_end_date = warranty_end_date;
    }

    public String getsysvendorno() {
        return this.sysvendorno;
    }

    public String getinvoiceno() {
        return this.invoiceno;
    }

    public String getinvoicedt() {
        return this.invoicedt;
    }

    public String getbrandname() {
        return this.brandname;
    }

    public String getmodelname() {
        return this.modelname;
    }

    public String getparentcategoryname() {
        return this.parentcategoryname;
    }

    public String getcategoryname() {
        return this.categoryname;
    }

    public String getserialno() {
        return this.serialno;
    }

    public String getquantity() {
        return this.quantity;
    }

    public String getnetamt() {
        return this.netamt;
    }

    public String getmanufacturer_warranty_tenure() {
        return this.manufacturer_warranty_tenure;
    }

    public String getmain_manufacturer_warranty_tenure() {
        return this.main_manufacturer_warranty_tenure;
    }

    public String getwarranty_start_date() {
        return this.warranty_start_date;
    }

    public String getwarranty_end_date() {
        return this.warranty_end_date;
    }
}
