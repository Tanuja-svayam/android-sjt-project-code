package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class PaymentMode {

    private String bankcd;
    private String paymodename;


    public PaymentMode(String bankcd, String paymodename) {
        this.bankcd = bankcd;
        this.paymodename = paymodename;
    }

    public String getbankcd() {
        return this.bankcd;
    }

    public String getpaymodename() {
        return this.paymodename;
    }

}
