package com.ttdevs.floatlog.utils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ttdevs
 * 2018-08-29 11:06
 */
public class LogcatUtil extends Thread {

    private boolean isQuit = false;

    private Handler mHandler;
    private String mKeyword;
    private String mLogLevel;

    public LogcatUtil(Handler handler, String keyword, String logLevel) {
        mHandler = handler;
        mLogLevel = logLevel;
        mKeyword = keyword;

        if (TextUtils.isEmpty(mKeyword)) {
            mKeyword = "*";
        }
    }

    @Override
    public void run() {
        Process process = null;
        BufferedReader bufferedReader = null;
        try {
            String cmd = String.format("logcat %s:%s", mKeyword, mLogLevel);
            process = Runtime.getRuntime().exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            isQuit = true;
            return;
        }
        while (!isQuit()) {
            try {
                String line = bufferedReader.readLine();
                sendMessage(line + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bufferedReader.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String msgString) {
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        msg.obj = msgString;
        mHandler.sendMessage(msg);
    }

    private boolean isQuit() {
        return isQuit;
    }

    public void quit() {
        isQuit = true;
    }
}
