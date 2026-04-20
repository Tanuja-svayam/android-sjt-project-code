package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class FinanceCompany {

    private String fincompanycd;
    private String financename;


    public FinanceCompany(String fincompanycd, String financename) {
        this.fincompanycd = fincompanycd;
        this.financename = financename;
    }

    public String getfincompanycd() {
        return this.fincompanycd;
    }

    public String getfinancename() {
        return this.financename;
    }

}
