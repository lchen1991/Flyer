package com.ttdevs.floatlog.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ttdevs.floatlog.R;
import com.ttdevs.floatlog.adapter.LogAdapter;
import com.ttdevs.floatlog.utils.Constant;
import com.ttdevs.floatlog.utils.LogcatUtil;

import java.util.ArrayList;

/**
 * @author ttdevs
 * 2018-08-28 17:01
 */
public class FloatWindow extends LinearLayout {
    public static final int MARGIN_LEFT = 10;

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private int mY;
    private String mLogLevel = "V";

    private View viewMove;
    private TextView viewKeyword;
    private Spinner spLevel;
    private View viewClean;
    private View viewClose;
    private RecyclerView rvLog;

    private ArrayList<String> mDataList = new ArrayList<>();
    private LogAdapter mAdapter;

    private LogcatUtil mLogcat;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case Constant.KEY_LOG_MESSAGE:
                    updateLog(msg.obj.toString());
                    break;

                default:
                    break;
            }
        }
    };

    public FloatWindow(Context context, int y) {
        super(context);

        mContext = context;

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mY = y;

        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.layout_window_float, this);

        viewMove = findViewById(R.id.view_move);
        spLevel = findViewById(R.id.spLevel);
        viewKeyword = findViewById(R.id.view_keyword);
        viewClean = findViewById(R.id.view_clean);
        viewClose = findViewById(R.id.view_close);
        rvLog = findViewById(R.id.rv_log);
        rvLog.setLayoutManager(new LinearLayoutManager(mContext));
        rvLog.setAdapter(mAdapter = new LogAdapter(mContext, mDataList));

        viewClean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        viewClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        initSpinner();
        initMoveWindow();
    }

    private void initSpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initMoveWindow() {
        viewMove.setOnTouchListener(new View.OnTouchListener() {
            private float downY, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mLayoutParams.x = MARGIN_LEFT;
        mLayoutParams.y = mY;
        return mLayoutParams;
    }


    private void showLogcat() {
        closeLogcat();

        String keyword = viewKeyword.getText().toString();
        String cmd = String.format("logcat %s:%s", keyword, mLogLevel);
        mLogcat = new LogcatUtil(mHandler, cmd);
        mLogcat.start();
    }

    private void closeLogcat() {
        if (null != mLogcat) {
            mLogcat.quit();
            mLogcat = null;
        }

        clearLog();
    }

    private void updateLog(String log) {
        mDataList.add(log);
        mAdapter.notifyDataSetChanged();

        if(rvLog.getL){

        }

        rvLog.scrollToPosition(mDataList.size() - 1);
    }

    private void clearLog() {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
    }
}
