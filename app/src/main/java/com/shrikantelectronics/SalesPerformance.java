package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class SalesPerformance {


    private String s_sysemployeeno;
    private String s_fullname;
    private String s_sale_month_1;
    private String s_sale_monthname_1;
    private String s_sale_month_2;
    private String s_sale_monthname_2;
    private String s_sale_month_3;
    private String s_sale_monthname_3;
    private String s_rank;
    private String s_imageurl;

    public SalesPerformance(String sysemployeeno,
                            String fullname,
                            String sale_month_1,
                            String sale_monthname_1,
                            String sale_month_2,
                            String sale_monthname_2,
                            String sale_month_3,
                            String sale_monthname_3,
                            String rank,
                            String imageurl)
    {

        this.s_sysemployeeno = sysemployeeno;
        this.s_fullname = fullname;
        this.s_sale_month_1 = sale_month_1;
        this.s_sale_monthname_1 = sale_monthname_1;
        this.s_sale_month_2 = sale_month_2;
        this.s_sale_monthname_2 = sale_monthname_2;
        this.s_sale_month_3 = sale_month_3;
        this.s_sale_monthname_3 = sale_monthname_3;
        this.s_rank = rank;
        this.s_imageurl = imageurl;
    }

    public String getsysemployeeno() {
        return this.s_sysemployeeno;
    }

    public String getfullname() {
        return this.s_fullname;
    }

    public String getsale_month_1() {
        return this.s_sale_month_1;
    }

    public String getsale_monthname_1() {
        return this.s_sale_monthname_1;
    }

    public String getsale_month_2() {
        return this.s_sale_month_2;
    }

    public String getsale_monthname_2() {
        return this.s_sale_monthname_2;
    }

    public String getsale_month_3() {
        return this.s_sale_month_3;
    }

    public String getsale_monthname_3() {
        return this.s_sale_monthname_3;
    }

    public String getrank() {
        return this.s_rank;
    }

    public String getimageurl() {
        return this.s_imageurl;
    }
}
