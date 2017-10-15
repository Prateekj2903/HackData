package com.pri.android.hackdata.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pri.android.hackdata.R;

public class ChallangeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout q1,q2,q3;
    int status;

    /*
    Activity for Weekly Challenge
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challange);
        status = getIntent().getIntExtra("clickQ",0);
        init();
    }

    /*
    Initialize variables
     */
    private void init() {
        q1 = (LinearLayout)findViewById(R.id.q1);
        q2 = (LinearLayout)findViewById(R.id.q2);
        q3 = (LinearLayout)findViewById(R.id.q3);

        q1.setOnClickListener(this);
        q2.setOnClickListener(this);
        q3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent ;
        switch (view.getId()){
            case R.id.q1:
                intent = new Intent(ChallangeActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 8);
                intent.putExtra("learn", 0);
                intent.putExtra("ques",2);
                intent.putExtra("clickQ",1);
                startActivity(intent);
                break;

            case R.id.q2:
                intent = new Intent(ChallangeActivity.this, IdentifyObject.class);
                intent.putExtra("caller", 8);
                intent.putExtra("learn", 0);
                intent.putExtra("ques",3);
                intent.putExtra("clickQ",2);
                startActivity(intent);
                break;

            case R.id.q3:
                intent = new Intent(ChallangeActivity.this, SpeakQuestionActivity.class);
                intent.putExtra("caller", 8);
                intent.putExtra("learn", 0);
                intent.putExtra("ques",4);
                intent.putExtra("clickQ",3);
                startActivity(intent);
                break;
        }
    }
}
