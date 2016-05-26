package com.davunited.classes;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ela on 23-05-2016.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
    public static MyApplication getInstance() {
        return myApplication;
    }

    public static Context getAppContext(){
        return myApplication.getApplicationContext();
    }
}
