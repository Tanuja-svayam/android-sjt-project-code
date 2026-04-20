package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Model_Location {

    private String companycd;
    private String sysmodelno;
    private String companyname;
    private String lifetime_sales;
    private String recent_sales;
    private String bd_stock;
    private String delivery_pending;
    private String balancestock;

    public Model_Location(String companycd, String sysmodelno, String companyname, String lifetime_sales, String recent_sales, String bd_stock, String delivery_pending, String balancestock)
     {
        this.companycd = companycd ;
        this.sysmodelno = sysmodelno;
        this.companyname = companyname;
        this.lifetime_sales = lifetime_sales;
        this.recent_sales = recent_sales;
        this.bd_stock = bd_stock;
        this.delivery_pending = delivery_pending;
        this.balancestock = balancestock;
    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getsysmodelno() {
        return this.sysmodelno;
    }

    public String getcompanyname() {
        return this.companyname;
    }

    public String getlifetime_sales() {
        return this.lifetime_sales;
    }

    public String getrecent_sales() {
        return this.recent_sales;
    }

    public String getbd_stock() {
        return this.bd_stock;
    }

    public String getdelivery_pending() {
        return this.delivery_pending;
    }

    public String getbalancestock() {
        return this.balancestock;
    }
}
