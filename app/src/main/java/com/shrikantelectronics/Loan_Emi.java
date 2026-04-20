package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
**/

public class Loan_Emi {

    private String sysemino;
    private String sysloanno;
    private String emino;
    private String vemidt;
    private String vemipaiddt;
    private String vreceiptdt;
    private String emiamt;
    private String adjamt;
    private String balamt;
    private String overduedays;


        public Loan_Emi(String sysemino, String sysloanno, String emino, String vemidt, String vemipaiddt, String vreceiptdt, String emiamt, String adjamt,String balamt, String overduedays)
     {
         this.sysemino = sysemino;
         this.sysloanno = sysloanno;
         this.emino = emino;
         this.vemidt = vemidt;
         this.vemipaiddt = vemipaiddt;
         this.vreceiptdt = vreceiptdt;
         this.emiamt = emiamt;
         this.adjamt = adjamt;
         this.balamt = balamt;
         this.overduedays = overduedays;

    }

    public String getsysemino() {return this.sysemino;}
    public String getsysloanno() {return this.sysloanno;}
    public String getemino() {return this.emino;}
    public String getvemidt() {return this.vemidt;}
    public String getvemipaiddt() {return this.vemipaiddt;}
    public String getvreceiptdt() {return this.vreceiptdt;}
    public String getemiamt() {return this.emiamt;}
    public String getadjamt() {return this.adjamt;}
    public String getbalamt() {return this.balamt;}
    public String getoverduedays() {return this.overduedays;}

}
