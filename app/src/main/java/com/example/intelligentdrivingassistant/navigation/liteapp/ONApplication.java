/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.intelligentdrivingassistant.navigation.liteapp;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class ONApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }
}
