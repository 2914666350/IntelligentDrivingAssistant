/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.example.intelligentdrivingassistant.navigation;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;

public class ReflectHelper {
    private static final String TAG = "ReflectHelper";
    private static final int UNKNOWN = -9999;
    private static final int ERROR = -20;
    private static Object sVmRuntime;
    private static Method setHiddenApiExemptions;
    private static int unsealed = UNKNOWN;

    static {
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

            Log.e(TAG, "getDeclaredMethod:" + getDeclaredMethod);

            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions",
                    new Class[]{String[].class});
            sVmRuntime = getRuntime.invoke(null);
        } catch (Throwable e) {
            Log.e(TAG, "reflect bootstrap failed:", e);
        }
    }

    public static void unseal(Context context) {
        if (SDK_INT < 29) {
            // Below Android P, ignore
            return;
        }

        // try exempt API first.
        if (exemptAll()) {
            return;
        }

        if (context == null) {
            return;
        }

        ApplicationInfo applicationInfo = context.getApplicationInfo();

        synchronized (com.example.intelligentdrivingassistant.navigation.ReflectHelper.class) {
            if (unsealed != UNKNOWN) {
                return;
            }

            unsealed = 0;

            return;

            // Android P, we need to sync the flags with ApplicationInfo
            // We needn't to this on Android Q.
        }
    }

    /**
     * make the method exempted from hidden API check.
     *
     * @param method the method signature prefix.
     * @return true if success.
     */
    public static boolean exempt(String method) {
        return exempt(new String[]{method});
    }

    /**
     * make specific methods exempted from hidden API check.
     *
     * @param methods the method signature prefix, such as "Ldalvik/system", "Landroid" or even "L"
     * @return true if success
     */
    private static boolean exempt(String... methods) {
        if (sVmRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }

        try {
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{methods});
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Make all hidden API exempted.
     *
     * @return true if success.
     */
    private static boolean exemptAll() {
        return exempt(new String[]{"L"});
    }
}