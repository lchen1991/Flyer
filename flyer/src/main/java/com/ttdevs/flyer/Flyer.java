package com.ttdevs.flyer;

import com.ttdevs.flyer.utils.Application;
import com.ttdevs.flyer.utils.Constant;
import com.ttdevs.flyer.utils.SystemUtils;
import com.ttdevs.flyer.view.FlyerWindow;

/**
 * @author ttdevs
 * 2018-08-28 16:49
 */
public class Flyer {
    private static FlyerWindow mFloatWindow;

    private Flyer() {

    }

    public synchronized static void show() {
        if (!SystemUtils.requestPermission()) {
            return;
        }
        if (null == mFloatWindow) {
            mFloatWindow = new FlyerWindow(Application.getApplicationContext(),
                    Constant.FLYER_MARGIN_TOP);
        }
        mFloatWindow.show();
    }

    public static void dismiss() {
        if (null == mFloatWindow) {
            return;
        }
        mFloatWindow.dismiss();
    }
}
