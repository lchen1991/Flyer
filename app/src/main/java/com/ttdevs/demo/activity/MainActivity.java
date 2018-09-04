package com.ttdevs.demo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ttdevs.demo.R;
import com.ttdevs.flyer.Flyer;

/**
 * @author ttdevs
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private Handler mHandler = new Handler();
    private int mInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler.postDelayed(this, mInterval);
    }

    @Override
    public void run() {
//        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>>" + System.currentTimeMillis());
//        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>");
//        for (int i = 0; i < 100; i++) {
//            Log.v(">>>>>", "verbose");
//            Log.d(">>>>>", "debug");
//            Log.i(">>>>>", "info");
//            Log.w(">>>>>", "warn");
//            Log.e(">>>>>", "error");
//            Log.wtf(">>>>>", "assert");
//        }
//        mHandler.postDelayed(this, mInterval);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                Flyer.show();
                break;
            case R.id.bt_fast:
                mInterval = 2;
                break;
            case R.id.bt_slow:
                mInterval = 2 * 1000;
                break;

            case R.id.bt_second:
                startActivity(new Intent(this, SecondActivity.class));
                break;
            default:
                break;
        }
    }
}
