package com.company;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.io.FilenameUtils.*;

/*
download given file by url and save it with same name into given localPlace
 */
public class DownLoadURL implements Runnable {
    // TimeOuts
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 500;

    private URL FILE_URL; // It's given URL to download
    private String localPlace; // It's local place on my HDD
    private File FILE_NAME; // It's new file with fool path on the HDD
    private File FILE_TEMP_NAME; // It's temporary file name, before file downloading will be completed

    DownLoadURL(String url, String localPlace) throws MalformedURLException {
        try {
            this.FILE_URL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.localPlace = localPlace;
        this.FILE_NAME = new File(concat(localPlace,
                                    getName(FILE_URL.getPath())));
        String tmpName = concat(localPlace, getBaseName(FILE_URL.getPath())) + ".tmp";
        System.out.println("Create: "+tmpName);
        this.FILE_TEMP_NAME = new File(tmpName);

    }
    public void run()  {
        // It's first try without resume downloading, analytics for exceptions, threads
        System.out.println("FILE_URL:"+FILE_URL);
        System.out.print(" --> FILE_NAME:"+FILE_NAME);
        try {
            if (!FILE_NAME.exists()) { // checking for existing the file before start the download
                // delete uncompleted download
                if (FILE_TEMP_NAME.exists()){
                    FILE_TEMP_NAME.delete();
                }
                FileUtils.copyURLToFile(
                        FILE_URL,
                        FILE_TEMP_NAME);
                //.tmp --> .mp3
                FileUtils.moveFile(FILE_TEMP_NAME, FILE_NAME);
                //     , CONNECT_TIMEOUT, READ_TIMEOUT);
            }else{
                System.out.println("File "+FILE_NAME+" just exist!");
            }
        } catch (Exception e){
            System.out.println("File "+FILE_NAME+" doesn't download :"+e);
        }
    }
}
