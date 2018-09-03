package com.ttdevs.flyer.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author ttdevs
 */
public class SystemUtils {

    public static boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(Application.getApplicationContext())) {
                SystemUtils.requestGrantOverlay(Application.getApplicationContext());
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestGrantOverlay(Context context) {
        // I/ActivityManager: START u0 {act=android.settings.action.MANAGE_OVERLAY_PERMISSION dat=package:com.ttdevs.demo flg=0x10008000 cmp=com.android.settings/.Settings$AppDrawOverlaySettingsActivity} from uid 1000 pid -1
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * From UETool
     *
     * @return
     */
    public static String getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);
            Field mActivitiesField = activityThreadClass.getDeclaredField("mActivities");
            mActivitiesField.setAccessible(true);
            Map activities = (Map) mActivitiesField.get(currentActivityThread);
            for (Object record : activities.values()) {
                Class recordClass = record.getClass();
                Field pausedField = recordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!(boolean) pausedField.get(record)) {
                    Field activityField = recordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return activityField.get(record).getClass().getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
