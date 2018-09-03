package com.ttdevs.flyer.utils;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ttdevs
 */
public class ActivityStack {

    public static void register(android.app.Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (mListener.size() > 0) {
                    for (OnTopActivityChangeListener listener : mListener) {
                        listener.onTopActivityChange(activity.getClass().getName());
                    }
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private static List<OnTopActivityChangeListener> mListener = new ArrayList<>();

    public static void addTopActivityChangeListener(OnTopActivityChangeListener listener) {
        if (null != listener) {
            mListener.add(listener);
        }
    }

    public interface OnTopActivityChangeListener {
        /**
         * 栈顶Activity改变
         *
         * @param activityName
         */
        void onTopActivityChange(String activityName);
    }
}
