package com.example.sdi.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.view.Display;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by SDI on 03.03.15.
 */
public class PhoneLog {
    public final static String WRITE_STATUS_OK = "OK";

    private static volatile PhoneLog mLog = null;
    private static String mLogFileName = "Log.log";

    private static File mLogFile = null;
    private static FileOutputStream mLogFileOutputStream = null;
    private static OutputStreamWriter mOutputStreamWriter = null;

    private final static SimpleDateFormat mPrefixDateFormat = new SimpleDateFormat("hh:mm:ss(SSS) :\t");

    private static FileManager mFileManager = null;

    /**
     * @param context: some activity from the application
     * */
    public void savePhoneSize( Activity context ){
        append(DeviceInfoManager.getScreenSizeDescription(context));
    }

    /**
     * 16
     * */
    public void saveSDKVersion(){
        append(DeviceInfoManager.getSDKVersion());
    }

    /**
     * example: Samsung galaxy star plus
     * */
    public void saveDevice() {
        append(DeviceInfoManager.getDeviceModelName());
    }

    private PhoneLog(){
        // private constructor;
        mFileManager = new FileManager(); // .../Log by default
        createFile(FileManager.isExternalStorageWritable(), true);
    }

    /**
    * http://habrahabr.ru/post/129494/
    * */
    public static PhoneLog getInstance(){
        PhoneLog localInstance = mLog;
        if (localInstance == null){
            synchronized (PhoneLog.class){
                localInstance = mLog;
                if (localInstance == null){
                    mLog = localInstance = new PhoneLog();
                }
            }
        }
        return localInstance;
    }

    /**
     * set name of log file on sdCard.
     * @param logName: new name of logFile.
     * */
    public static void setLogName(String logName){
        mLogFileName = logName;
    }

    /**
     * delete all files from log folder
     * */
    public static void clearLogFolder(){
        mFileManager.clearFolder();

        // force creation of the new instance
        mLog = null;
    }

    /**
     * create new log file with name <<nameOfTheNewLog>> and delete previous if it needed
     *
     * @param nameOfTheNewLog: name.ext of new log file
     * @param deletePreviousLogFile: previous log file will be deleted if TRUE
    * */
    public static void writeToNewNewLog(String nameOfTheNewLog, boolean deletePreviousLogFile){
        if (mLogFile == null){
            setLogName(nameOfTheNewLog);
            createFile(FileManager.isExternalStorageWritable(), true);
        } else {
            setLogName(nameOfTheNewLog);
            createFile(FileManager.isExternalStorageWritable(), deletePreviousLogFile);
        }
    }

    /**
     * @param loggedMessage : message that will be added to log file
     * @return: status of write operation.
     *          must be OK.
    * */
    public String append(String loggedMessage){
        try {
            String formatedDate = mPrefixDateFormat.format(Calendar.getInstance().getTime());
            StringBuilder sb = new StringBuilder(formatedDate).append(loggedMessage).append("\n");
            mOutputStreamWriter.append(sb.toString());
            mOutputStreamWriter.flush();
            return WRITE_STATUS_OK;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     * @param isExternalStorageAvailable: create file on sdCard if TRUE
     * @param deleteOldFile: file will be deleted if TRUE
     * */
    private static void createFile(boolean isExternalStorageAvailable, boolean deleteOldFile){
        if (isExternalStorageAvailable) {
            // write ot external storage

            // delete prev file if exists
            if (mLogFile != null && mLogFile.exists() && deleteOldFile) {
                mLogFile.delete();
            }

            // create log file if not exists
            mLogFile = mFileManager.createNewFile(isExternalStorageAvailable, mLogFileName);

            try {
                mLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                mLogFileOutputStream = new FileOutputStream(mLogFile);
                mOutputStreamWriter = new OutputStreamWriter(mLogFileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // sdCard not available
            // TODO: add possibility to write in internal storage
        }
    }
}
