package com.example.eeintership.timetracker;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Data.UserData;
import Data.UploadSpreadsheetData;

public class MainActivity extends AppCompatActivity {

    private ApplicationTimeTracker applicationTimeTracker;

    private Button btnRemote;


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







        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        final UserData userData = new UserData();
        final UploadSpreadsheetData uploadSpreadsheetData = new UploadSpreadsheetData();

        final Intent intentFinishActivity = new Intent(this,FinishActivity.class);

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        ly_open_close = (LinearLayout) this.findViewById(R.id.background_shadow_open);
        final Intent intentOpenFinishActivity = new Intent(this,FinishActivity.class);
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

                    //INPUT START TIME
                    Calendar calender = Calendar.getInstance();
                    int cDay = calender.get(Calendar.DAY_OF_MONTH);
                    int cMonth = calender.get(Calendar.MONTH) + 1;
                    int cYear = calender.get(Calendar.YEAR);
                    int cHour = calender.get(Calendar.HOUR);
                    int cHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
                    int cMinute = calender.get(Calendar.MINUTE);
                    int cSecond = calender.get(Calendar.SECOND);

                    Time time = new Time(cHourOfDay,cMinute,00);
                    uploadSpreadsheetData.startingTime = time;
                    uploadSpreadsheetData.date = calender;

                    // orange edge shows above the button
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    DisplayMetrics displayMetrics=getApplication().getResources()
                            .getDisplayMetrics();

                    int x=Math.round(2*(displayMetrics.xdpi)/displayMetrics.DENSITY_DEFAULT);
                    params.setMargins(0,x,0,0);
                    btn_open_close.setLayoutParams(params);

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


                    // green edge shows above the button
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    DisplayMetrics displayMetrics=getApplication().getResources()
                            .getDisplayMetrics();

                    int x=Math.round(2*(displayMetrics.xdpi)/displayMetrics.DENSITY_DEFAULT);
                    params.setMargins(0,0,0,x);
                    btn_open_close.setLayoutParams(params);
                    //INPUT FINISH TIME
                    Calendar calender = Calendar.getInstance();
                    int cHour = calender.get(Calendar.HOUR);
                    int cHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
                    int cMinute = calender.get(Calendar.MINUTE);
                    int cSecond = calender.get(Calendar.SECOND);
                    uploadSpreadsheetData.finishTime = new Time(cHourOfDay,cMinute,00);
                    userData.addUploadRepository(uploadSpreadsheetData);

                    applicationTimeTracker.setUserData(userData);
                    startActivity(intentFinishActivity);                   

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

                    // orange edge shows above the button
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    DisplayMetrics displayMetrics=getApplication().getResources()
                            .getDisplayMetrics();

                    int x=Math.round(2*(displayMetrics.xdpi)/displayMetrics.DENSITY_DEFAULT);
                    params.setMargins(0,x,0,0);
                    btn_break_back.setLayoutParams(params);

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

                    // green edge shows above the button
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    DisplayMetrics displayMetrics=getApplication().getResources()
                            .getDisplayMetrics();

                    int x=Math.round(2*(displayMetrics.xdpi)/displayMetrics.DENSITY_DEFAULT);
                    params.setMargins(0,0,0,x);
                    btn_break_back.setLayoutParams(params);

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




        final Intent intentRemote =  new Intent(this, FinishRemoteActivity.class);
        btnRemote = (Button) findViewById(R.id.button_remote);
        btnRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentRemote);
            }
        });
    }
}