package com.pri.android.hackdata.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.pri.android.hackdata.R;

/**
 * Created by Priyanshu on 08-10-2017.
 */

public class AnswerRejectActivity extends AppCompatActivity {

    /*
    Activity Called when Wrong Answer Selected
     */
    ImageView redoBt;
    int caller, quesNo,learn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reject_answer);
        redoBt = (ImageView)findViewById(R.id.retryButton);
        caller = getIntent().getIntExtra("caller", 0);
        quesNo = getIntent().getIntExtra("ques", 0);
        learn = getIntent().getIntExtra("learn",0);

        redoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnswerRejectActivity.this, SpeachQuestionActivity.class);

                intent.putExtra("learn", learn);
                if(caller == 0 || caller == 2){
                    intent = new Intent(AnswerRejectActivity.this, SpeakQuestionActivity.class);
                    intent.putExtra("caller", caller);
                    intent.putExtra("learn", learn);
                }else if(caller==1 || caller == 3){
                    intent = new Intent(AnswerRejectActivity.this, SpeachQuestionActivity.class);
                    intent.putExtra("caller", caller);
                    intent.putExtra("learn", learn);
                }else if(caller==4){
                    intent = new Intent(AnswerRejectActivity.this, FindObject.class);
                    intent.putExtra("caller", caller);
                }else if(caller == 5){
                    intent = new Intent(AnswerRejectActivity.this, IdentifyObject.class);
                    intent.putExtra("caller", caller);
                }else if(caller == 8){//challenge
                    intent = new Intent(AnswerRejectActivity.this, ChallangeActivity.class);
                    intent.putExtra("caller", caller);
                    intent.putExtra("status", 0);

                }else {
                    intent = new Intent(AnswerRejectActivity.this, SpeakingAlphabetActivity.class);
                    intent.putExtra("learn", learn);

                }
                intent.putExtra("ques", quesNo);
                startActivity(intent);
                finish();
            }
        });
    }
}
