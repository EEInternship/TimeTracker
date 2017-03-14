package com.example.eeintership.timetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MonthlyOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyoverview);

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
