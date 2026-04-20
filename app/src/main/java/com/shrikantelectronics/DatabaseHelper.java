package com.shrikantelectronics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "AppDatabase.db";
	private static final int DATABASE_VERSION = 4;

	// Table for user registration
	public static final String TABLE_USER = "Registration";
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_NAME = "storename";
	public static final String COLUMN_MOBILE = "registeredmobile";
	public static final String COLUMN_EMAIL = "registeredemail";
	public static final String COLUMN_STATICIP = "staticip";
	public static final String COLUMN_DATABASE = "authdatabase";
	public static final String COLUMN_AUTHKEY = "authkey";
	public static final String COLUMN_USERID = "userid";
	public static final String COLUMN_PASSWORD = "password";
	public static final String WEBSERVICE_URL = "webservice_url";
	public static final String IMAGEURL_URL = "imageurl_url";
	public static String SVAYAM_URL_IP = "svayam_url_ip";
	public static String SVAYAM_IMAGE_FOR_HTDOCS_UPLOAD = "svayam_image_for_htdocs_upload";

	// SQL query to create user table
	private static final String CREATE_USER_TABLE =
			"CREATE TABLE " + TABLE_USER + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_NAME + " TEXT, " +
					COLUMN_MOBILE + " TEXT, " +
					COLUMN_EMAIL + " TEXT, " +
					COLUMN_STATICIP + " TEXT, " +
					COLUMN_DATABASE + " TEXT, " +
					COLUMN_AUTHKEY + " TEXT, " +
					COLUMN_USERID + " TEXT, " +
					COLUMN_PASSWORD + " TEXT, " +
					WEBSERVICE_URL + " TEXT, " +
					IMAGEURL_URL + " TEXT, " +
					SVAYAM_IMAGE_FOR_HTDOCS_UPLOAD + " TEXT, " +
					SVAYAM_URL_IP + " TEXT );";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		onCreate(db);
	}
}
