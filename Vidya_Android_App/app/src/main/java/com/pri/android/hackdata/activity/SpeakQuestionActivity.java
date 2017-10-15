package com.pri.android.hackdata.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pri.android.hackdata.Model;
import com.pri.android.hackdata.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Priyanshu on 08-10-2017.
 */

public class SpeakQuestionActivity extends AppCompatActivity {
    FrameLayout youtube;
    RelativeLayout speak;
    private ImageView btnSpeak;
    EditText editTextAns;
    Button btSubmit;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    HashMap<Integer, Model> funWithNum;
    HashMap<Integer, Model> funWithWords;
    Intent callingIntent;
    TextView questionBox;
    int quesNo = 0;
    int caller = 0;
    int learn = 0;
    int q=0;
    String finalAns;
    TextToSpeech t1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_layout_speak);

        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);
//        youtube = (FrameLayout)findViewById(R.id.youtube_view);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        questionBox = (TextView)findViewById(R.id.question_box);
        btSubmit = (Button)findViewById(R.id.submitButton);
        initializeQuestions();

        callingIntent = getIntent();
        quesNo = callingIntent.getIntExtra("ques", 1);
        caller = callingIntent.getIntExtra("caller", 0);
        q = callingIntent.getIntExtra("clickQ",0);
        learn = callingIntent.getIntExtra("learn",0);
        editTextAns = (EditText)findViewById(R.id.speak_edit_box);
        speak = (RelativeLayout)findViewById(R.id.question_box_speach);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("en","IN"));
                }
            }
        });
        if(caller == 0){
            questionBox.setText(funWithNum.get(quesNo).question.toString());
        }else{
            questionBox.setText(funWithWords.get(quesNo).question.toString());
        }
        if(learn == 1){
            speak.setVisibility(View.VISIBLE);
            speak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(caller == 2){
                        String toSpeak = funWithWords.get(quesNo).question.toString();
//                    Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }

                }
            });
        }else{
            //// TODO: 15-10-2017  
        }
        if(learn == 1 && caller ==0){
//
//            BlankFragment fragment = new BlankFragment();
//            FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction()
//                    .replace(R.id.youtube_view, fragment)
//                    .commit();
        }

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caller == 0){
                    String correct = funWithNum.get(quesNo).answer.toString();
                    if(correct.equals(finalAns)){
                        Intent intent = new Intent(SpeakQuestionActivity.this, AnswerAcceptActivity.class);
                        intent.putExtra("caller", caller);
                        intent.putExtra("learn", learn);
                        intent.putExtra("ques", quesNo);
                        intent.putExtra("clickQ",q);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SpeakQuestionActivity.this, AnswerRejectActivity.class);
                        intent.putExtra("caller", caller);
                        intent.putExtra("ques", quesNo);
                        intent.putExtra("learn", learn);
                        intent.putExtra("clickQ",q);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    String correct = funWithWords.get(quesNo).answer.toString();
                    if(correct.equals(finalAns)){
                        Intent intent = new Intent(SpeakQuestionActivity.this, AnswerAcceptActivity.class);
                        intent.putExtra("caller", caller);
                        intent.putExtra("ques", quesNo);
                        intent.putExtra("learn", learn);
                        intent.putExtra("clickQ",q);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SpeakQuestionActivity.this, AnswerRejectActivity.class);
                        intent.putExtra("caller", caller);
                        intent.putExtra("ques", quesNo);
                        intent.putExtra("learn", learn);
                        intent.putExtra("clickQ",q);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
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
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);

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
                    finalAns = ans;
                    Toast.makeText(this, ans, Toast.LENGTH_SHORT).show();
                    //TODO check ans
                    editTextAns.setText(ans);
                }
                break;
            }

        }
    }


}
