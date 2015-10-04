package com.rizz.pdf.pdfsensing;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.rizz.pdf.pdfsensing.Handlers.AudioHandler;
import com.rizz.pdf.pdfsensing.Handlers.FileHandler;
import com.rizz.pdf.pdfsensing.Handlers.LocationHandler;
import com.rizz.pdf.pdfsensing.Handlers.MetaDataHandler;
import com.rizz.pdf.pdfsensing.Handlers.StatusHandler;

/**
 * Created by Rizz.
 * Handles initialization of handlers,
 * propagates method calls issued by buttons
 * and sets general layout
 */
public class FullscreenActivity extends Activity {
    private static final String LOG_TAG = "FullScreenActivity";
    private ArcRecordTimer arcRecTimer;
    private ArcPlayTimer arcPlayTimer;
    private RelativeLayout helpOverlay;
    private boolean helpVisibility = false;

    /*TODO
    * fix play button state
    * make record button only usable when conditions are met
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
        LocationHandler.init(getBaseContext());
        MetaDataHandler.init();
        StatusHandler.init(this);
        Log.i(LOG_TAG, "services initialized");

        arcRecTimer = (ArcRecordTimer)(this.findViewById(R.id.arcRecTimer));
        arcPlayTimer = (ArcPlayTimer)(this.findViewById(R.id.arcPlayTimer));
        helpOverlay = (RelativeLayout)(this.findViewById(R.id.help_overlay));
    }

    //Propagate button calls towards methods
    public void recordAction(View view) {
        if (StatusHandler.checkReadyState())
            arcRecTimer.startCountdown();
    }

    public void playAction(View view) {arcPlayTimer.startCountdown();}

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
