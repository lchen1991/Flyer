package com.ttdevs.flyer.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

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
}
