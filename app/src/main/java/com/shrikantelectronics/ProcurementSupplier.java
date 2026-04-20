package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class ProcurementSupplier {

    private String sysvendorno;
    private String description;


    public ProcurementSupplier(String sysvendorno, String description) {
        this.sysvendorno = sysvendorno;
        this.description = description;
    }

    public String getsysvendorno() {
        return this.sysvendorno;
    }

    public String getdescription() {
        return this.description;
    }

}
