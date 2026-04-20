package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Maincash {

    private String bankcd;
    private String name;
    private String outstanding;
    private String legdertype;

    public Maincash(String name, String outstanding, String bankcd, String legdertype ) {
        this.bankcd = bankcd;
        this.name = name;
        this.outstanding = outstanding;
        this.legdertype = legdertype;
    }

    public String getbankcd() {
        return this.bankcd;
    }

    public String getname() {
        return this.name;
    }

    public String getoutstanding() {
        return this.outstanding;
    }

    public String getlegdertype() {
        return this.legdertype;
    }

}
