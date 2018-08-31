package com.ttdevs.flyer.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;

import com.ttdevs.flyer.R;

/**
 * @author ttdevs
 */
public class Constant {
    public static char VERBOSE = 'V';
    public static char DEBUG = 'D';
    public static char INFO = 'I';
    public static char WARN = 'W';
    public static char ERROR = 'E';
    public static char ASSERT = 'A';

    public static SparseArray<ForegroundColorSpan> LOG_COLOR_SPAN = new SparseArray() {
        {
            Context context = Application.getApplicationContext();
            put(VERBOSE, new ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_level_verbose)));
            put(DEBUG, new ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_level_debug)));
            put(INFO, new ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_level_info)));
            put(WARN, new ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_level_warn)));
            put(ERROR, new ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_level_error)));
            put(ASSERT, new ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_level_assert)));
        }
    };

    public static final int KEY_LOG_MESSAGE = 0x01;


    /**
     * 08-31 18:26:49.429  2391  2391 I log content
     * I
     */
    public static final int INDEX_LOG_LEVEL = 4;
    /**
     * 08-31 18:26:49.429  2391  2391 I log content
     * log content
     */
    public static final int INDEX_LOG_CONTENT = 5;
}
