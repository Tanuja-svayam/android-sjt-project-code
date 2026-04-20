package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Location {

    private String companycd;
    private String companyname;


    public Location(String companycd, String companyname) {
        this.companycd = companycd;
        this.companyname = companyname;
    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getcompanyname() {
        return this.companyname;
    }

}
