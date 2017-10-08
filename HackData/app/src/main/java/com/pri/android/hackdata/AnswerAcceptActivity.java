package com.pri.android.hackdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Priyanshu on 08-10-2017.
 */

public class AnswerAcceptActivity extends AppCompatActivity {

    ImageView nextBt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_answer);
        nextBt = (ImageView)findViewById(R.id.nextButton);
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callingIntent = getIntent();
                int caller = callingIntent.getIntExtra("caller", 0);
                int quesNo = callingIntent.getIntExtra("ques", 0);
                Intent intent = new Intent(AnswerAcceptActivity.this, SpeachQuestionActivity.class);
                if(caller == 0 || caller == 2){
                    intent = new Intent(AnswerAcceptActivity.this, SpeakQuestionActivity.class);
                    intent.putExtra("caller", caller);
                }else if(caller==1 || caller == 3){
                    intent = new Intent(AnswerAcceptActivity.this, SpeachQuestionActivity.class);
                    intent.putExtra("caller", caller);
                }
                intent.putExtra("ques", quesNo+1);
                startActivity(intent);
                finish();
            }
        });
    }
}
