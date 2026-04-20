package com.shrikantelectronics;

import android.app.Activity;
import android.os.Environment;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.File;
import java.util.Locale;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CreatePlainXLSX {

    public static void export(Activity activity, TableLayout tableLayout) {

        try {

            //file path
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/example.xls";
            File file = new File(filePath);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            //Excel sheetA first sheetA
            WritableSheet sheetA = workbook.createSheet("sheet A", 0);

            /*
            // column and row titles
            sheetA.addCell(new Label(0, 0, "sheet A 1"));
            sheetA.addCell(new Label(1, 0, "sheet A 2"));
            sheetA.addCell(new Label(0, 1, "sheet A 3"));
            sheetA.addCell(new Label(1, 1, "sheet A 4"));


            //Excel sheetB represents second sheet
            WritableSheet sheetB = workbook.createSheet("sheet B", 1);

            // column and row titles
            sheetB.addCell(new Label(0, 0, "sheet B 1"));
            sheetB.addCell(new Label(1, 0, "sheet B 2"));
            sheetB.addCell(new Label(0, 1, "sheet B 3"));
            sheetB.addCell(new Label(1, 1, "sheet B 4"));
*/

            for (int i = 0; i < tableLayout.getChildCount(); i++) {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(i);

                // Loop through TableRow Cells
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    TextView textView = (TextView) tableRow.getChildAt(j);

                    sheetA.addCell(new Label(j, i, textView.getText().toString()));


                }
            }


            // close workbook
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}