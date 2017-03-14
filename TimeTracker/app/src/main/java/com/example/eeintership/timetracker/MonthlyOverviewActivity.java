package com.example.eeintership.timetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MonthlyOverviewActivity extends AppCompatActivity {
    private TextView textViewMonthlyOverview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyoverview);

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        textViewMonthlyOverview=(TextView)findViewById(R.id.tvMonthlyOverview);
    }
}
