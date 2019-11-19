package com.company;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.io.FilenameUtils.concat;
import static org.apache.commons.io.FilenameUtils.getName;

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

    DownLoadURL(String url, String localPlace) throws MalformedURLException {
        try {
            this.FILE_URL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.localPlace = localPlace;
        this.FILE_NAME = new File(concat(localPlace,
                                    getName(FILE_URL.getPath())));

    }
    public void run()  {
        // It's first try without resume downloading, analytics for exceptions, threads
        System.out.println("FILE_URL:"+FILE_URL);
        System.out.println("FILE_NAME:"+FILE_NAME);
        try {
            if (!FILE_NAME.exists()) { // checking for existing the file before start the download
                FileUtils.copyURLToFile(
                        FILE_URL,
                        FILE_NAME);
                //     , CONNECT_TIMEOUT, READ_TIMEOUT);
            }else{
                System.out.println("File "+FILE_NAME+" just exist!");
            }
        } catch (Exception e){
            System.out.println("1 File "+FILE_NAME+" doesn't download :"+e);
            System.err.println("2 " +e);
            System.out.println("3 "+e.toString());
            System.out.println("4 ");
            e.printStackTrace();
        }
    }
}
