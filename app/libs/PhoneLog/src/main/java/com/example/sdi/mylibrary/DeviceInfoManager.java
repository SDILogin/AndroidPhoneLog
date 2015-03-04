package com.example.sdi.mylibrary;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

/**
 * Created by SDI on 04.03.15.
 *
 * Contains static methods.
 * Methods from this class returns info about current device.
 */
public class DeviceInfoManager {

    public static String getScreenSizeDescription(Activity someActivity){
        Display display = someActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return String.format("width: %d, height: %h", width, height);
    }

    public static String getSDKVersion(){
       return String.format("sdk version: %d", Build.VERSION.SDK_INT);
    }

    public static String getDeviceModelName(){
       return String.format("device: %s", Devices.getDeviceName());
    }
}
