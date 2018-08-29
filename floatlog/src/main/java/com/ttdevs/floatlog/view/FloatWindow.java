package com.ttdevs.floatlog.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ttdevs.floatlog.R;
import com.ttdevs.floatlog.utils.LogcatUtil;

/**
 * @author ttdevs
 * 2018-08-28 17:01
 */
public class FloatWindow extends LinearLayout {
    public static final int MARGIN_LEFT = 10;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private int mY, mTouchSlop;
    private String mLogLevel = "Verbose";

    private View viewMove;
    private AppCompatTextView viewKeyword;
    private Spinner spLevel;
    private View viewClose;
    private AppCompatTextView tvLog;

    private LogcatUtil mLogcat;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            // TODO: 2018/8/29  内存控制
            tvLog.append(msg.obj.toString());
        }
    };

    public FloatWindow(Context context, int y) {
        super(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mY = y;

        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.layout_window_float, this);

        viewMove = findViewById(R.id.view_move);
        viewKeyword = findViewById(R.id.view_keyword);
        spLevel = findViewById(R.id.spLevel);
        viewClose = findViewById(R.id.view_close);
        tvLog = findViewById(R.id.tv_log);
        tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());

        viewClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.logcat_level,
                R.layout.item_logcat_level);
        adapter.setDropDownViewResource(R.layout.item_logcat_level_item);
        spLevel.setAdapter(adapter);
        spLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence level = adapter.getItem(position);
                mLogLevel = String.valueOf(level.charAt(0));
                showLogcat();

                Log.v("v>>>>>", mLogLevel);
                Log.d("d>>>>>", mLogLevel);
                Log.i("i>>>>>", mLogLevel);
                Log.w("w>>>>>", mLogLevel);
                Log.e("e>>>>>", mLogLevel);
                Log.wtf("wtf>>>>>", mLogLevel);

                System.out.println(mLogLevel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewMove.setOnTouchListener(new View.OnTouchListener() {
            private float downX, downY;
            private float lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        lastY = downY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.y += event.getRawY() - lastY;
                        mLayoutParams.y = Math.max(0, mLayoutParams.y);
                        mWindowManager.updateViewLayout(FloatWindow.this, mLayoutParams);
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(event.getRawX() - downX) < mTouchSlop && Math.abs(event.getRawY() - downY) < mTouchSlop) {
                            // TODO: 2018/8/29
                        }
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void show() {
        try {
            mWindowManager.addView(this, getWindowLayoutParams());
        } catch (Exception e) {
            e.printStackTrace();
        }

        showLogcat();
    }

    public void dismiss() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeLogcat();
    }

    private WindowManager.LayoutParams getWindowLayoutParams() {
        mLayoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mLayoutParams.x = MARGIN_LEFT;
        mLayoutParams.y = mY;
        return mLayoutParams;
    }


    private void showLogcat() {
        closeLogcat();

        String keyword = viewKeyword.getText().toString();
        mLogcat = new LogcatUtil(mHandler, keyword, mLogLevel);
        mLogcat.start();
    }

    private void closeLogcat() {
        if (null != mLogcat) {
            mLogcat.quit();
            mLogcat = null;
        }
    }
}
