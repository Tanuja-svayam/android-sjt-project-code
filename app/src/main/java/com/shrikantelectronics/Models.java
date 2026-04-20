package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Models {

    private String sysmodelno;
    private String modelcode;
    private String sysproductcategoryno_top;
    private String sysproductcategoryno_parent;
    private String sysproductcategoryno;
    private String topcategoryname;
    private String parentcategoryname;
    private String categoryname;
    private String modelimageurl;
    private String searchfield;
    private String stock_stk;
    private String stock_bkg;
    private String stock_avl;
    private String mrp;
    private String dp;
    private String slc;

    public Models(String sysmodelno, String modelcode,
                  String sysproductcategoryno_top, String sysproductcategoryno_parent, String sysproductcategoryno, String topcategoryname,
                  String parentcategoryname, String categoryname, String modelimageurl, String searchfield,
                  String stock_stk, String stock_bkg, String stock_avl,
                  String mrp, String dp, String slc) {

        this.sysmodelno = sysmodelno;
        this.modelcode = modelcode;
        this.sysproductcategoryno_top = sysproductcategoryno_top;
        this.sysproductcategoryno_parent = sysproductcategoryno_parent;
        this.sysproductcategoryno = sysproductcategoryno;
        this.topcategoryname = topcategoryname;
        this.parentcategoryname = parentcategoryname;
        this.categoryname = categoryname;
        this.modelimageurl = modelimageurl;
        this.searchfield = searchfield;
        this.stock_stk = stock_stk;
        this.stock_bkg = stock_bkg;
        this.stock_avl = stock_avl;
        this.mrp = mrp;
        this.dp= dp;
        this.slc= slc;
    }


    public String getmodelimageurl() {
        return this.modelimageurl;
    }

    public String getsearchfield() {
        return this.searchfield;
    }

    public String getsysmodelno() {
        return this.sysmodelno;
    }

    public String getmodelcode() {
        return this.modelcode;
    }

    public String getcategoryname() {
        return this.categoryname;
    }

    public String getsysproductcategoryno_top() {
        return this.sysproductcategoryno_top;
    }

    public String getsysproductcategoryno_parent() {
        return this.sysproductcategoryno_parent;
    }

    public String getsysproductcategoryno() {
        return this.sysproductcategoryno;
    }

    public String gettopcategoryname() {
        return this.topcategoryname;
    }

    public String getparentcategoryname() {
        return this.parentcategoryname;
    }

    public String getstock_stk() {
        return this.stock_stk;
    }
    public String getstock_bkg() {
        return this.stock_bkg;
    }
    public String getstock_avl() {
        return this.stock_avl;
    }

    public String getmrp() {
        return this.mrp;
    }
    public String getdp() {
        return this.dp;
    }
    public String getslc() {
        return this.slc;
    }

}
