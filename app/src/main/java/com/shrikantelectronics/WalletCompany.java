package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class WalletCompany {

    private String digitalcompanycd;
    private String digitalcompanyname;


    public WalletCompany(String digitalcompanycd, String digitalcompanyname) {
        this.digitalcompanycd = digitalcompanycd;
        this.digitalcompanyname = digitalcompanyname;
    }

    public String getdigitalcompanycd() {
        return this.digitalcompanycd;
    }

    public String getdigitalcompanyname() {
        return this.digitalcompanyname;
    }

}
