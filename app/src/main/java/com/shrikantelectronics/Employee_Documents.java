package com.shrikantelectronics;

/**
 * Created by Administrator on 10/12/2016.
 */

public class Employee_Documents {

    private String sysdocumentno;
    private String sysemployeeno;
    private String employeename;
    private String document_name;
    private String vuploaddate;
    private String vvaliddate;
    private String rootpath;
    private String foldername;
    private String file_name;
    private String file_type;
    private String mime_type;
    private String mobilefileuri;
    private String latitude;
    private String longitude;
    private String parameters;
    private String documenturl;

    public Employee_Documents(String sysdocumentno, String sysemployeeno, String employeename, String  document_name, String  vuploaddate, String vvaliddate, String rootpath, String foldername, String file_name, String file_type, String mime_type, String mobilefileuri, String latitude, String longitude, String parameters, String documenturl) {

        this.sysdocumentno    = sysdocumentno;
        this.sysemployeeno    = sysemployeeno;
        this.employeename     = employeename;
        this.document_name    = document_name;
        this.vuploaddate      = vuploaddate;
        this.vvaliddate       = vvaliddate;
        this.rootpath	       = rootpath;
        this.foldername       = foldername;
        this.file_name	       = file_name;
        this.file_type	       = file_type;
        this.mime_type	       = mime_type;
        this.mobilefileuri    = mobilefileuri;
        this.latitude	       = latitude;
        this.longitude	       = longitude;
        this.parameters       = parameters;
        this.documenturl      = documenturl;

    }

    public String getsysdocumentno() {
        return this.sysdocumentno;
    }


    public String getsysemployeeno() {
        return this.sysemployeeno;
    }

    public String getemployeename() {
        return this.employeename;
    }

    public String getdocument_name() {
        return this.document_name;
    }

    public String getvuploaddate() {
        return this.vuploaddate;
    }

    public String getvvaliddate() {
        return this.vvaliddate;
    }

    public String getrootpath() {
        return this.rootpath;
    }

    public String getfoldername() {
        return this.foldername;
    }

    public String getfile_name() {
        return this.file_name;
    }

    public String getfile_type() {
        return this.file_type;
    }

    public String getmime_type() {
        return this.mime_type;
    }

    public String getmobilefileuri() {
        return this.mobilefileuri;
    }

    public String getparameters() {
        return this.parameters;
    }

    public String getdocumenturl() {
        return this.documenturl;
    }
}