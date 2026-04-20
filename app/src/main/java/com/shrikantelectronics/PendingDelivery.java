package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class PendingDelivery {

    private String sysorderdtlno;
    private String vinvorderdt;
    private String invorderno;
    private String custname;
    private String custaddress;
    private String custdeliveryaddress;
    private String customermobileno;
    private String custtelephone;
    private String custemailid;
    private String topcategoryname;
    private String parentcategoryname;
    private String categoryname;
    private String brandname;
    private String modelname;
    private String serialno;
    private String picklocationname;
    private String vexpected_delivery_date;
    private String delorderno;
    private String transportcharges;
    private String deliveryinstruction;
    private String delivery_status;
    private String seachfield;
    private String sysmodelno;
    private String companycd;
    private String sysinvorderno;

public PendingDelivery(String sysorderdtlno, String vinvorderdt, String invorderno, String custname, String custaddress,
                       String custdeliveryaddress,
                       String customermobileno,
                       String custtelephone,
                       String custemailid,
                       String topcategoryname,
                       String parentcategoryname,
                       String categoryname,
                       String brandname,
                       String modelname,
                       String serialno,
                       String picklocationname,
                       String vexpected_delivery_date,
                       String delorderno,
                       String transportcharges,
                       String deliveryinstruction,
                       String delivery_status,
                       String seachfield,
                       String sysmodelno,
                       String sysinvorderno,
                       String companycd) {
        this.sysorderdtlno = sysorderdtlno;
        this.vinvorderdt = vinvorderdt;
        this.invorderno = invorderno;
        this.custname = custname;
        this.custaddress = custaddress;
        this.custdeliveryaddress = custdeliveryaddress;
    this.customermobileno = customermobileno;
    this.custtelephone = custtelephone;
    this.custemailid = custemailid;
    this.topcategoryname = topcategoryname;
    this.parentcategoryname = parentcategoryname;
    this.categoryname = categoryname;
    this.brandname = brandname;
    this.modelname = modelname;
    this.serialno = serialno;
    this.picklocationname = picklocationname;
    this.vexpected_delivery_date = vexpected_delivery_date;
    this.delorderno = delorderno;
    this.transportcharges = transportcharges;
    this.deliveryinstruction = deliveryinstruction;
    this.delivery_status = delivery_status;
    this.seachfield = seachfield;
    this.sysmodelno = sysmodelno;
    this.sysinvorderno = sysinvorderno;
    this.companycd = companycd;

    }

    public String getsysorderdtlno() {
        return this.sysorderdtlno;
    }

    public String getvinvorderdt() {
        return this.vinvorderdt;
    }

    public String getinvorderno() {
        return this.invorderno;
    }

    public String getcustname() {
        return this.custname;
    }

    public String getcustaddress() {
        return this.custaddress;
    }

    public String getcustdeliveryaddress() {
        return this.custdeliveryaddress;
    }

    public String getcustomermobileno() {
        return this.customermobileno;
    }

    public String getcusttelephone() {
        return this.custtelephone;
    }

    public String getcustemailid() {
        return this.custemailid;
    }

    public String gettopcategoryname() {
        return this.topcategoryname;
    }

    public String getparentcategoryname() {
        return this.parentcategoryname;
    }

    public String getcategoryname() {
        return this.categoryname;
    }

    public String getbrandname() {
        return this.brandname;
    }

    public String getmodelname() {
        return this.modelname;
    }

    public String getserialno() {
        return this.serialno;
    }

    public String getpicklocationname() {
        return this.picklocationname;
    }

    public String getvexpected_delivery_date() {
        return this.vexpected_delivery_date;
    }

    public String getdelorderno() {
        return this.delorderno;
    }

    public String gettransportcharges() {
        return this.transportcharges;
    }

    public String getdeliveryinstruction() {
        return this.deliveryinstruction;
    }

    public String getdelivery_status() {
        return this.delivery_status;
    }

    public String getseachfield() {
        return this.seachfield;
    }

    public String getsysmodelno() {
        return this.sysmodelno;
    }

    public String getcompanycd() {
        return this.companycd;
    }

    public String getsysinvorderno() {
        return this.sysinvorderno;
    }

}
