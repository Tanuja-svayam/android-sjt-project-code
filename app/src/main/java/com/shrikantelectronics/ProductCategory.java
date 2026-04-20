package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class ProductCategory {

    private String sysproductcategoryno;
    private String description;


    public ProductCategory(String sysproductcategoryno, String description) {
        this.sysproductcategoryno = sysproductcategoryno;
        this.description = description;
    }

    public String getsysproductcategoryno() {
        return this.sysproductcategoryno;
    }

    public String getdescription() {
        return this.description;
    }

}
