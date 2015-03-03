package com.example.sdi.mylibrary;

import android.os.Environment;

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
    private final static String mLogDirName = "Log";

    private static volatile PhoneLog mLog = null;
    private static String mLogFileName = "Log.log";

    private static File mLogFile = null;
    private static FileOutputStream mLogFileOutputStream = null;
    private static OutputStreamWriter mOutputStreamWriter = null;

    private static String mPrefix = "";

    private final static SimpleDateFormat mPrefixDateFormat = new SimpleDateFormat("hh:mm:ss(SSS) :\t");

    private PhoneLog(){
        // private constructorll;
        createFile(isExternalStorageWritable(), true);
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
     * @param loggedMessage : message that will be added to log file
     * @return: status of write operation.
     *          must be OK.
    * */
    public String append(String loggedMessage){
        try {
            String formatedDate = mPrefixDateFormat.format(Calendar.getInstance().getTime());
            mOutputStreamWriter.append(formatedDate + loggedMessage + "\n");
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
    private void createFile(boolean isExternalStorageAvailable, boolean deleteOldFile){
        if (isExternalStorageAvailable) {
            // write ot external storage
            if (mLogFile == null) {
                // create log file if not exists
                File dir = new File(
                        Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator +
                        mLogDirName
                );

                // create dir
                dir.mkdirs();

                // create file in dir
                mLogFile = new File(dir, mLogFileName);
                if (mLogFile.exists() && deleteOldFile){
                    mLogFile.delete();
                    try {
                        mLogFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

    /* Check if external storage is available for read and write*/
    private boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
