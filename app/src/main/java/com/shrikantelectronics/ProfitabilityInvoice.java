package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class ProfitabilityInvoice {

    private String Row_id;
    private String id;
    private String sysproductno;
    private String sysbrandno;
    private String location;
    private String executive_name;
    private String invoice_no;
    private String invoice_date;
    private String invoicedt;
    private String customer_name;
    private String brand;
    private String model;
    private String top_category;
    private String parent_category;
    private String category;
    private String serial_no;
    private String installer_name;
    private String installer_contact;
    private String slc_at_sale;
    private String kitty;
    private String remarks;
    private String gstn;
    private String customer_mobile;
    private String alternate_number;
    private String hsncode;
    private String ageingfromdate;
    private String ageing;
    private String basic;
    private String quantity;
    private String cgst_taxrate;
    private String cgst;
    private String sgst_taxrate;
    private String sgst;
    private String igst_taxrate;
    private String igst;
    private String tax;
    private String unitprice_include_tax;
    private String purchaseprice;
    private String gp;
    private String invoiceorder_creditnote_amount;
    private String transportcharges;
    private String installationcharges;
    private String bankcharges;
    private String overheads;
    private String exchangeshortexcess;
    private String salesexpenses;
    private String pricedropvalue;
    private String cdsupport;
    private String selloutsupport;
    private String general_creditnote;
    private String scheme_creditnote;
    private String purchasediscount;
    private String netsalesamount;
    private String net_nlc_including_pd_so;
    private String profitloss;

    public ProfitabilityInvoice(String Row_id,
                                String id,
                                String sysproductno,
                                String sysbrandno,
                                String location,
                                String executive_name,
                                String invoice_no,
                                String invoice_date,
                                String invoicedt,
                                String customer_name,
                                String brand,
                                String model,
                                String top_category,
                                String parent_category,
                                String category,
                                String serial_no,
                                String installer_name,
                                String installer_contact,
                                String slc_at_sale,
                                String kitty,
                                String remarks,
                                String gstn,
                                String customer_mobile,
                                String alternate_number,
                                String hsncode,
                                String ageingfromdate,
                                String ageing,
                                String basic,
                                String quantity,
                                String cgst_taxrate,
                                String cgst,
                                String sgst_taxrate,
                                String sgst,
                                String igst_taxrate,
                                String igst,
                                String tax,
                                String unitprice_include_tax,
                                String purchaseprice,
                                String gp,
                                String invoiceorder_creditnote_amount,
                                String transportcharges,
                                String installationcharges,
                                String bankcharges,
                                String overheads,
                                String exchangeshortexcess,
                                String salesexpenses,
                                String pricedropvalue,
                                String cdsupport,
                                String selloutsupport,
                                String general_creditnote,
                                String scheme_creditnote,
                                String purchasediscount,
                                String netsalesamount,
                                String net_nlc_including_pd_so,
                                String profitloss) {
        this.Row_id                          = Row_id;
        this.id				     = id;
        this.sysproductno		     = sysproductno;
        this.sysbrandno			     = sysbrandno;
        this.location			     = location;
        this.executive_name		     = executive_name;
        this.invoice_no			     = invoice_no;
        this.invoice_date		     = invoice_date;
        this.invoicedt			     = invoicedt;
        this.customer_name		     = customer_name;
        this.brand			     = brand;
        this.model			     = model;
        this.top_category		     = top_category;
        this.parent_category		     = parent_category;
        this.category			     = category;
        this.serial_no			     = serial_no;
        this.installer_name		     = installer_name;
        this.installer_contact		     = installer_contact;
        this.slc_at_sale		     = slc_at_sale;
        this.kitty			     = kitty;
        this.remarks			     = remarks;
        this.gstn			     = gstn;
        this.customer_mobile		     = customer_mobile;
        this.alternate_number		     = alternate_number;
        this.hsncode			     = hsncode;
        this.ageingfromdate		     = ageingfromdate;
        this.ageing			     = ageing;
        this.basic			     = basic;
        this.quantity			     = quantity;
        this.cgst_taxrate		     = cgst_taxrate;
        this.cgst			     = cgst;
        this.sgst_taxrate		     = sgst_taxrate;
        this.sgst			     = sgst;
        this.igst_taxrate		     = igst_taxrate;
        this.igst			     = igst;
        this.tax			     = tax;
        this.unitprice_include_tax	     = unitprice_include_tax;
        this.purchaseprice		     = purchaseprice;
        this.gp				     = gp;
        this.invoiceorder_creditnote_amount  = invoiceorder_creditnote_amount;
        this.transportcharges		     = transportcharges;
        this.installationcharges	     = installationcharges;
        this.bankcharges		     = bankcharges;
        this.overheads			     = overheads;
        this.exchangeshortexcess	     = exchangeshortexcess;
        this.salesexpenses		     = salesexpenses;
        this.pricedropvalue		     = pricedropvalue;
        this.cdsupport			     = cdsupport;
        this.selloutsupport		     = selloutsupport;
        this.general_creditnote		     = general_creditnote;
        this.scheme_creditnote		     = scheme_creditnote;
        this.purchasediscount		     = purchasediscount;
        this.netsalesamount		     = netsalesamount;
        this.net_nlc_including_pd_so	     = net_nlc_including_pd_so;
        this.profitloss			     = profitloss;

    }

    public String getRow_id                          () { return this.Row_id;}
    public String getid				     () { return this.id;}
    public String getsysproductno		     () { return this.sysproductno;}
    public String getsysbrandno			     () { return this.sysbrandno;}
    public String getlocation			     () { return this.location;}
    public String getexecutive_name		     () { return this.executive_name;}
    public String getinvoice_no			     () { return this.invoice_no;}
    public String getinvoice_date		     () { return this.invoice_date;}
    public String getinvoicedt			     () { return this.invoicedt;}
    public String getcustomer_name		     () { return this.customer_name;}
    public String getbrand			     () { return this.brand;}
    public String getmodel			     () { return this.model;}
    public String gettop_category		     () { return this.top_category;}
    public String getparent_category		     () { return this.parent_category;}
    public String getcategory			     () { return this.category;}
    public String getserial_no			     () { return this.serial_no;}
    public String getinstaller_name		     () { return this.installer_name;}
    public String getinstaller_contact		     () { return this.installer_contact;}
    public String getslc_at_sale		     () { return this.slc_at_sale;}
    public String getkitty			     () { return this.kitty;}
    public String getremarks			     () { return this.remarks;}
    public String getgstn			     () { return this.gstn;}
    public String getcustomer_mobile		     () { return this.customer_mobile;}
    public String getalternate_number		     () { return this.alternate_number;}
    public String gethsncode			     () { return this.hsncode;}
    public String getageingfromdate		     () { return this.ageingfromdate;}
    public String getageing			     () { return this.ageing;}
    public String getbasic			     () { return this.basic;}
    public String getquantity			     () { return this.quantity;}
    public String getcgst_taxrate		     () { return this.cgst_taxrate;}
    public String getcgst			     () { return this.cgst;}
    public String getsgst_taxrate		     () { return this.sgst_taxrate;}
    public String getsgst			     () { return this.sgst;}
    public String getigst_taxrate		     () { return this.igst_taxrate;}
    public String getigst			     () { return this.igst;}
    public String gettax			     () { return this.tax;}
    public String getunitprice_include_tax	     () { return this.unitprice_include_tax;}
    public String getpurchaseprice		     () { return this.purchaseprice;}
    public String getgp				     () { return this.gp;}
    public String getinvoiceorder_creditnote_amount  () { return this.invoiceorder_creditnote_amount;}
    public String gettransportcharges		     () { return this.transportcharges;}
    public String getinstallationcharges	     () { return this.installationcharges;}
    public String getbankcharges		     () { return this.bankcharges;}
    public String getoverheads			     () { return this.overheads;}
    public String getexchangeshortexcess	     () { return this.exchangeshortexcess;}
    public String getsalesexpenses		     () { return this.salesexpenses;}
    public String getpricedropvalue		     () { return this.pricedropvalue;}
    public String getcdsupport			     () { return this.cdsupport;}
    public String getselloutsupport		     () { return this.selloutsupport;}
    public String getgeneral_creditnote		     () { return this.general_creditnote;}
    public String getscheme_creditnote		     () { return this.scheme_creditnote;}
    public String getpurchasediscount		     () { return this.purchasediscount;}
    public String getnetsalesamount		     () { return this.netsalesamount;}
    public String getnet_nlc_including_pd_so	     () { return this.net_nlc_including_pd_so;}
    public String getprofitloss			     () { return this.profitloss;}


}

