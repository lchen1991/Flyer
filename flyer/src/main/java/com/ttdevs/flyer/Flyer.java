package com.ttdevs.flyer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.ttdevs.flyer.utils.Application;
import com.ttdevs.flyer.view.FlyerWindow;

/**
 * @author ttdevs
 * 2018-08-28 16:49
 */
public class Flyer {
    public static final int MARGIN_TOP = 200 * 3;

    private FlyerWindow mFloatWindow;

    private static Flyer mInstance = new Flyer();

    private Flyer() {

    }

    public static void showWindow() {
        mInstance.show(MARGIN_TOP);
    }

    public static void dismissWindow() {
        mInstance.dismiss();
    }

    private boolean show(int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(Application.getApplicationContext())) {
                requestPermission(Application.getApplicationContext());
                Toast.makeText(Application.getApplicationContext(), "After grant this permission, re-enable UETool", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (mFloatWindow == null) {
            mFloatWindow = new FlyerWindow(Application.getApplicationContext(), y);
        }
        mFloatWindow.show();
        return true;
    }

    private boolean dismiss() {
        if (null == mFloatWindow) {
            return false;
        }
        mFloatWindow.dismiss();
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
