package com.pri.android.hackdata;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Priyanshu on 08-10-2017.
 */

public class SpeakQuestionActivity extends AppCompatActivity {


    private ImageView btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    HashMap<Integer, Model> funWithNum;
    HashMap<Integer, Model> funWithWords;
    Intent callingIntent;
    TextView questionBox;
    int quesNo = 0;
    int caller = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_layout_speak);

        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        questionBox = (TextView)findViewById(R.id.question_box);

        initializeQuestions();

        callingIntent = getIntent();
        quesNo = callingIntent.getIntExtra("ques", 1);
        caller = callingIntent.getIntExtra("caller", 0);

        if(caller == 0){
            questionBox.setText(funWithNum.get(quesNo).question.toString());
        }else{
            questionBox.setText(funWithWords.get(quesNo).question.toString());
        }

    }

    private void initializeQuestions() {
        funWithNum= new HashMap<>();
        funWithWords= new HashMap<>();
        Model m = new Model();
        m.question = "What is 2+2?";
        m.answer = "4";
        funWithNum.put(1, m);
        m = new Model();
        m.question = "What is 4+4?";
        m.answer = "8";
        funWithNum.put(2, m);
        m = new Model();
        m.question = "What is 8+1?";
        m.answer = "9";
        funWithNum.put(3, m);
        m = new Model();
        m.question = "What is 11+3?";
        m.answer = "14";
        funWithNum.put(4, m);

        m = new Model();
        m.question = "Speak CAT";
        m.answer = "cat";
        funWithWords.put(1, m);
        m = new Model();
        m.question = "Speak SAT";
        m.answer = "sat";
        funWithWords.put(2, m);
        m = new Model();
        m.question = "Speak YES";
        m.answer = "yes";
        funWithWords.put(3, m);
        m = new Model();
        m.question = "Speak OKAY";
        m.answer = "ok";
        funWithWords.put(4, m);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtSpeechInput.setText(result.get(0));
                    String ans = result.get(0); //the answer spoken
                    Toast.makeText(this, ans, Toast.LENGTH_SHORT).show();
                    //TODO check ans
                    if(caller == 0){
                        String correct = funWithNum.get(quesNo).answer.toString();
                        if(correct.equals(ans)){
                            Intent intent = new Intent(SpeakQuestionActivity.this, AnswerAcceptActivity.class);
                            intent.putExtra("caller", caller);
                            intent.putExtra("ques", quesNo);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(SpeakQuestionActivity.this, AnswerRejectActivity.class);
                            intent.putExtra("caller", caller);
                            intent.putExtra("ques", quesNo);
                            Toast.makeText(this, ""+ans, Toast.LENGTH_SHORT);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        String correct = funWithWords.get(quesNo).answer.toString();
                        if(correct.equals(ans)){
                            Intent intent = new Intent(SpeakQuestionActivity.this, AnswerAcceptActivity.class);
                            intent.putExtra("caller", caller);
                            intent.putExtra("ques", quesNo);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(SpeakQuestionActivity.this, AnswerRejectActivity.class);
                            intent.putExtra("caller", caller);
                            intent.putExtra("ques", quesNo);
                            Toast.makeText(this, ""+ans, Toast.LENGTH_SHORT);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                break;
            }

        }
    }


}
