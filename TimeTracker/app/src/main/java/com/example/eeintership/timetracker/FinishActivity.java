package com.example.eeintership.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Time;
import java.text.ParseException;

import Data.DataAll;
import Data.UploadRepository;

public class FinishActivity extends AppCompatActivity {
    private ApplicationTimeTracker applicationTimeTracker;
    private Button buttonFinish;
    private EditText editTextDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        final DataAll dataAll= applicationTimeTracker.getDataAll();
        final UploadRepository uploadRepository = dataAll.getUploadRepository();
        try {
            uploadRepository.setWorkingTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Intent intentMainActivity = new Intent(this,MainActivity.class);
        editTextDescription = (EditText) findViewById(R.id.editTextFinish);
        buttonFinish = (Button)findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                uploadRepository.description = editTextDescription.getText().toString();
                dataAll.addUploadRepository(uploadRepository);
                applicationTimeTracker.setDataAll(dataAll);
                startActivity(intentMainActivity);
            }
        });

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
