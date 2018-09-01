package com.ttdevs.flyer.view;

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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ttdevs.flyer.R;
import com.ttdevs.flyer.adapter.LogAdapter;
import com.ttdevs.flyer.utils.Constant;
import com.ttdevs.flyer.utils.LogcatUtil;

import java.util.ArrayList;

import static com.ttdevs.flyer.utils.Constant.FLYER_MARGIN_LEFT;

/**
 * @author ttdevs
 * 2018-08-28 17:01
 */
public class FlyerWindow extends LinearLayout {
    private Context mContext;
    private int mY;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private String mLogLevel = "V";

    private View viewIcon;
    private TextView viewKeyword;
    private Spinner spLevel;
    private View viewClean;
    private CheckBox cbScroll;
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

    public FlyerWindow(Context context, int y) {
        super(context);

        mContext = context;
        mY = y;

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.layout_flyer_window, this);

        viewIcon = findViewById(R.id.view_icon);
        spLevel = findViewById(R.id.spLevel);
        viewKeyword = findViewById(R.id.view_keyword);
        viewClean = findViewById(R.id.view_clean);
        cbScroll = findViewById(R.id.cb_scroll);
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
                R.layout.view_logcat_level);
        adapter.setDropDownViewResource(R.layout.item_logcat_level);
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
        viewIcon.setOnTouchListener(new View.OnTouchListener() {
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
                        mWindowManager.updateViewLayout(FlyerWindow.this, mLayoutParams);
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
        mLayoutParams.height = (int) getResources().getDimension(R.dimen.window_height);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mLayoutParams.x = FLYER_MARGIN_LEFT;
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

    private void clearLog() {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void updateLog(String log) {
        mDataList.add(log);


        if (cbScroll.isChecked()) {
            rvLog.scrollToPosition(mDataList.size() - 1);
        }

        mAdapter.notifyDataSetChanged();
    }
}
