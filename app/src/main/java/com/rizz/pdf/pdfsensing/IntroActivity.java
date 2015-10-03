package com.rizz.pdf.pdfsensing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * An introduction to the app with a short overview
 * and checklist of non verifiable statuses
 */
public class IntroActivity extends Activity {
    private ArrayList<CheckBox> checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        checkBoxes = new ArrayList<>();
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox1));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox2));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox3));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox4));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox5));
        /*
        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
    */
    }

    public void startButtonAction(View view) {
        for (CheckBox check : checkBoxes)
            if (!check.isChecked()) return;
        //Start main activity
        Intent intent = new Intent(this, FullscreenActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
