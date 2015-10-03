package com.rizz.pdf.pdfsensing.Handlers;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Rizz on 01/10/2015.
 */
public class HistoryHandler {
    private static String LOG_TAG = "HistoryHandler";
    private static ImageButton historyButton = null;
    private HistoryHandler(){}
    private static void init() {}
    public static void openHistory(View view) {
        historyButton = (ImageButton) view;
        Log.i(LOG_TAG, "Menu button pushed");
    }

    /**
     * TO DO
     */
}
