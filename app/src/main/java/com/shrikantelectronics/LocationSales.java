package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class LocationSales {

    private String companycd;
    private String stocklocation;
    private String purchase_value;
    private String totalqty;
    private String days_30;
    private String days_31_60;
    private String days_61_90;
    private String days_91_180;
    private String days_180_above;



    public LocationSales(String companycd, String stocklocation,
                         String purchase_value, String totalqty,
                         String days_30,
                         String days_31_60,
                         String days_61_90,
                         String days_91_180,
                         String days_180_above                         ) {
        this.companycd = companycd;
        this.stocklocation = stocklocation;
        this.purchase_value = purchase_value;
        this.totalqty = totalqty;
        this.days_30 = days_30;
        this.days_31_60 = days_31_60;
        this.days_61_90 = days_61_90;
        this.days_91_180 = days_91_180;
        this.days_180_above = days_180_above;

    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getstocklocation() {
        return this.stocklocation;
    }

    public String getpurchase_value() {
        return this.purchase_value;
    }

    public String gettotalqty() {
        return this.totalqty;
    }

    public String getdays_30() {
        return this.days_30;
    }

    public String getdays_31_60() {
        return this.days_31_60;
    }

    public String getdays_61_90() {
        return this.days_61_90;
    }

    public String getdays_91_180() {
        return this.days_91_180;
    }

    public String getdays_180_above() {
        return this.days_180_above;
    }

}
