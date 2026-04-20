package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class TidMaster {

    private String systidno;
    private String tiddescription;


    public TidMaster(String systidno, String tiddescription) {
        this.systidno = systidno;
        this.tiddescription = tiddescription;
    }

    public String getsystidno() {
        return this.systidno;
    }

    public String gettiddescription() {
        return this.tiddescription;
    }

}
