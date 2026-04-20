package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Todays_Sales {

    private String companycd;
    private String stocklocation;
    private String sales_target;
    private String sales_mtd_target;
    private String order_sales_date;
    private String order_sales_ach;
    private String op_sales_date;
    private String op_sales_ach;
    private String today_sales_ach;
    private String today_sales_quantity;
    private String sales_ach;
    private String sales_total_gap;
    private String sales_mtd_gap;
    private String sales_kitty;
    private String sales_ach_percent;
    private String sales_month_name;
    private String sales_month_target;
    private String month_sales_ach;
    private String sales_month_gap;

    public Todays_Sales(String companycd,
                        String stocklocation,
                        String sales_target,
                        String sales_mtd_target,
                        String order_sales_date,
                        String order_sales_ach,
                        String op_sales_date,
                        String op_sales_ach,
                        String today_sales_ach,
                        String today_sales_quantity,
                        String sales_ach,
                        String sales_total_gap,
                        String sales_mtd_gap,
                        String sales_kitty,
                        String sales_ach_percent,
                        String sales_month_name,
                        String sales_month_target,
                        String month_sales_ach,
                        String sales_month_gap

    ) {

        this.companycd = companycd;
        this.stocklocation = stocklocation;
        this.sales_target = sales_target;
        this.sales_mtd_target = sales_mtd_target;
        this.order_sales_date = order_sales_date;
        this.order_sales_ach = order_sales_ach;
        this.op_sales_date = op_sales_date;
        this.op_sales_ach = op_sales_ach;
        this.today_sales_ach = today_sales_ach;
        this.today_sales_quantity= today_sales_quantity;

        this.sales_ach = sales_ach;
        this.sales_total_gap = sales_total_gap;
        this.sales_mtd_gap = sales_mtd_gap;
        this.sales_kitty = sales_kitty;
        this.sales_ach_percent = sales_ach_percent;

        this.sales_month_name= sales_month_name;
                this.sales_month_target= sales_month_target;
                this.month_sales_ach= month_sales_ach;
                this.sales_month_gap= sales_month_gap;

    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getstocklocation() {
        return this.stocklocation;
    }

    public String getsales_target() {
        return this.sales_target;
    }

    public String getsales_mtd_target() {
        return this.sales_mtd_target;
    }

    public String getorder_sales_date() {
        return this.order_sales_date;
    }

    public String getorder_sales_ach() {
        return this.order_sales_ach;
    }

    public String getop_sales_date() {
        return this.op_sales_date;
    }

    public String getop_sales_ach() {
        return this.op_sales_ach;
    }

    public String gettoday_sales_ach() {
        return this.today_sales_ach;
    }

    public String gettoday_sales_quantity() {
        return this.today_sales_quantity;
    }

    public String getsales_ach() {
        return this.sales_ach;
    }

    public String getsales_total_gap() {
        return this.sales_total_gap;
    }

    public String getsales_mtd_gap() {
        return this.sales_mtd_gap;
    }

    public String getsales_kitty() {
        return this.sales_kitty;
    }

    public String getsales_ach_percent() {
        return this.sales_ach_percent;
    }

    public String getsales_month_name() {
        return this.sales_month_name;
    }
    public String getsales_month_target() {
        return this.sales_month_target;
    }
    public String getmonth_sales_ach() {
        return this.month_sales_ach;
    }
    public String getsales_month_gap() {
        return this.sales_month_gap;
    }

}
