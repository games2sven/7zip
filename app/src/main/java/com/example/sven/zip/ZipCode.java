package com.example.sven.zip;

public class ZipCode {
    static {
        System.loadLibrary("native-lib");
    }

    public native static int exec(String cmd);


}
