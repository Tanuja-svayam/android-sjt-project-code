package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class LocationwiseStockSummary{
    private String ID;
    private String Brand;
    private String Main_Category;
    private String Sub_Category;
    private String Category;
    private String Model;
    private String KA;
    private String SU;
    private String BA;
    private String UN;
    private String MN;
    private String SG;
    private String BG;
    private String KA_DEF;
    private String SU_DEF;
    private String BA_DEF;
    private String GD_DEF;
    private String SIT;
    private String CL;
    private String Total;

    public LocationwiseStockSummary(String ID,
                                       String Brand,
                                       String Main_Category,
                                       String Sub_Category,
                                       String Category,
                                       String Model,
                                       String KA,
                                       String SU,
                                       String BA,
                                       String UN,
                                       String MN,
                                       String SG,
                                       String BG,
                                       String KA_DEF,
                                       String SU_DEF,
                                       String BA_DEF,
                                       String GD_DEF,
                                       String SIT,
                                       String CL,
                                       String Total

    ) {
        this.ID = ID;
        this.Brand = Brand;
        this.Main_Category = Main_Category;
        this.Sub_Category = Sub_Category;
        this.Category = Category;
        this.Model = Model;
        this.KA = KA;
        this.SU = SU;
        this.BA = BA;
        this.UN = UN;
        this.MN = MN;
        this.SG = SG;
        this.BG = BG;
        this.KA_DEF = KA_DEF;
        this.SU_DEF = SU_DEF;
        this.BA_DEF = BA_DEF;
        this.GD_DEF = GD_DEF;
        this.SIT = SIT;
        this.CL = CL;
        this.Total = Total;
    }

    public String getID() {
        return this.ID;
    }
    public String getBrand() {
        return this.Brand;
    }
    public String getMain_Category() {
        return this.Main_Category;
    }
    public String getSub_Category() {
        return this.Sub_Category;
    }
    public String getCategory() {
        return this.Category;
    }
    public String getModel() {
        return this.Model;
    }

    public String getKA() {
        return this.KA;
    }
    public String getSU() {
        return this.SU;
    }
    public String getBA() {
        return this.BA;
    }
    public String getUN() {
        return this.UN;
    }
    public String getMN() {
        return this.MN;
    }
    public String getSG() {
        return this.SG;
    }
    public String getBG() {
        return this.BG;
    }

    public String getKA_DEF() {
        return this.KA_DEF;
    }
    public String getSU_DEF() {
        return this.SU_DEF;
    }
    public String getBA_DEF() {
        return this.BA_DEF;
    }
    public String getGD_DEF() {
        return this.GD_DEF;
    }
    public String getSIT() {
        return this.SIT;
    }
    public String getCL() {
        return this.CL;
    }
    public String getTotal() {
        return this.Total;
    }
}
