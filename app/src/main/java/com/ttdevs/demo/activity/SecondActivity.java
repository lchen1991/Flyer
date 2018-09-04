package com.ttdevs.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ttdevs.demo.R;

/**
 * @author ttdevs
 */
public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_third:
                startActivity(new Intent(this, ThirdActivity.class));
                break;
            case R.id.bt_finish:
                finish();
                break;

            default:
                break;
        }
    }
}
