package com.example.eeintership.timetracker;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_open_close;
    int open = 0;

    private Button btn_break_back;
    int breakBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        btn_open_close = (Button) this.findViewById(R.id.button_open);
        btn_open_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (open == 0) {
                    Drawable myD=null;
                    Resources res=getResources();
                    try{
                        myD=Drawable.createFromXml(res,res.getXml(R.xml.btn_design_orange));
                    }catch (Exception e){
                        // if something goes wrong.
                    }

                    btn_open_close.setBackground(myD);
                    open = 1;
                    btn_open_close.setText("Close");
                }else {
                    Drawable myD=null;
                    Resources res=getResources();
                    try{
                        myD=Drawable.createFromXml(res,res.getXml(R.xml.btn_design_green));
                    }catch (Exception e){
                        // if something goes wrong.
                    }

                    btn_open_close.setBackground(myD);
                    open = 0;
                    btn_open_close.setText("Open");
                }
            }
        });

        btn_break_back = (Button) this.findViewById(R.id.button_break);
        btn_break_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (breakBack == 0) {
                    Drawable myD=null;
                    Resources res=getResources();
                    try{
                        myD=Drawable.createFromXml(res,res.getXml(R.xml.btn_design_orange));
                    }catch (Exception e){
                        // if something goes wrong.
                    }

                    btn_break_back.setBackground(myD);
                    breakBack = 1;
                    btn_break_back.setText("Back");
                } else {
                    Drawable myD=null;
                    Resources res=getResources();
                    try{
                        myD=Drawable.createFromXml(res,res.getXml(R.xml.btn_design_green));
                    }catch (Exception e){
                        // if something goes wrong.
                    }

                    btn_break_back.setBackground(myD);
                    breakBack = 0;
                    btn_break_back.setText("Break");
                }
            }
        });
    }
}
