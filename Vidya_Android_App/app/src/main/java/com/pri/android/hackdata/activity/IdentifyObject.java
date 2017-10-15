package com.pri.android.hackdata.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pri.android.hackdata.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class IdentifyObject extends AppCompatActivity implements View.OnClickListener{

    private final int REQ_CODE_SPEECH_INPUT = 100;
    Button submit;
    ImageView imageFrame,imgRecord;
    EditText answer;
    Intent callingIntent;
    HashMap<Integer, Model> imageDB;
    String finalAns;

    int quesNo = 0;
    int caller = 0;
    int learn = 0;
    int q=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_object);
        init();

        callingIntent = getIntent();
        quesNo = callingIntent.getIntExtra("ques", 1);
        caller = callingIntent.getIntExtra("caller", 0);
        learn = callingIntent.getIntExtra("learn",0);
        q = callingIntent.getIntExtra("clickQ",0);
        initializeQuestions();

        initQuestionImage();

    }

    private void initQuestionImage() {
        imageFrame.setImageDrawable(getResources().getDrawable(imageDB.get(quesNo).id));
    }

    private void init() {
        submit = (Button)findViewById(R.id.submitButton);
        imageFrame = (ImageView)findViewById(R.id.image_frame);
        imgRecord = (ImageView)findViewById(R.id.btn_record);
        answer = (EditText)findViewById(R.id.speak_edit_box);

        submit.setOnClickListener(this);
        imgRecord.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submitButton:
                checkfinalAns();
                break;
            case R.id.btn_record:
                promptSpeechInput();
                break;
        }
    }

    /*
    Check final answer with computed answer
     */
    private void checkfinalAns() {
        String correct = imageDB.get(quesNo).answer;
        if(correct.equalsIgnoreCase(finalAns)){
            Intent intent = new Intent(IdentifyObject.this, AnswerAcceptActivity.class);
            intent.putExtra("caller", caller);
            intent.putExtra("learn", learn);
            intent.putExtra("ques", quesNo);
            intent.putExtra("clickQ",q);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(IdentifyObject.this, AnswerRejectActivity.class);
            intent.putExtra("caller", caller);
            intent.putExtra("ques", quesNo);
            intent.putExtra("learn", learn);
            intent.putExtra("clickQ",q);
            startActivity(intent);
            finish();
        }
    }

    class Model{
        int id;
        String answer;
    }
    /*
    Initialize Questions
     */
    private void initializeQuestions() {
        imageDB= new HashMap<>();
        Model m = new Model();
        m.id = R.drawable.apple;
        m.answer = "apple";
        imageDB.put(1, m);

        m = new Model();
        m.id = R.drawable.cat;
        m.answer = "cat";
        imageDB.put(2, m);
        m = new Model();
        m.id = R.drawable.chair;
        m.answer = "chair";
        imageDB.put(3, m);
        m = new Model();
        m.id = R.drawable.dog;
        m.answer = "dog";
        imageDB.put(4, m);

    }

    //ui for receiving voice input
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
                    answer.setText(ans);
                }
                break;
            }

        }
    }

}
