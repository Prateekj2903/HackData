package com.pri.android.hackdata.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pri.android.hackdata.Model;
import com.pri.android.hackdata.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SpeakingAlphabetActivity extends AppCompatActivity {


    private ImageView btnSpeak;
    RelativeLayout questionSpeak;
    EditText editTextAns;
    Button btSubmit;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    HashMap<Integer, Model> funWithNum;
    HashMap<Integer, Model> funWithWords;
    Intent callingIntent;
    TextView questionBox;
    int quesNo = 0;
    int caller = 0;
    int learn =0;
    String finalAns;
    TextToSpeech t1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_alphabet);

        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        questionSpeak = (RelativeLayout)findViewById(R.id.question_box_speach);

        questionBox = (TextView)findViewById(R.id.question_box);
        btSubmit = (Button)findViewById(R.id.submitButton);
        initializeQuestions();

        callingIntent = getIntent();
        quesNo = callingIntent.getIntExtra("ques", 1);
        caller = callingIntent.getIntExtra("caller", 0);
//        caller = caller%4;
        learn = callingIntent.getIntExtra("learn", 0);
        editTextAns = (EditText)findViewById(R.id.speak_edit_box);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("en","IN"));
                }
            }
        });
        //Speak digit or letter
        questionSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caller == 6){
                    String toSpeak = funWithWords.get(quesNo).question.toString();
//                    Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    String toSpeak = funWithNum.get(quesNo).question.toString();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        });

        if(caller == 6){
            questionBox.setText(funWithWords.get(quesNo).question.toString());
        }else{
            questionBox.setText(funWithNum.get(quesNo).question.toString());
        }
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caller == 6){//
                    String correct = funWithWords.get(quesNo).answer.toString();
                    if(correct.equals(finalAns)){
                        Intent intent = new Intent(SpeakingAlphabetActivity.this, AnswerAcceptActivity.class);
                        intent.putExtra("caller", 6);//speaking alpha
                        intent.putExtra("ques", quesNo);
                        intent.putExtra("learn", 1);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SpeakingAlphabetActivity.this, AnswerRejectActivity.class);
                        intent.putExtra("caller", 7);//speaking digit
                        intent.putExtra("ques", quesNo);
                        intent.putExtra("learn", learn);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    String correct = funWithNum.get(quesNo).answer.toString();
                    if(correct.equals(finalAns)){
                        Intent intent = new Intent(SpeakingAlphabetActivity.this, AnswerAcceptActivity.class);
                        intent.putExtra("caller", caller);
                        intent.putExtra("learn", learn);
                        intent.putExtra("ques", quesNo);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SpeakingAlphabetActivity.this, AnswerRejectActivity.class);
                        intent.putExtra("caller", caller);
                        intent.putExtra("learn", learn);
                        intent.putExtra("ques", quesNo);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    /*
       Initialize and populate Questions
        */
    private void initializeQuestions() {
        funWithNum= new HashMap<>();
        funWithWords= new HashMap<>();
        Model m = new Model();
        m.question = "Speak 1";
        m.answer = "1";
        funWithNum.put(1, m);
        m = new Model();
        m.question = "Speak 3";
        m.answer = "3";
        funWithNum.put(2, m);
        m = new Model();
        m.question = "Speak 5";
        m.answer = "5";
        funWithNum.put(3, m);
        m = new Model();
        m.question = "Speak 9";
        m.answer = "9";
        funWithNum.put(4, m);

        m = new Model();
        m.question = "Speak a";
        m.answer = "a";
        funWithWords.put(1, m);
        m = new Model();
        m.question = "Speak k";
        m.answer = "k";
        funWithWords.put(2, m);
        m = new Model();
        m.question = "Speak b";
        m.answer = "b";
        funWithWords.put(3, m);
        m = new Model();
        m.question = "Speak h";
        m.answer = "h";
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
