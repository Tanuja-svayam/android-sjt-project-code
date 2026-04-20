package com.shrikantelectronics;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Class which has Utility methods
 *
 */
public class Utility {

		public static String IPAddress;

	private static Pattern pattern;
	private static Matcher matcher;
	//Email Pattern
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Validate Email with regular expression
	 *
	 * @param email
	 * @return true for Valid Email and false for Invalid Email
	 */
	public static boolean validate(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}
	/**
	 * Checks for Null String object
	 *
	 * @param txt
	 * @return true for not null and false for null String object
	 */
	public static boolean isNotNull(String txt){
		return txt!=null && txt.trim().length()>0 ? true: false;
	}

	public static String getSafeString(JSONObject obj, String key) {
		if (obj.has(key) && !obj.isNull(key)) {
			return obj.optString(key, "");
		}
		return "";
	}

	public static String ConvetToDDMMMYYYY(int year, int month, int day) {

		String syear = "";
		String smonth = "";
		String sday = "";

		syear = "" + year;
		sday = "" + day;

		if(sday.length() == 1) {
			sday = "0" + sday;
		}

		switch(month)
		{
			case 0:
				smonth = "JAN";
				break;
			case 1:
				smonth = "FEB";
				break;
			case 2:
				smonth = "MAR";
				break;
			case 3:
				smonth = "APR";
				break;
			case 4:
				smonth = "MAY";
				break;
			case 5:
				smonth = "JUN";
				break;
			case 6:
				smonth = "JUL";
				break;
			case 7:
				smonth = "AUG";
				break;
			case 8:
				smonth = "SEP";
				break;
			case 9:
				smonth = "OCT";
				break;
			case 10:
				smonth = "NOV";
				break;
			case 11:
				smonth = "DEC";
				break;
		}

		return sday + "-" + smonth + "-" + syear;

	}


	public static String DoubleToString(Double value) {

		BigDecimal myNumber = new BigDecimal(value);

		Double myDouble = myNumber.doubleValue();

		NumberFormat formatter = new DecimalFormat("#.##");

		return formatter.format(myDouble);

	}


	public static void ExportTableLayoutContentToExcel(Activity activity, TableLayout tableLayout) {
		try {

			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String FileName = "Stock" + timeStamp + ".xls";

			String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
					+ "/" + FileName;
			File file = new File(filePath);

			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
			WritableWorkbook workbook;
			workbook = Workbook.createWorkbook(file, wbSettings);
			WritableSheet sheetA = workbook.createSheet("sheet A", 0);

			// Create Cell Format
			WritableFont boldWhiteFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
			boldWhiteFont.setColour(Colour.WHITE);
			WritableCellFormat headerFormat = new WritableCellFormat(boldWhiteFont);
			headerFormat.setBackground(Colour.DARK_BLUE);
			headerFormat.setAlignment(Alignment.CENTRE);
			headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			for (int i = 0; i < tableLayout.getChildCount(); i++) {
				TableRow tableRow = (TableRow) tableLayout.getChildAt(i);

				// Loop through TableRow Cells
				for (int j = 0; j < tableRow.getChildCount(); j++) {
					TextView textView = (TextView) tableRow.getChildAt(j);

					Label label = new Label(j, i, textView.getText().toString());

					if (i == 0) {
						label.setCellFormat(headerFormat);
					}


					sheetA.addCell(label);

				}
			}


			// close workbook
			workbook.write();
			workbook.close();

			Toast.makeText(activity, "Excel saved at: " + filePath, Toast.LENGTH_LONG).show();
			openExcel(activity, file);

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, "Error exporting PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


	public static void exportScrollViewContentToPdf(Activity activity, ScrollView scrollView) {
		try {
			// Capture full content as a Bitmap
			Bitmap bitmap = captureScrollView(scrollView);

			// Create PDF document
			PdfDocument pdfDocument = new PdfDocument();
			PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
			PdfDocument.Page page = pdfDocument.startPage(pageInfo);

			Canvas canvas = page.getCanvas();
			canvas.drawBitmap(bitmap, 0, 0, null);
			pdfDocument.finishPage(page);

			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String FileName = "Stock" + timeStamp + ".pdf";

			String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
					+ "/" + FileName;
			File file = new File(filePath);

		 	pdfDocument.writeTo(new FileOutputStream(file));
			pdfDocument.close();

			Toast.makeText(activity, "PDF saved at: " + filePath, Toast.LENGTH_LONG).show();
			openPdf(activity, file);

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, "Error exporting PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


	private static Bitmap captureScrollView(ScrollView scrollView) {
		int width = scrollView.getWidth();
		int height = 0;

		// Calculate total height of the ScrollView content
		for (int i = 0; i < scrollView.getChildCount(); i++) {
			height += scrollView.getChildAt(i).getHeight();
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		scrollView.draw(canvas);

		return bitmap;
	}

	private static void openPdf(Activity activity, File file) {
		try {
			Uri pdfUri = FileProvider.getUriForFile(activity,
					activity.getApplicationContext().getPackageName() + ".provider", file);

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(pdfUri, "application/pdf");
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			activity.startActivity(intent);
		} catch (Exception e) {
			Log.e("PdfExportUtility", "No app found to open the PDF", e);
			Toast.makeText(activity, "No app found to open the PDF", Toast.LENGTH_SHORT).show();
		}
	}

	public static void openExcel(Activity activity, File file) {
		try {
			Uri pdfUri = FileProvider.getUriForFile(activity,
					activity.getApplicationContext().getPackageName() + ".provider", file);

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(pdfUri, "application/xls");
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			activity.startActivity(intent);
		} catch (Exception e) {
			Log.e("ExcelExportUtility", "No app found to open the Exel", e);
			Toast.makeText(activity, "No app found to open the PDF", Toast.LENGTH_SHORT).show();
		}
	}


	// Common function usable from any Activity
	public static void downloadAndOpenPdf(Context context, String fileUrl, String fileName) {
		new AsyncTask<String, Void, File>() {

			@Override
			protected File doInBackground(String... strings) {
				String url = strings[0];
				String name = strings[1];

				File pdfFile = new File(context.getExternalFilesDir(null), name);
				try {
					if (!pdfFile.exists()) pdfFile.createNewFile();
					FileDownloader.downloadFile(url, pdfFile); // existing logic
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return pdfFile;
			}

			@Override
			protected void onPostExecute(File file) {
				if (file != null) {
					try {
						Uri pdfUri = FileProvider.getUriForFile(
								context,
								context.getPackageName() + ".provider",
								file
						);

						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(pdfUri, "application/pdf");
						intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						context.startActivity(Intent.createChooser(intent, "Open PDF"));
					} catch (ActivityNotFoundException e) {
						Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						Toast.makeText(context, "Error opening file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				} else {
					Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute(fileUrl, fileName);
	}
}


