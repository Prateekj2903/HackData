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

public class AnswerAcceptActivity extends AppCompatActivity {


    /*
    Activity Called when correct Answer Selected
     */
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
                int learn= callingIntent.getIntExtra("learn", 1);
                int caller = callingIntent.getIntExtra("caller", 0);
                int quesNo = callingIntent.getIntExtra("ques", 0);
                int q = callingIntent.getIntExtra("clickQ",0);

                Intent intent = new Intent(AnswerAcceptActivity.this, SpeachQuestionActivity.class);

                intent.putExtra("learn",learn);

                if(caller == 0 || caller == 2){
                    intent = new Intent(AnswerAcceptActivity.this, SpeakQuestionActivity.class);
                    intent.putExtra("caller", caller);
                }else if(caller==1 || caller == 3){
                    intent = new Intent(AnswerAcceptActivity.this, SpeachQuestionActivity.class);
                    intent.putExtra("caller", caller);
                }else if(caller==6 || caller ==7){
                    intent = new Intent(AnswerAcceptActivity.this, SpeakingAlphabetActivity.class);
                    intent.putExtra("caller", caller);
                }else if(caller==4){
                    intent = new Intent(AnswerAcceptActivity.this, FindObject.class);
                    intent.putExtra("caller", caller);
                }else if(caller == 5){
                    intent = new Intent(AnswerAcceptActivity.this, IdentifyObject.class);
                    intent.putExtra("caller", caller);
                }else if(caller == 8){//challenge
                    intent = new Intent(AnswerAcceptActivity.this, ChallangeActivity.class);
                    intent.putExtra("caller", caller);
                    intent.putExtra("status", 1);
                    intent.putExtra("clickQ",q);

                }
                intent.putExtra("learn", learn);
                intent.putExtra("ques", (quesNo)%4+1);
                startActivity(intent);
                finish();
            }
        });
    }
}
