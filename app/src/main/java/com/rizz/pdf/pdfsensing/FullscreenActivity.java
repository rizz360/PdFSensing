package com.rizz.pdf.pdfsensing;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.rizz.pdf.pdfsensing.Handlers.AudioHandler;
import com.rizz.pdf.pdfsensing.Handlers.FileHandler;
import com.rizz.pdf.pdfsensing.Handlers.MetaDataHandler;

/**
 * Created by Rizz.
 * Handles initialization of handlers,
 * propagates method calls issued by buttons
 * and sets general layout
 */
public class FullscreenActivity extends Activity {
    private static final String LOG_TAG = "FullScreenActivity";
    private ArcRecordTimer arcRecTimer;
    private RelativeLayout helpOverlay;
    private boolean helpVisibility = false;

    /*TODO
    * fix play button state
    * make intro screen nicer to look at
    * add splash screen before intro screen?
     */
    public FullscreenActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        super.setContentView(R.layout.activity_fullscreen);

        Log.i(LOG_TAG, "Initializing services");
        FileHandler.init();
        AudioHandler.init(this);
        MetaDataHandler.init();
        Log.i(LOG_TAG, "services initialized");

        arcRecTimer = (ArcRecordTimer)(this.findViewById(R.id.arcRecTimer));
        helpOverlay = (RelativeLayout)(this.findViewById(R.id.help_overlay));
    }

    //Propagate button calls towards methods
    public void recordAction(View view) {
        arcRecTimer.startCountdown();
    }

    public void helpAction(View view) {
        helpVisibility = !helpVisibility;
        if(helpVisibility)
            helpOverlay.setVisibility(RelativeLayout.VISIBLE);
        else helpOverlay.setVisibility(RelativeLayout.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioHandler.onPause();
    }
}
