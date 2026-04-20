package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Todays_Outstanding {

    private String companycd;
    private String stocklocation;

    private String cash_lastdate;
    private String cash;

    private String cheque_lastdate;
    private String cheque;

    private String creditcard_lastdate;
    private String creditcard;

    private String clearcheque_lastdate;
    private String clearcheque;

    private String finnance_lastdate;
    private String finnance;

    private String total;

    public Todays_Outstanding(String companycd, String stocklocation,

                              String cash_lastdate,
                              String cash,

                              String cheque_lastdate,
                              String cheque,

                              String creditcard_lastdate,
                              String creditcard,

                              String clearcheque_lastdate,
                              String clearcheque,

                              String finnance_lastdate,
                              String finnance,

                              String total) {

        this.companycd = companycd;
        this.stocklocation = stocklocation;

        this.cash_lastdate = cash_lastdate;
        this.cash = cash;

        this.cheque_lastdate = cheque_lastdate;
        this.cheque = cheque;

        this.creditcard_lastdate = creditcard_lastdate;
        this.creditcard = creditcard;

        this.clearcheque_lastdate = clearcheque_lastdate;
        this.clearcheque = clearcheque;

        this.finnance_lastdate = finnance_lastdate;
        this.finnance = finnance;

        this.total = total;
    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getstocklocation() {
        return this.stocklocation;
    }

    public String getcash_lastdate() {
        return this.cash_lastdate;
    }
    public String getcash() {
        return this.cash;
    }

    public String getcheque_lastdate() {
        return this.cheque_lastdate;
    }
    public String getcheque() {
        return this.cheque;
    }

    public String getcreditcard_lastdate() {
        return this.creditcard_lastdate;
    }
    public String getcreditcard() {
        return this.creditcard;
    }

    public String getclearcheque_lastdate() {
        return this.clearcheque_lastdate;
    }
    public String getclearcheque() {
        return this.clearcheque;
    }

    public String getfinnance_lastdate() {
        return this.finnance_lastdate;
    }
    public String getfinnance() {
        return this.finnance;
    }

    public String gettotal() {
        return this.total;
    }
}
