package com.ttdevs.flyer.utils;

import android.content.Context;

import java.lang.reflect.Method;

/**
 * @author ttdevs
 * 2018-08-28 16:52
 */
public class Application {
    private static Context CONTEXT;

    private Application() {
    }

    public static Context getApplicationContext() {
        if (CONTEXT != null) {
            return CONTEXT;
        } else {
            try {
                Class activityThreadClass = Class.forName("android.app.ActivityThread");
                Method method = activityThreadClass.getMethod("currentApplication");
                CONTEXT = (Context) method.invoke(null);
                return CONTEXT;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
