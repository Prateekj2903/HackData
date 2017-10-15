package com.pri.android.hackdata.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pri.android.hackdata.R;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout drawChar ,idObj,findObj,drawDig, pracArith,pracPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        init();
    }
    /*
   Initialize and bind view variables
    */
    public void init() {
        drawChar = (LinearLayout) findViewById(R.id.draw_alpha);
        idObj = (LinearLayout)findViewById(R.id.id_obj);
        findObj = (LinearLayout) findViewById(R.id.find_obj);
        drawDig = (LinearLayout)findViewById(R.id.draw_dig);
        pracArith = (LinearLayout) findViewById(R.id.prac_arth);
        pracPro = (LinearLayout)findViewById(R.id.prac_pro);

        drawChar.setOnClickListener(this);
        idObj.setOnClickListener(this);
        findObj.setOnClickListener(this);
        drawDig.setOnClickListener(this);
        pracArith.setOnClickListener(this);
        pracPro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.draw_alpha:
                intent = new Intent(PracticeActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 3);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;

            case R.id.id_obj:
                intent = new Intent(PracticeActivity.this, IdentifyObject.class);
                intent.putExtra("caller", 5);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
            case R.id.find_obj:
                intent = new Intent(PracticeActivity.this, FindObject.class);
                intent.putExtra("caller", 4);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
            case R.id.draw_dig:
                intent = new Intent(PracticeActivity
                        .this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 1);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
            case R.id.prac_arth:
                intent = new Intent(PracticeActivity.this, SpeakQuestionActivity.class);
                intent.putExtra("caller", 0);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
            case R.id.prac_pro:
                intent = new Intent(PracticeActivity.this, SpeakQuestionActivity.class);
                intent.putExtra("caller", 2);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;

        }
    }
}

