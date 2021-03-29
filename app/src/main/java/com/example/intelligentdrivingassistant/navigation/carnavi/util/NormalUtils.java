/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.intelligentdrivingassistant.navigation.carnavi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.intelligentdrivingassistant.navigation.carnavi.CarNaviActivity;
import com.example.intelligentdrivingassistant.navigation.carnavi.DemoDrivingActivity;
import com.example.intelligentdrivingassistant.navigation.carnavi.DemoNaviActivity;
import com.example.intelligentdrivingassistant.navigation.carnavi.DemoNaviSettingActivity;

public class NormalUtils {

    public static void gotoNavi(Activity activity) {
        Intent it = new Intent(activity, CarNaviActivity.class);
        activity.startActivity(it);
    }

    public static void gotoSettings(Activity activity) {
        Intent it = new Intent(activity, DemoNaviSettingActivity.class);
        activity.startActivity(it);
    }

    public static void gotoDriving(Activity activity) {
        Intent it = new Intent(activity, DemoDrivingActivity.class);
        activity.startActivity(it);
    }

    public static String getTTSAppID() {
        return "23852884";
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
