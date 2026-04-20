package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class CrmActivityStatus {

    private String companycd;
    private String stocklocation;
    private String op_pending;
    private String op_inprocess;
    private String added_during_month;
    private String cl_currentyear;
    private String cl_currentmonth;
    private String total_in_open_in_hand;

    public CrmActivityStatus(String companycd,
                             String stocklocation,
                             String op_pending,
                             String op_inprocess,
                             String added_during_month,
                             String cl_currentyear,
                             String cl_currentmonth,
                             String total_in_open_in_hand) {

        this.companycd = companycd;
        this.stocklocation = stocklocation;
        this.op_pending = op_pending;
        this.op_inprocess = op_inprocess;
        this.added_during_month = added_during_month;
        this.cl_currentyear = cl_currentyear;
        this.cl_currentmonth = cl_currentmonth;
        this.total_in_open_in_hand = total_in_open_in_hand;
    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getstocklocation() {
        return this.stocklocation;
    }

    public String getop_pending() {
        return this.op_pending;
    }

    public String gettotal_in_open_in_hand() {
        return this.total_in_open_in_hand;
    }

    public String getop_inprocess() {
        return this.op_inprocess;
    }

    public String getadded_during_month() {
        return this.added_during_month;
    }

    public String getcl_currentyear() {
        return this.cl_currentyear;
    }

    public String getcl_currentmonth() {
        return this.cl_currentmonth;
    }

}
