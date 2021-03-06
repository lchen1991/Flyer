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
import com.ttdevs.flyer.utils.ActivityStack;
import com.ttdevs.flyer.utils.Constant;
import com.ttdevs.flyer.utils.LogcatUtil;
import com.ttdevs.flyer.utils.SystemUtils;

import java.util.ArrayList;

import static com.ttdevs.flyer.utils.Constant.DELETE_LOG_SIZE;
import static com.ttdevs.flyer.utils.Constant.EVERY_PRINT_LOG;
import static com.ttdevs.flyer.utils.Constant.FLYER_MARGIN_LEFT;
import static com.ttdevs.flyer.utils.Constant.MAX_LOG_SIZE;

/**
 * @author ttdevs
 * 2018-08-28 17:01
 */
public class FlyerWindow extends LinearLayout {
    private Context mContext;
    private int mY;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();

//    static {
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//
//        mLayoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
//        mLayoutParams.height = (int) getResources().getDimension(R.dimen.window_height);
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // TYPE_PHONE
//            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        } else {
//            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        }
//
//        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
//        mLayoutParams.format = PixelFormat.TRANSLUCENT; // RGBA_8888
//        mLayoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
//        mLayoutParams.x = FLYER_MARGIN_LEFT;
//        mLayoutParams.y = mY;
//
//        params.x = 0;
//        params.y = 0;
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.gravity = Gravity.LEFT | Gravity.TOP;
//        params.type = WindowManager.LayoutParams.TYPE_PHONE;
//        params.format = PixelFormat.RGBA_8888;
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//
//        mLayoutParams = params;
//    }

    private String mLogLevel = "V";

    private View viewIcon;
    private TextView viewKeyword;
    private Spinner spLevel;
    private View viewClean;
    private CheckBox cbScroll;
    private View viewClose;
    private RecyclerView rvLog;
    private TextView tvTopActivity;
    private View viewChangeHeight;

    private ArrayList<String> mDataList = new ArrayList<>();
    private LogAdapter mAdapter;

    private LogcatUtil mLogcat;

    public FlyerWindow(Context context, int y) {
        super(context);

        mContext = context;
        mY = y;

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        initView();

        ActivityStack.addTopActivityChangeListener(new ActivityStack.OnTopActivityChangeListener() {
            @Override
            public void onTopActivityChange(String activity) {
                tvTopActivity.setText(activity);
            }
        });
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.color.global_bg_black);
        inflate(mContext, R.layout.layout_flyer_window, this);

        viewIcon = findViewById(R.id.view_icon);
        spLevel = findViewById(R.id.spLevel);
        viewKeyword = findViewById(R.id.view_keyword);
        viewClean = findViewById(R.id.view_clean);
        cbScroll = findViewById(R.id.cb_scroll);
        viewClose = findViewById(R.id.view_close);
        tvTopActivity = findViewById(R.id.tv_top_activity);
        viewChangeHeight = findViewById(R.id.view_change_height);
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
        initChangeHeight();
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

    private void initChangeHeight() {
        viewChangeHeight.setOnTouchListener(new OnTouchListener() {
            private float downY, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getRawY();
                        lastY = downY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.height += (event.getRawY() - lastY);
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
        tvTopActivity.setText(SystemUtils.getTopActivity());
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // TYPE_PHONE
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mLayoutParams.format = PixelFormat.TRANSLUCENT; // RGBA_8888
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

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case Constant.KEY_LOG_MESSAGE:
                    String logMsg = msg.obj.toString();
                    updateLog(logMsg);
                    break;

                default:
                    break;
            }
        }
    };

    private void updateLog(String logMsg) {
        mDataList.add(logMsg);

        if (mDataList.size() > MAX_LOG_SIZE) {
            for (int i = 0; i < DELETE_LOG_SIZE; i++) {
                mDataList.remove(i);
            }
        }

        if (mDataList.size() % EVERY_PRINT_LOG == 0) {
            System.err.println(String.format(">>>> Log size:" + mDataList.size()));
        }

        if (cbScroll.isChecked()) {
            rvLog.scrollToPosition(mDataList.size() - 1);
        }

        mAdapter.notifyDataSetChanged();
    }
}
