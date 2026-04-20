package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class State {

    private String sysstateno;
    private String statename;


    public State(String sysstateno, String statename) {
        this.sysstateno = sysstateno;
        this.statename = statename;
    }

    public String getsysstateno() {
        return this.sysstateno;
    }

    public String getstatename() {
        return this.statename;
    }

}
