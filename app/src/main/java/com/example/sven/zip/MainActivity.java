package com.example.sven.zip;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }

        // 解压   7zr x [压缩文件]  -o[输出目录]
        // 压缩   7zr a [输出文件] [待压缩文件/目录] -mx=9
        File src = new File(Environment.getExternalStorageDirectory(), "7-Zip");
        File out = new File(Environment.getExternalStorageDirectory(), "7-Zip.7z");
        int result = ZipCode.exec("7zr a " + out.getAbsolutePath() + " "
                + src.getAbsolutePath() + " -mx=9");
        Log.e("Sven", "ZipCode.exec: "+result);

    }

}
