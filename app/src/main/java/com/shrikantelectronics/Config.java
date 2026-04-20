package com.shrikantelectronics;

import android.os.Environment;
import android.widget.Button;

import java.util.Calendar;

public class Config  {
	// File upload url (replace the ip with your server address) http://192.168.1.10/

	public static String FILE_UPLOAD_URL ="";
	public static String SERVICE_IMAGE_UPLOAD_URL ="";
	public static String EMPLOYEE_DOCUMENT_UPLOAD_URL ="";
	public static String WEBSERVICE_URL  ="";
	public static String IMAGEURL_URL  ="";
	public static String STATICIP  ="";
	public static String SVAYAM_URL_IP  ="103.203.230.97";

	static Calendar c = Calendar.getInstance();
	static int year  = c.get(Calendar.YEAR);
	static int month = c.get(Calendar.MONTH);
	static int day  = c.get(Calendar.DAY_OF_MONTH);

	public static String FROMDATE =  Utility.ConvetToDDMMMYYYY(year,month,1);
	public static String TODATE  = Utility.ConvetToDDMMMYYYY(year,month,day);

	public static final String IMAGE_DIRECTORY_NAME =  "Android File Upload";

	public static String ServiceProviderCode;

}