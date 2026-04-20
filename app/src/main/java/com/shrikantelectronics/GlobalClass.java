package com.shrikantelectronics;

/**
 * Created by Administrator on 21/10/2017.
 */

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GlobalClass extends Application {

    private String sysuserno;
    private String sysemployeeno;
    private String companycd;
    private String reportingto;
    private String reporting_fullname;
    private String companyname;
    private String parentcompanyname;
    private String employeename;
    private String userid;
    private String groupcode;
    private String usertype;
    private String employeeimagename;
    private String AccountingStartDate;
    private String AccountingEndDate;
    private String apppackagename;
    private String cashsales_custcd;
    private String cashsales_customername;
    private String default_state;

    private String device_token;
    public static String APPTHEME = "AppTheme"; // Default

    public static String CLIENTID  ="SV250300060";
    public static String SUBSCRIPTIONID  ="60";

    public static String SVAYAM_CLIENTID  ="SVAYAMMGMT";
    public static String SVAYAM_SUBSCRIPTIONID  ="1";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        APPTHEME = prefs.getString("app_theme", APPTHEME);
    }

    public void setColorPrimary(String colorCode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("app_theme", colorCode).apply();

        APPTHEME = colorCode;
    }
    public void setAccountingStartDate(String AccountingStartDate) {
        this.AccountingStartDate = AccountingStartDate;
    }

    public String getAccountingStartDate() {
        return AccountingStartDate;
    }

    public void setAccountingEndDate(String AccountingEndDate) {
        this.AccountingEndDate = AccountingEndDate;
    }

    public String getAccountingEndDate() {
        return AccountingEndDate ;
    }

    public String getSysuserno() {
        return sysuserno;
    }

    public void setSysuserno(String sysuserno) {
        this.sysuserno = sysuserno;
    }

    public String getsysemployeeno() {
        return sysemployeeno;
    }

    public void setsysemployeeno(String sysemployeeno) {
        this.sysemployeeno = sysemployeeno;
    }

    public String getreporting_fullname() {
        return reporting_fullname;
    }
    public void setreporting_fullname(String reporting_fullname) {
        this.reporting_fullname = reporting_fullname;
    }

    public String getdefault_state() {
        return default_state;
    }


    public void setdefault_state(String default_state) {
        this.default_state = default_state;
    }



    public String getdevice_token() {
        return device_token;
    }

    public void setddevice_token(String device_token) {
        this.device_token = device_token;
    }


    public String getreportingto() {
        return reportingto;
    }
    public void setreportingto(String reportingto) {
        this.reportingto = reportingto;
    }

    public String getcashsales_custcd() {
        return cashsales_custcd;
    }

    public void setcashsales_custcd(String cashsales_custcd) {
        this.cashsales_custcd = cashsales_custcd;
    }

    public String getcashsales_customername() {
        return cashsales_customername;
    }
    public void setcashsales_customername(String cashsales_customername) {
        this.cashsales_customername = cashsales_customername;
    }

    public String getcompanycd() {
        return companycd;
    }

    public void setcompanycd(String companycd) {
        this.companycd = companycd;
    }

    public String getcompanyname() {
        return companyname;
    }

    public void setcompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getparentcompanyname() {
        return parentcompanyname;
    }

    public void setparentcompanyname(String parentcompanyname) {
        this.parentcompanyname = parentcompanyname;
    }
    public String getemployeename() {
        return employeename;
    }

    public void setemployeename(String employeename) {
        this.employeename = employeename;
    }


    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }

    public String getapppackagename() {
        return "example.prgguru.com.shree_krishna_bharuch_smi_crm";
    }

    public void setapppackagename(String apppackagename) {
        this.apppackagename= apppackagename;
    }

    public String getgroupcode() {
        return groupcode;
    }

    public void setgroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    public String getusertype() {
        return usertype;
    }

    public void setusertype(String usertype) {
        this.usertype = usertype;
    }

    public String getemployeeimagename() {
        return employeeimagename;
    }

    public void setemployeeimagename(String employeeimagename) {
        this.employeeimagename = employeeimagename;
    }
}

