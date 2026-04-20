package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Company {

    private String companycd;
    private String companyname;
    private String contactpersonmobile;
    private String cmpemail;


    public Company(String companycd, String companyname, String contactpersonmobile, String cmpemail) {

        this.companycd = companycd;
        this.companyname = companyname;
        this.contactpersonmobile= contactpersonmobile;
        this.cmpemail = cmpemail;
    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getcompanyname() {
        return this.companyname;
    }

    public String getcontactpersonmobile() {
        return this.contactpersonmobile;
    }

    public String getcmpemail() {
        return this.cmpemail;
    }

}
