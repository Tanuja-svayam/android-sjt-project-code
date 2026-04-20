package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Serial_Stock_Verification {

    private String sysproductno;
    private String serialno;
    private String barcodeno;
    private String modelname;
    private String brandname;
    private String stocklocation;
    private String sysmodelno;
    private String sysbrandno;
    private String searchserial;

    public Serial_Stock_Verification(String sysproductno, String serialno, String barcodeno, String modelname, String brandname, String stocklocation, String sysmodelno, String sysbrandno, String searchserial) {
        this.sysproductno = sysproductno;
        this.serialno = serialno;
        this.barcodeno = barcodeno;
        this.modelname = modelname;
        this.brandname = brandname;
        this.stocklocation = stocklocation;
        this.sysmodelno = sysmodelno;
        this.sysbrandno = sysbrandno;
        this.searchserial= searchserial;

    }

    public String getsysproductno() {
        return this.sysproductno;
    }

    public String getserialno() {
        return this.serialno;
    }

    public String getsysbrandno() {
        return this.sysbrandno;
    }


    public String getbarcodeno() {
        return this.barcodeno;
    }

    public String getmodelname() {
        return this.modelname;
    }

    public String getbrandname() {
        return this.brandname;
    }

    public String getstocklocation() {
        return this.stocklocation;
    }

    public String getsysmodelno() {
        return this.sysmodelno;
    }

    public String getsearchserial() {
        return this.searchserial;
    }
}
