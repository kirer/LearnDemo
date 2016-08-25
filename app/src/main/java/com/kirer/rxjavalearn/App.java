package com.kirer.rxjavalearn;

import android.app.Application;

import com.kirer.Kirer;

/**
 * Created by xinwb on 2016/8/24.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Kirer.init(this);
    }
}
