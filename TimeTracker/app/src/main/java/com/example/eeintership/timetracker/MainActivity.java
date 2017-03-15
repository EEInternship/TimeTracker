package com.example.eeintership.timetracker;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ly_open_close;
    private Button btn_open_close;
    int open = 0;

    private LinearLayout ly_break_back;
    private Button btn_break_back;
    int breakBack = 0;

    private Button btn_monthly_overview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ly_open_close = (LinearLayout) this.findViewById(R.id.background_shadow_open);
        btn_open_close = (Button) this.findViewById(R.id.button_open);
        btn_open_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (open == 0) {
                    // for button change
                    Drawable myD = null;
                    Resources res = getResources();
                    try {
                        myD = Drawable.createFromXml(res, res.getXml(R.xml.btn_design_orange));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    btn_open_close.setBackground(myD);

                    // for shadow change
                    Drawable myDs = null;
                    Resources resS = getResources();
                    try {
                        myDs = Drawable.createFromXml(resS, resS.getXml(R.xml.btn_design_orange_shadow));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    ly_open_close.setBackground(myDs);

                    btn_open_close.setTextColor(Color.parseColor("#C0FFFFFF"));
                    open = 1;
                    btn_open_close.setText("Close");
                } else {
                    // for button change
                    Drawable myD = null;
                    Resources res = getResources();
                    try {
                        myD = Drawable.createFromXml(res, res.getXml(R.xml.btn_design_green));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    btn_open_close.setBackground(myD);

                    // for shadow change
                    Drawable myDs = null;
                    Resources resS = getResources();
                    try {
                        myDs = Drawable.createFromXml(resS, resS.getXml(R.xml.btn_design_green_shadow));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    ly_open_close.setBackground(myDs);

                    btn_open_close.setTextColor(Color.parseColor("#FFFFFF"));
                    open = 0;
                    btn_open_close.setText("Open");
                }
            }
        });

        ly_break_back = (LinearLayout) this.findViewById(R.id.background_shadow_break);
        btn_break_back = (Button) this.findViewById(R.id.button_break);
        btn_break_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (breakBack == 0) {
                    // for button change
                    Drawable myD = null;
                    Resources res = getResources();
                    try {
                        myD = Drawable.createFromXml(res, res.getXml(R.xml.btn_design_orange));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    btn_break_back.setBackground(myD);

                    // for shadow change
                    Drawable myDs = null;
                    Resources resS = getResources();
                    try {
                        myDs = Drawable.createFromXml(resS, resS.getXml(R.xml.btn_design_orange_shadow));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    ly_break_back.setBackground(myDs);

                    btn_break_back.setTextColor(Color.parseColor("#C0FFFFFF"));
                    breakBack = 1;
                    btn_break_back.setText("Back");
                } else {
                    // for button change
                    Drawable myD = null;
                    Resources res = getResources();
                    try {
                        myD = Drawable.createFromXml(res, res.getXml(R.xml.btn_design_green));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    btn_break_back.setBackground(myD);

                    // for shadow change
                    Drawable myDs = null;
                    Resources resS = getResources();
                    try {
                        myDs = Drawable.createFromXml(resS, resS.getXml(R.xml.btn_design_green_shadow));
                    } catch (Exception e) {
                        // if something goes wrong.
                    }
                    ly_break_back.setBackground(myDs);

                    btn_break_back.setTextColor(Color.parseColor("#FFFFFF"));
                    breakBack = 0;
                    btn_break_back.setText("Break");
                }
            }
        });

        final Intent intentOpenMonthlyOverview = new Intent(this,MonthlyOverviewActivity.class);
        btn_monthly_overview=(Button)findViewById(R.id.button_monthly_overview);
        btn_monthly_overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentOpenMonthlyOverview);
            }
        });
    }
}