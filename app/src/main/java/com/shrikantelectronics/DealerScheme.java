package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class DealerScheme {

    private String sysschemesno;
    private String custname;
    private String brandname;
    private String vsupschfromdate;
    private String vsupschtodate;
    private String schemacode;
    private String valuationmethod;
    private String schemval;
    private String targetvalue;
    private String soldvalue;
    private String returnvalue;
    private String achievedvalue;
    private String diffvalue;
    private String value_net;
    private String schemval_achieved;


    public DealerScheme(String sysschemesno,
                        String custname,
                        String brandname,
                        String vsupschfromdate,
                        String vsupschtodate,
                        String schemacode,
                        String valuationmethod,
                        String schemval,
                        String targetvalue,
                        String soldvalue,
                        String returnvalue,
                        String achievedvalue,
                        String diffvalue,
                        String value_net,
                        String schemval_achieved) {

        this.sysschemesno = sysschemesno;
        this.custname = custname;
        this.brandname = brandname;
        this.vsupschfromdate = vsupschfromdate;
        this.vsupschtodate = vsupschtodate;
        this.schemacode = schemacode;
        this.schemval = schemval;
        this.valuationmethod = valuationmethod;
        this.targetvalue = targetvalue;
        this.soldvalue = soldvalue;
        this.returnvalue = returnvalue;
        this.achievedvalue = achievedvalue;
        this.diffvalue = diffvalue;
        this.value_net = value_net;
        this.schemval_achieved = schemval_achieved;
    }

    public String getsysschemesno() {
        return this.sysschemesno;
    }

    public String getcustname() {
        return this.custname;
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

    public String getschemval() {
        return this.schemval;
    }

    public String getsoldvalue() {
        return this.soldvalue;
    }

    public String getreturnvalue() {
        return this.returnvalue;
    }

    public String getachievedvalue() {
        return this.achievedvalue;
    }

    public String gettargetvalue() {
        return this.targetvalue;
    }

    public String getdiffvalue() {
        return this.diffvalue;
    }


    public String getvalue_net() {
        return this.value_net;
    }


    public String getschemval_achieved() {
        return this.schemval_achieved;
    }

     public String getvaluationmethod() {
        return this.valuationmethod;
    }


}
