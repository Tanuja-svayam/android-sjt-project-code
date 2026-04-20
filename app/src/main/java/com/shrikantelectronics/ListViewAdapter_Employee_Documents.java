package com.shrikantelectronics;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListViewAdapter_Employee_Documents extends BaseAdapter {

    // Declare Variables
    DownloadManager manager;
    Context mContext;
    LayoutInflater inflater;
    private List<Employee_Documents> EmployeeDocumentList = null;
    private ArrayList<Employee_Documents> arraylist;

    public ListViewAdapter_Employee_Documents(Context context,
                                              List<Employee_Documents> EmployeeDocumentList) {
        mContext = context;
        this.EmployeeDocumentList = EmployeeDocumentList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Employee_Documents>();
        this.arraylist.addAll(EmployeeDocumentList);
    }

    public class ViewHolder {
        EditText employeename;
        EditText document_name;
        EditText vuploaddate;
        EditText vvaliddate;
        EditText file_name;
        Button download;
    }

    @Override
    public int getCount() {
        return EmployeeDocumentList.size();
    }

    @Override
    public Employee_Documents getItem(int position) {
        return EmployeeDocumentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_employee_documents_view_list, null);
            // Locate the TextViews in listview_item.xml
            holder.employeename = (EditText) view.findViewById(R.id.employeename);
            holder.document_name = (EditText) view.findViewById(R.id.document_name);
            holder.vuploaddate = (EditText) view.findViewById(R.id.vuploaddate);
            holder.vvaliddate = (EditText) view.findViewById(R.id.vvaliddate);
            holder.file_name = (EditText) view.findViewById(R.id.file_name);
            holder.download = (Button) view.findViewById(R.id.download);

            // Locate the ImageView in listview_item.xml

           // holder.modelimage = (ImageView) view.findViewById(R.id.modelimage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.employeename.setText(EmployeeDocumentList.get(position).getemployeename());
        holder.document_name.setText(EmployeeDocumentList.get(position).getdocument_name());
        holder.vuploaddate.setText(EmployeeDocumentList.get(position).getvuploaddate());
        holder.vvaliddate.setText(EmployeeDocumentList.get(position).getvvaliddate());
        holder.file_name.setText(EmployeeDocumentList.get(position).getfile_name());

        holder.download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(EmployeeDocumentList.get(position).getdocumenturl());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                long reference = manager.enqueue(request);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        EmployeeDocumentList.clear();
        if (charText.length() == 0) {
            EmployeeDocumentList.addAll(arraylist);
        } else {
            for (Employee_Documents wp : arraylist) {
                if (wp.getdocument_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    EmployeeDocumentList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}