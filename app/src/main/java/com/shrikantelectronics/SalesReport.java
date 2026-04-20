package com.shrikantelectronics;

public class SalesReport {
    private String modelName;
    private String customerName;
    private String quantity;
    private String amount;
    private String location;
    private String salesExecutive;
    private String invorderno;
    private String invorderdt;
    private String sysinvno;
    private String model_instock_qty;
    private String profitloss;
    private String custcd;

    public SalesReport(String invorderno, String invorderdt, String modelName, String customerName, String quantity, String amount, String location,
                       String salesExecutive, String model_instock_qty, String profitloss, String sysinvno, String custcd) {

        this.invorderno = invorderno;
        this.invorderdt = invorderdt;
        this.modelName = modelName;
        this.customerName = customerName;
        this.quantity = quantity;
        this.amount = amount;
        this.location = location;
        this.salesExecutive = salesExecutive;
        this.model_instock_qty = model_instock_qty;
        this.profitloss = profitloss;
        this.sysinvno = sysinvno;
        this.custcd = custcd;
    }

    public String getsysinvno() { return sysinvno; }
    public String getinvorderno() { return invorderno; }
    public String getinvorderdt() { return invorderdt; }
    public String getModelName() { return modelName; }
    public String getCustomerName() { return customerName; }
    public String getQuantity() { return quantity; }
    public String getAmount() { return amount; }
    public String getLocation() { return location; }
    public String getSalesExecutive() { return salesExecutive; }
    public String getmodel_instock_qty() { return model_instock_qty; }
    public String getprofitloss() { return profitloss; }
    public String getcustcd() { return custcd; }

}
