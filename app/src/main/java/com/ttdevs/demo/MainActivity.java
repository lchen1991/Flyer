package com.ttdevs.demo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ttdevs.flyer.Flyer;

/**
 * @author ttdevs
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    public static final int REPEAT_INTERVAL = 2000;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler.postDelayed(this, REPEAT_INTERVAL);
    }

    @Override
    public void run() {

        Log.v(">>>>>", "verbose");
        Log.d(">>>>>", "debug");
        Log.i(">>>>>", "info");
        Log.w(">>>>>", "warn");
        Log.e(">>>>>", "error");
        Log.wtf(">>>>>", "assert");

        System.err.println(">>>>>" + System.currentTimeMillis());
        System.out.println(">>>>>" + System.currentTimeMillis());
        Log.v(">>>>>", "========================");

        mHandler.postDelayed(this, REPEAT_INTERVAL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                Flyer.show();
                break;
            case R.id.bt_second:
                startActivity(new Intent(this, SecondActivity.class));
                break;
            default:
                break;
        }
    }
}
