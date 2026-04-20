package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Brand {

    private String sysbrandno;
    private String description;


    public Brand(String sysbrandno, String description) {
        this.sysbrandno = sysbrandno;
        this.description = description;
    }

    public String getsysbrandno() {
        return this.sysbrandno;
    }

    public String getdescription() {
        return this.description;
    }

}
