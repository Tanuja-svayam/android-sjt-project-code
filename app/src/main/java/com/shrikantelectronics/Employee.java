package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Employee {

    private String sysemployeeno;
    private String fullname;


    public Employee(String sysemployeeno, String fullname) {
        this.sysemployeeno = sysemployeeno;
        this.fullname = fullname;
    }

    public String getsysemployeeno() {
        return this.sysemployeeno;
    }

    public String getfullname() {
        return this.fullname;
    }

}
