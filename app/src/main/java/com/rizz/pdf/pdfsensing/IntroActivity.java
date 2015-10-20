package com.rizz.pdf.pdfsensing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * An introduction to the app with a short overview
 * and checklist of non verifiable statuses
 */
public class IntroActivity extends Activity {
    private LinearLayout checkBoxList;
    private ArrayList<CheckBox> checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        //prepareStartButton();
    }
    /*
    private void prepareStartButton() {
        checkBoxList = (LinearLayout) findViewById(R.id.checkBoxLayout);
        checkBoxes = new ArrayList<>();
        for (int i = 0; i < checkBoxList.getChildCount(); i++)
            checkBoxes.add((CheckBox) checkBoxList.getChildAt(i));
    }*/
    public void startButtonAction(View view) {
        Intent intent = new Intent(this, FullscreenActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
