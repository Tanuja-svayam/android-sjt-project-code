package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class SchemePurchase {

    private String sysschemesno;
    private String vendorname;
    private String brandname;
    private String modelname;
    private String vpurorderdt;
    private String schemacode;
    private String refdocumentno;
    private String quantity;
    private String grossamt;

    public SchemePurchase(String sysschemesno,
                          String vendorname,
                          String brandname,
                          String modelname,
                          String vpurorderdt,
                          String schemacode,
                          String refdocumentno,
                          String quantity,
                          String grossamt) {

        this.sysschemesno = sysschemesno;
        this.vendorname = vendorname;
        this.brandname = brandname;
        this.modelname = modelname;
        this.vpurorderdt = vpurorderdt;
        this.schemacode = schemacode;
        this.refdocumentno = refdocumentno;
        this.quantity = quantity;
        this.grossamt = grossamt;
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

    public String getmodelname() {
        return this.modelname;
    }

    public String getvpurorderdt() {
        return this.vpurorderdt;
    }

    public String getschemacode() {
        return this.schemacode;
    }

    public String getrefdocumentno() {
        return this.refdocumentno;
    }


    public String getquantity() {
        return this.quantity;
    }

    public String getgrossamt() {
        return this.grossamt;
    }


}
