package com.example.main;

import android.Manifest;
import android.app.Activity;

/**
 * Created by ebao on 2016/11/19.
 */

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.ChoosePDFActivity;
import com.artifex.mupdfdemo.MuPDFActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;


class FileDownloader {
    private static final int  MEGABYTE = 1024 * 1024;

    public static void downloadFile(String fileUrl, File directory){
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class MainActivity extends Activity {
    Button download;
    Button button_local;
    EditText edit_url;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(MainActivity.this);
        setContentView(R.layout.home);
        download = (Button) findViewById(R.id.button_net);
        download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("DEBUG click");
                download(v);
//                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/temppdf/" + "temp.pdf");  // -> filename = temp.pdf
//                Uri uri = Uri.fromFile(pdfFile);
//                Intent intent = new Intent(MainActivity.this,MuPDFActivity.class);
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setData(uri);
//                try {
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        });
        button_local = (Button) findViewById(R.id.button_local);
        button_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, ChoosePDFActivity.class);
                startActivity(intent);
            }
        });

    }

    public void download(View v) {
        edit_url = (EditText) findViewById(R.id.edit_url);
        String urlStr = edit_url.getText().toString();
        new DownloadFile().execute(urlStr, "temp.pdf");
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {  //async !!!!

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> url
            String fileName = strings[1];  // -> temp.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "temppdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/temppdf/" + "temp.pdf");  // -> filename = temp.pdf
            Uri uri = Uri.fromFile(pdfFile);
            Intent intent = new Intent(MainActivity.this,MuPDFActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

