package com.ttdevs.demo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.ttdevs.floatlog.FloatLog;

/**
 * @author ttdevs
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, Runnable {
    public static final int REPEAT_INTERVAL = 2000;

    private ToggleButton tbOperate;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbOperate = findViewById(R.id.tb_operate);
        tbOperate.setOnCheckedChangeListener(this);

        mHandler.postDelayed(this, REPEAT_INTERVAL);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            FloatLog.showWindow();
        } else {
            FloatLog.dismissWindow();
        }
    }

    @Override
    public void run() {
        System.out.println(">>>>>" + System.currentTimeMillis());
        System.err.println("<<<<<" + System.currentTimeMillis());

        Log.v("v>>>>>", "verbose");
        Log.d("d>>>>>", "debug");
        Log.i("i>>>>>", "info");
        Log.w("w>>>>>", "warn");
        Log.e("e>>>>>", "error");
        Log.wtf("wtf>>>>>", "assert");

        mHandler.postDelayed(this, REPEAT_INTERVAL);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
