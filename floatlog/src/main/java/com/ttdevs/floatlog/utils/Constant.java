package com.ttdevs.floatlog.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;

import com.ttdevs.floatlog.R;

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
}
