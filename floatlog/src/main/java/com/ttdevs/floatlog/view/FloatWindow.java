package com.ttdevs.floatlog.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

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

    private View viewHeader;
    private ToggleButton tbMode;
    private AppCompatTextView viewKeyword;
    private Spinner spLevel;
    private View viewClose;
    private AppCompatTextView tvLog;

    public FloatWindow(Context context, int y) {
        super(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mY = y;

        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.window_float, this);

        viewHeader = findViewById(R.id.view_header);
        tbMode = findViewById(R.id.tb_mode);
        viewKeyword = findViewById(R.id.view_keyword);
        spLevel = findViewById(R.id.spLevel);
        viewClose = findViewById(R.id.view_close);
        tvLog = findViewById(R.id.tv_log);
        tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());

        tbMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spLevel.setVisibility(isChecked ? View.GONE : View.VISIBLE);

                if (isChecked) {
                    showCLog();
                } else {
                    showLogcat();
                }
            }
        });
        viewClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.logcat_level,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLevel.setAdapter(adapter);
        spLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLogLevel = String.valueOf(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHeader.setOnTouchListener(new View.OnTouchListener() {
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
    }

    public void dismiss() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            tvLog.append(msg.obj.toString());
        }
    };

    private LogcatUtil mLogcat;

    private void showLogcat() {
        String keyword = viewKeyword.getText().toString();
        if (null != mLogcat) {
            mLogcat.quit();
            mLogcat = null;
        }
        mLogcat = new LogcatUtil(mHandler, keyword, mLogLevel);
        mLogcat.start();
    }

    private void showCLog() {
        if(null != mLogcat){
            mLogcat.quit();
        }

    }
}
