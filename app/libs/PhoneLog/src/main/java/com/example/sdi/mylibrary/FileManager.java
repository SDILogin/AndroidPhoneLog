package com.example.sdi.mylibrary;

import android.os.Environment;

import java.io.File;

/**
 * Created by SDI on 04.03.15.
 */
public class FileManager {
    private String mDirName = "Log";

    public FileManager(){

    }

    public FileManager(String dirName){
        mDirName = dirName;
    }

    /**
     *      @param isExternalStorageUsed: use sdCard if TRUE
     *      @return folder file. Will be created if not exists
     * */
    public File getFolder(boolean isExternalStorageUsed){
        StringBuilder sb = new StringBuilder();
        File dir = null;
        if (isExternalStorageUsed){
            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .append(File.separator)
                    .append(mDirName);
            dir = new File(sb.toString());

            // create Log directory if not exists
            dir.mkdirs();
        }

        return dir;
    }

    /**
     *      this method returns log file with name <<localName>>
     *      @param isExternalStorageUsed: use sdCard if TRUE
     *      @param localName: name of new log file
     *      @return new log file
     * */
    public File createNewFile(boolean isExternalStorageUsed, String localName){
        StringBuilder sb = new StringBuilder();
        File newLogFile = null; // todo: replace with some default value
        if (isExternalStorageUsed){
            sb.append(getFolder(isExternalStorageUsed).getAbsolutePath())
                    .append(File.separator).append(localName);
            newLogFile = new File(sb.toString());
        }

        return newLogFile;
    }

    /**
     *  all log files will be deleted
     * */
    public void clearFolder(){
        File folder = getFolder(isExternalStorageWritable());
        File[] files = folder.listFiles();
        for (File f : files){
            f.delete();
        }
    }

    /* Check if external storage is available for read and write*/
    public static boolean isExternalStorageWritable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
