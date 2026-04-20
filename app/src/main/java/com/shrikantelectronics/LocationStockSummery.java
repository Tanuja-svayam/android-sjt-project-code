package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class LocationStockSummery {

    private String  from_date;
    private String  to_date;
    private String  companycd;
    private String  stocklocation;
    private String  op_balquantity;
    private String  op_valueprice;
    private String  pu_quantity;
    private String  pu_valueprice;
    private String  tr_in_quantity;
    private String  tr_in_valueprice;
    private String  tr_out_quantity;
    private String  tr_out_valueprice;
    private String  sa_in_quantity;
    private String  sa_in_valueprice;
    private String  sa_out_quantity;
    private String  sa_out_valueprice;
    private String  sa_return_in_quantity;
    private String  sa_return_in_valueprice;
    private String  sa_return_out_quantity;
    private String  sa_return_out_valueprice;
    private String  pu_return_in_quantity;
    private String  pu_return_in_valueprice;
    private String  pu_return_out_quantity;
    private String  pu_return_out_valueprice;
    private String  cl_balquantity;
    private String  cl_valueprice;

    public LocationStockSummery(String from_date, String to_date,
             String companycd,
             String stocklocation,
             String op_balquantity,
             String op_valueprice,
             String pu_quantity,
             String pu_valueprice,
             String tr_in_quantity,
             String tr_in_valueprice,
             String tr_out_quantity,
             String tr_out_valueprice,
             String sa_in_quantity,
             String sa_in_valueprice,
             String sa_out_quantity,
             String sa_out_valueprice,
             String sa_return_in_quantity,
             String sa_return_in_valueprice,
             String sa_return_out_quantity,
             String sa_return_out_valueprice,
             String pu_return_in_quantity,
             String pu_return_in_valueprice,
             String pu_return_out_quantity,
             String pu_return_out_valueprice,
             String cl_balquantity,
             String cl_valueprice) {



        this.from_date			  =    from_date			;
        this.to_date			  =    to_date				;
        this.companycd			  =    companycd			;
        this.stocklocation		  =    stocklocation			;
        this.op_balquantity		  =    op_balquantity			;
        this.op_valueprice		  =    op_valueprice			;
        this.pu_quantity		  =    pu_quantity			;
        this.pu_valueprice		  =    pu_valueprice			;
        this.tr_in_quantity		  =    tr_in_quantity			;
        this.tr_in_valueprice		  =    tr_in_valueprice			;
        this.tr_out_quantity		  =    tr_out_quantity			;
        this.tr_out_valueprice		  =    tr_out_valueprice		;
        this.sa_in_quantity		  =    sa_in_quantity			;
        this.sa_in_valueprice		  =    sa_in_valueprice			;
        this.sa_out_quantity		  =    sa_out_quantity			;
        this.sa_out_valueprice		  =    sa_out_valueprice		;
        this.sa_return_in_quantity	  =    sa_return_in_quantity		;
        this.sa_return_in_valueprice	  =    sa_return_in_valueprice		;
        this.sa_return_out_quantity	  =    sa_return_out_quantity		;
        this.sa_return_out_valueprice	  =    sa_return_out_valueprice		;
        this.pu_return_in_quantity	  =    pu_return_in_quantity		;
        this.pu_return_in_valueprice	  =    pu_return_in_valueprice		;
        this.pu_return_out_quantity	  =    pu_return_out_quantity		;
        this.pu_return_out_valueprice	  =    pu_return_out_valueprice		;
        this.cl_balquantity		  =    cl_balquantity			;
        this.cl_valueprice		  =    cl_valueprice			;

    }

    public String getfrom_date() {
        return this.from_date;
    }
    public String getto_date() {
        return this.to_date;
    }
    public String getcompanycd() {
        return this.companycd;
    }
    public String getstocklocation() {
        return this.stocklocation;
    }
    public String getop_balquantity() {
        return this.op_balquantity;
    }
    public String getop_valueprice() {
        return this.op_valueprice;
    }
    public String getpu_quantity() {
        return this.pu_quantity;
    }
    public String getpu_valueprice() {
        return this.pu_valueprice;
    }
    public String gettr_in_quantity() {
        return this.tr_in_quantity;
    }
    public String gettr_in_valueprice() {
        return this.tr_in_valueprice;
    }
    public String gettr_out_quantity() {
        return this.tr_out_quantity;
    }
    public String gettr_out_valueprice() {
        return this.tr_out_valueprice;
    }
    public String getsa_in_quantity() {
        return this.sa_in_quantity;
    }
    public String getsa_in_valueprice() {
        return this.sa_in_valueprice;
    }
    public String getsa_out_quantity() {
        return this.sa_out_quantity;
    }
    public String getsa_out_valueprice() {
        return this.sa_out_valueprice;
    }
    public String getsa_return_in_quantity() {
        return this.sa_return_in_quantity;
    }
    public String getsa_return_in_valueprice() {
        return this.sa_return_in_valueprice;
    }
    public String getsa_return_out_quantity() {
        return this.sa_return_out_quantity;
    }
    public String getsa_return_out_valueprice() {
        return this.sa_return_out_valueprice;
    }
    public String getpu_return_in_quantity() {
        return this.pu_return_in_quantity;
    }
    public String getpu_return_in_valueprice() {
        return this.pu_return_in_valueprice;
    }
    public String getpu_return_out_quantity() {
        return this.pu_return_out_quantity;
    }
    public String getpu_return_out_valueprice() {
        return this.pu_return_out_valueprice;
    }
    public String getcl_balquantity() {
        return this.cl_balquantity;
    }
    public String getcl_valueprice() {
        return this.cl_valueprice;
    }

}
