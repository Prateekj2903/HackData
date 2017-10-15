package com.pri.android.hackdata.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.pri.android.hackdata.R;

/**
 * Created by Priyanshu on 08-10-2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout learn, practice, challange;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        init();
    }
// bind variable with views
    private void init() {

        learn = (LinearLayout)findViewById(R.id.learn);
        practice = (LinearLayout)findViewById(R.id.practice);
        challange = (LinearLayout)findViewById(R.id.challenges);

        learn.setOnClickListener(this);
        practice.setOnClickListener(this);
        challange.setOnClickListener(this);
    }

    Intent intent;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.learn:
                intent = new Intent(MainActivity.this, LearnActivity.class);
                startActivity(intent);
                break;
            case R.id.practice:
                intent = new Intent(MainActivity.this, PracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.challenges:
                intent = new Intent(MainActivity.this, ChallangeActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
        super.onBackPressed();
    }
}
