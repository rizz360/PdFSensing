package com.rizz.pdf.pdfsensing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
    }

    public void skipButtonAction(View view) {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

}
