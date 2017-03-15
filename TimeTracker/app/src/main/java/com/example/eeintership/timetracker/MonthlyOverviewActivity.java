package com.example.eeintership.timetracker;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MonthlyOverviewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 2;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 3;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 4;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };
    private Date currentDate;
    private String[] namesOfMonths;
    private int[] numberOfDaysInMonth;

    private TextView textViewMonthlyOverview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyoverview);

        // hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        currentDate= new Date();
        namesOfMonths=new String[]{"Januar","Februar","Marec","April"};
        numberOfDaysInMonth= new int[] {31,28,31,30};
        Log.i("MonthlyOverviewActivity",namesOfMonths[currentDate.getMonth()]);
        textViewMonthlyOverview=(TextView)findViewById(R.id.tvMonthlyOverview);
        mCredential=GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        getResultsFromApi();
    }
    private void getResultsFromApi()
    {
        if(!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        }
        else if(mCredential.getSelectedAccountName()==null){
            chooseAccount();
        } else if (!isDeviceOnline()) {
            textViewMonthlyOverview.setText("No network connection available!");
        } else {
            new MakeRequestRead(mCredential).execute();
        }
    }
    private boolean isGooglePlayServicesAvailable(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }
    private void acquireGooglePlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(googleApiAvailability.isUserResolvableError(connectionStatusCode)){
            showGooglePlayServiceAvailabilityErrorDialog(connectionStatusCode);
        }
    }
    void showGooglePlayServiceAvailabilityErrorDialog(int connectionStatusCode){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog =googleApiAvailability.getErrorDialog(
                MonthlyOverviewActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES
        );
        dialog.show();
    }
    private void chooseAccount(){
        if(EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)){
            startActivityForResult(mCredential.newChooseAccountIntent(),REQUEST_ACCOUNT_PICKER);
        }
        else
        {
            EasyPermissions.requestPermissions(this,"This app needs to access your Google account (via Contacts).",REQUEST_PERMISSION_GET_ACCOUNTS, android.Manifest.permission.GET_ACCOUNTS);
        }
    }
    private boolean isDeviceOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if(resultCode != RESULT_OK){
                    textViewMonthlyOverview.setText("This app requires Google Play Services. Please install Google Play Servies and re-launch this app!");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if(resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if(accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if(resultCode == RESULT_OK){
                    getResultsFromApi();
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

    private class MakeRequestRead extends AsyncTask<Void,Void,ArrayList<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestRead(GoogleAccountCredential credential) {
            HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets
                    .Builder(httpTransport,jsonFactory,credential)
                    .setApplicationName("")
                    .build();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try{
                return getDataFromSheet();
            }catch(Exception e){
                mLastError = e;
                cancel(true);
                return null;
            }
        }
        private ArrayList<String>getDataFromSheet() throws IOException {
            //Test spreadsheet
            String spreadsheetId="1IeH8kq3znoWEA7-BG8iGBC3IQqUzfnxE_dsGliy1hyo";
            String range = namesOfMonths[currentDate.getMonth()]+"!A3:I"+String.valueOf(numberOfDaysInMonth[currentDate.getMonth()]+2);
            ArrayList<String> results = new ArrayList<String>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values != null) {
                results.add("Datum");
                for (List row : values) {
                    String temp="";
                    for(int i=0;i<row.size();i++){
                        temp+=row.get(i)+" ";
                    }
                    results.add(temp);
                }
            }
            return results;
        }
        @Override
        protected void onPreExecute() {
            //
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if (strings == null || strings.size() == 0) {
                textViewMonthlyOverview.setText("No results returned.");
            } else {
                strings.add(0, "Data retrieved using the Google Sheets API:");
                textViewMonthlyOverview.setText(TextUtils.join("\n", strings));
            }
        }

        @Override
        protected void onCancelled(ArrayList<String> strings) {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServiceAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MonthlyOverviewActivity.REQUEST_AUTHORIZATION);
                } else {
                    textViewMonthlyOverview.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                textViewMonthlyOverview.setText("Request cancelled.");
            }
        }
    }
}
