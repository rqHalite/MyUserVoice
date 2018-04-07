package com.rock.myuservoice;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Rock on 2018/4/4.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
