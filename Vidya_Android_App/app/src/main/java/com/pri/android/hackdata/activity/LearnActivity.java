package com.pri.android.hackdata.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.pri.android.hackdata.R;

public class LearnActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout tracingAlpha, speakAlpha,speakDigit, tracDigit,learnArth,pracPro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        init();

    }

    /*
    Initialize and bind view variables
     */
    private void init(){
        tracingAlpha = (LinearLayout )findViewById(R.id.tracing_alpha);
        speakAlpha = (LinearLayout)findViewById(R.id.speaking_alpha);
        speakDigit= (LinearLayout )findViewById(R.id.speak_digit);
        tracDigit = (LinearLayout)findViewById(R.id.trac_digit);
        learnArth = (LinearLayout )findViewById(R.id.learn_arth);
        pracPro = (LinearLayout)findViewById(R.id.prac_pro);

        tracingAlpha.setOnClickListener(LearnActivity.this);
        speakAlpha.setOnClickListener(LearnActivity.this);
        speakDigit.setOnClickListener(LearnActivity.this);
        tracDigit.setOnClickListener(LearnActivity.this);
        learnArth.setOnClickListener(LearnActivity.this);
        pracPro.setOnClickListener(LearnActivity.this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.tracing_alpha:
                intent = new Intent(LearnActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 3);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;

            case R.id.speaking_alpha:
                intent = new Intent(LearnActivity.this, SpeakingAlphabetActivity.class);
                intent.putExtra("caller", 6);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;
            case R.id.speak_digit:
                intent = new Intent(LearnActivity.this, SpeakingAlphabetActivity.class);
                intent.putExtra("caller", 7);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;

            case R.id.trac_digit:
                intent = new Intent(LearnActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 1);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;
            case R.id.learn_arth:
                intent = new Intent(LearnActivity.this, SpeakQuestionActivity.class);
                intent.putExtra("caller", 0);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;

            case R.id.prac_pro:
                intent = new Intent(LearnActivity.this, SpeakQuestionActivity.class);
                intent.putExtra("caller", 2);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;

        }
    }
}
