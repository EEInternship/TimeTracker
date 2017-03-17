package com.example.eeintership.timetracker;


import android.content.Intent;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Time;
import java.text.ParseException;

import Data.UserData;
import Data.UploadSpreadsheetData;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.client.util.Strings;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import pub.devrel.easypermissions.EasyPermissions;

public class FinishActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 2;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 3;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 4;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };
    private GoogleAccountCredential mCredential;
    private Random random;

    private ApplicationTimeTracker applicationTimeTracker;
    private Button buttonFinish;
    private EditText editTextDescription;
    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        userData = applicationTimeTracker.getUserData();
        final UploadSpreadsheetData uploadSpreadsheetData = userData.getUploadSpreadsheetData();
        try {
            uploadSpreadsheetData.setWorkingTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editTextDescription = (EditText) findViewById(R.id.editTextFinish);
        buttonFinish = (Button) findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSpreadsheetData.description = editTextDescription.getText().toString();
                userData.addUploadRepository(uploadSpreadsheetData);
                applicationTimeTracker.setUserData(userData);
                writeResultsToApi();
            }
        });

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        random = new Random();
        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

    }

    private void writeResultsToApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            //mTextView.setText("No network connection available!");
            //mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            new MakeRequestWrite(mCredential).execute();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (googleApiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServiceAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServiceAvailabilityErrorDialog(int connectionStatusCode) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = googleApiAvailability.getErrorDialog(
                FinishActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES
        );
        dialog.show();
    }

    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)) {
            startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        } else {
            EasyPermissions.requestPermissions(this, "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    //mTextView.setText("This app requires Google Play Services. Please install Google Play Servies and re-launch this app!");
                } else {
                    writeResultsToApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        writeResultsToApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    writeResultsToApi();
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private class MakeRequestWrite extends AsyncTask<Void, Void, Void> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestWrite(GoogleAccountCredential credential) {
            HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets
                    .Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("Google Sheets Reading")
                    .build();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                writeDataToSheet();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }

            return null;
        }

        private void writeDataToSheet() throws IOException {
            String spreadsheetId = "1IeH8kq3znoWEA7-BG8iGBC3IQqUzfnxE_dsGliy1hyo";
           // String range = "Januar!B14:H14";

            UploadSpreadsheetData uploadSpreadsheetData = userData.getUploadSpreadsheetData();

            String range = namesOfMonth(uploadSpreadsheetData.date.get(Calendar.MONTH)+1)+"!B"+String.valueOf(uploadSpreadsheetData.date.get(Calendar.DAY_OF_MONTH)+2)+":H"+
                    String.valueOf(uploadSpreadsheetData.date.get(Calendar.DAY_OF_MONTH)+2);

            //for the values that you want to input, create a list of object lists
            List<List<Object>> values = new ArrayList<>();

            //Where each value represents the list of objects that is to be written to a range
            //I simply want to edit a single row, so I use a single list of objects
            List<Object> spreadsheetData = new ArrayList<>();
            //Generate random int number for testing purposes
            spreadsheetData.add(cutTime(uploadSpreadsheetData.startingTime));
            spreadsheetData.add( "/");
            spreadsheetData.add( "/");
            spreadsheetData.add(cutTime(uploadSpreadsheetData.finishTime));
            spreadsheetData.add(cutTime(uploadSpreadsheetData.workingTime));
            spreadsheetData.add(cutTime(uploadSpreadsheetData.overHoursTime));
            spreadsheetData.add(uploadSpreadsheetData.description);

            values.add(spreadsheetData);

            //Create the valuerange object and set its fields
            ValueRange valueRange = new ValueRange();
            valueRange.setMajorDimension("ROWS");
            valueRange.setRange(range);
            valueRange.setValues(values);

            //then gloriously execute this copy-pasted code ;)
            this.mService.spreadsheets().values()
                    .update(spreadsheetId, range, valueRange)
                    .setValueInputOption("RAW")
                    .execute();

            //Try calling this method before executing the readDataFromApi method,
            //and you'll see the immediate change
        }

        @Override
        protected void onPreExecute() {
            //mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //mProgressBar.setVisibility(View.INVISIBLE);
            //After the WriteRequest has been posted close this activity and return to the main activity.
            finish();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            //mProgressBar.setVisibility(View.INVISIBLE);
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServiceAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            FinishActivity.REQUEST_AUTHORIZATION);
                } else {
                    /*mTextView.setText("The following error occurred:\n"
                            + mLastError.getMessage());*/
                }
            } else {
                //mTextView.setText("Request cancelled.");
            }
        }
    }


    private String namesOfMonth(int monthNumber){
        String month = "";
        switch (monthNumber){
            case 1:
                month = "Januar";
                break;
            case 2:
                month = "Februar";
                break;
            case 3:
                month = "Marec";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "Maj";
                break;
            case 6:
                month = "Junij";
                break;
            case 7:
                month = "Julij";
                break;
            case 8:
                month = "Avgust";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "Oktober";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
        }
        return month;
    }

    private String cutTime(Time time){
        String hours, minutes;

        if(time.getHours()<10)
            hours = "0"+String.valueOf(time.getHours());
        else
            hours = String.valueOf(time.getHours());
        if(time.getMinutes()<10)
            minutes = "0"+String.valueOf(time.getMinutes());
        else
            minutes = String.valueOf(time.getMinutes());

        return hours + ":" + minutes;
    }
}
