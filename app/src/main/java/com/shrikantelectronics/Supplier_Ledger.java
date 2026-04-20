package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Supplier_Ledger {

    private String sysvendorno;
    private String sysaccvoucherno;
    private String voucherdt;
    private String trndesc;
    private String debit;
    private String credit;
    private String refno;
    private String drcr;

    public Supplier_Ledger(String sysvendorno, String sysaccvoucherno, String voucherdt, String trndesc, String debit, String credit, String refno, String drcr)
     {
        this.sysvendorno = sysvendorno ;
        this.sysaccvoucherno = sysaccvoucherno;
        this.voucherdt = voucherdt;
        this.trndesc = trndesc;
        this.debit = debit;
        this.credit = credit;
        this.refno = refno;
        this.drcr = drcr;
    }

    public String getsysvendorno() {
        return this.sysvendorno;    }

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
