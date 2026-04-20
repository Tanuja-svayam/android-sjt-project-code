package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Transporter {

    private String transportercd;
    private String transportername;


    public Transporter(String transportercd, String transportername) {
        this.transportercd = transportercd;
        this.transportername = transportername;
    }

    public String gettransportercd() {
        return this.transportercd;
    }

    public String gettransportername() {
        return this.transportername;
    }

}
