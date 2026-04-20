package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Customer_Ledger {

    private String custcd;
    private String sysaccvoucherno;
    private String voucherdt;
    private String trndesc;
    private String debit;
    private String credit;
    private String refno;
    private String drcr;

    public Customer_Ledger(String custcd, String sysaccvoucherno, String voucherdt, String trndesc, String debit, String credit, String refno, String drcr)
     {
        this.custcd = custcd ;
        this.sysaccvoucherno = sysaccvoucherno;
        this.voucherdt = voucherdt;
        this.trndesc = trndesc;
        this.debit = debit;
        this.credit = credit;
        this.refno = refno;
        this.drcr = drcr;
    }

    public String getcustcd() {
        return this.custcd;    }

    public String getsysaccvoucherno() {
        return this.sysaccvoucherno;
    }

    public String getvoucherdt() {
        return this.voucherdt;
    }

    public String gettrndesc() {
        return this.trndesc;
    }

    public String getdebit() {
        return this.debit;
    }

    public String getcredit() {
        return this.credit;
    }

    public String getrefno() {
        return this.refno;
    }

    public String getdrcr() {
        return this.drcr;
    }

}
