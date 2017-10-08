package com.pri.android.hackdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Priyanshu on 08-10-2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout funWithMaths, learnDigits, funWithWords, learnAlphabets;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        init();
    }

    private void init() {
        funWithMaths = (LinearLayout)findViewById(R.id.funWithMaths);
        learnDigits = (LinearLayout)findViewById(R.id.learnDigit);
        learnAlphabets = (LinearLayout)findViewById(R.id.learnAlphabets);
        funWithWords = (LinearLayout)findViewById(R.id.funWithWords);
        funWithWords.setOnClickListener(this);
        funWithMaths.setOnClickListener(this);
        learnDigits.setOnClickListener(this);
        learnAlphabets.setOnClickListener(this);
    }

    Intent intent;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.funWithMaths:
                intent = new Intent(MainActivity.this, SpeakQuestionActivity.class);
                intent.putExtra("caller", 0);
                startActivity(intent);
                break;
            case R.id.funWithWords:
                intent = new Intent(MainActivity.this, SpeakQuestionActivity.class);
                intent.putExtra("caller", 2);
                startActivity(intent);
                break;
            case R.id.learnAlphabets:
                intent = new Intent(MainActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 3);
                startActivity(intent);
                break;
            case R.id.learnDigit:
                intent = new Intent(MainActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 1);
                startActivity(intent);
                break;

        }
    }
}
