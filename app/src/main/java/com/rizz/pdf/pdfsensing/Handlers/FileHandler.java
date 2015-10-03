package com.rizz.pdf.pdfsensing.Handlers;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Rizz on 01/10/2015.
 * Manages the folder structure, file naming and contents of the meta file
 */
public class FileHandler {
    private static final String LOG_TAG = "FileHandler";
    private static FileHandler fileHandler = null;
    private FileHandler() {
        File sdCard = Environment.getExternalStorageDirectory();
        defaultDirectory = new File(sdCard.getAbsolutePath() + "/pdfsensing");
        if(!defaultDirectory.mkdirs())
            Log.i(LOG_TAG, "Directory " + defaultDirectory+ " already existed.");
    }

    public static void init() {
        if(fileHandler == null) fileHandler = new FileHandler();
        updateFileNameTemplate();
    }

    private static String mFileNameTemplate = null;
    private static File defaultDirectory = null;

    public static void updateFileNameTemplate() {
        mFileNameTemplate = defaultDirectory.getAbsolutePath();
        MetaDataHandler.update();
        mFileNameTemplate += "/" + MetaDataHandler.getFormattedDate();
        Log.i(LOG_TAG, "Using template:" + mFileNameTemplate);
    }

    public static String getRecFileName() {
        return mFileNameTemplate + "-rec.3gpp";
    }

    public static String getMetaFileName() {
        return mFileNameTemplate + "-meta.txt";
    }

    public static void writeMetaFile() {
        LocationHandler.updateLocation();
        File metaFile = new File(getMetaFileName());
        FileWriter writer;
        try {
            writer = new FileWriter(metaFile, true);
            List<String> metaData = MetaDataHandler.getMetaData();
            for(String line : metaData)
                writer.write(line + "\n");

            writer.flush();
            writer.close();
            MetaDataHandler.deleteMetaData();
        }
        catch(IOException ex) {
            Log.e(LOG_TAG, "FileWriter failed - cannot write metadata");
            ex.printStackTrace();
        }
        readFile(metaFile);
    }

    private static void readFile(File f) {
        try{
            if (f.exists()) {
                BufferedReader in = new BufferedReader(new FileReader(f));
                while(in.ready())
                    Log.i(LOG_TAG,in.readLine());
                in.close();
            }
        }
        catch(FileNotFoundException e) {
            Log.e(LOG_TAG, "File was not found");
            e.printStackTrace();
        }
        catch(IOException io) {
            Log.e(LOG_TAG, "Could not read file");
            io.printStackTrace();
        }
    }
}
