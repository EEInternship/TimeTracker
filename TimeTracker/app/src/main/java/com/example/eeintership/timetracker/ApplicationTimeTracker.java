package com.example.eeintership.timetracker;

import android.app.Application;
import Data.DataAll;
/**
 * Created by Klemen on 15. 03. 2017.
 */

public class ApplicationTimeTracker extends Application{
    private DataAll dataAll;

    @Override
    public void onCreate() {
        super.onCreate();
        if (dataAll == null){
            dataAll = new DataAll();
        }
    }



    public DataAll getDataAll(){
        return dataAll;
    }

    public void setDataAll(DataAll data) {
        this.dataAll = data;
    }
}
