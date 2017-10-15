package com.pri.android.hackdata.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.master.permissionhelper.PermissionHelper;
import com.pri.android.hackdata.Model;
import com.pri.android.hackdata.PaintView;
import com.pri.android.hackdata.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpeachQuestionActivity extends AppCompatActivity {
    RelativeLayout queWraper;
    private PaintView paintView;
    TextView speakAlpha;
    PermissionHelper mPermissionHelper;
    Bitmap bitmap;
    HashMap<Integer, Model> learnDigits;
    HashMap<Integer, Model> learnAlphabets;
    int caller, quesNo;
    ImageView questionBox;
    TextToSpeech t1;
    int learn;
    FloatingActionButton clearBt;
    int q =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_layout_speech);
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
        AndroidNetworking.initialize(getApplicationContext());
        queWraper = (RelativeLayout)findViewById(R.id.que_box_wrap);
        FirebaseApp.initializeApp(this);
        Log.i("Firebase token", FirebaseInstanceId.getInstance().getToken() + "");
        questionBox=(ImageView) findViewById(R.id.question_box_speach);
        speakAlpha = (TextView) findViewById(R.id.speech_digit);// 8 ya a jo question hai
        clearBt = (FloatingActionButton)findViewById(R.id.clear_button);

        clearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.clear();
            }
        });

        initializeQuestions();

        caller = getIntent().getIntExtra("caller", 0);
        q = getIntent().getIntExtra("clickQ",0);

        learn = getIntent().getIntExtra("learn",0);
        if(learn == 0){
            queWraper.setVisibility(View.INVISIBLE);
        }else{
            queWraper.setVisibility(View.VISIBLE);
        }

        quesNo = getIntent().getIntExtra("ques", 1);
        if(caller == 1){
            speakAlpha.setText(learnDigits.get(quesNo).question.split(" ")[1]);
        }else{
            speakAlpha.setText(learnAlphabets.get(quesNo).question.split(" ")[1]);
        }
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("en","IN"));
                }
            }
        });

        questionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caller == 1){
                    String toSpeak = learnDigits.get(quesNo).question.toString();
//                    Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    String toSpeak = learnAlphabets.get(quesNo).question.toString();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        Button submitBt = (Button)findViewById(R.id.submitButton);
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPermissionHelper = new PermissionHelper(SpeachQuestionActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                mPermissionHelper.request(new PermissionHelper.PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        saveImage();
                    }

                    @Override
                    public void onPermissionDenied() {

                    }

                    @Override
                    public void onPermissionDeniedBySystem() {

                    }
                });

            }
        });

    }
    /*
       populate questions
        */
    private void initializeQuestions() {
        learnDigits = new HashMap<>();
        learnAlphabets = new HashMap<>();

        Model m = new Model();
        m.question = "Draw 8";
        m.answer = "8";
        learnDigits.put(1, m);

        m = new Model();
        m.question = "Draw 4";
        m.answer = "4";
        learnDigits.put(2, m);
        m = new Model();
        m.question = "Draw 7";
        m.answer = "7";
        learnDigits.put(3, m);
        m = new Model();
        m.question = "Draw 9";
        m.answer = "9";
        learnDigits.put(4, m);

        m = new Model();
        m.question = "Draw D";
        m.answer = "D";
        learnAlphabets.put(1, m);
        m = new Model();
        m.question = "Draw F";
        m.answer = "F";
        learnAlphabets.put(2, m);
        m = new Model();
        m.question = "Draw K";
        m.answer = "K";
        learnAlphabets.put(3, m);
        m = new Model();
        m.question = "Draw L";
        m.answer = "L";
        learnAlphabets.put(4, m);
    }

    private void saveImage() {
        paintView.setDrawingCacheEnabled(true);
        Bitmap b = paintView.getDrawingCache();
        File file,f=new File("/string.png");
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            file =new File(android.os.Environment.getExternalStorageDirectory(),"DrawnImages");
            if(!file.exists())
            {
                file.mkdirs();

            }
            f = new File(file.getAbsolutePath()+ "/image14" +
                    ""+".png");
            if(f.exists()){
                f.delete();
                f = new File(file.getAbsolutePath()+ "/image14" +
                        ""+".png");
            }
        }
        try{
        FileOutputStream ostream = new FileOutputStream(f);
        b.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        sendPic(f);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        System.out.println(encodedImage.length());
        return encodedImage;
    }

    String url1 = "http://192.168.43.254:8079/image";//ip
    String url2 = "http://Aaditya21396.pythonanywhere.com/image";

    ProgressDialog loading;

    private void sendPic(File f) {

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(f));
            loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);


            Map<String, String> jsonParams = new HashMap<String, String>();
            String image = getStringImage(bitmap);
            if(caller == 1) {
                jsonParams.put("imageFile", "0" + image);
            }else{
                jsonParams.put("imageFile", "1" + image);
            }

            JsonObjectRequest myRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url1,
                    new JSONObject(jsonParams),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            loading.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            volleyError.printStackTrace();
                            if(loading!=null && loading.isShowing() )
                                loading.dismiss();

                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
//            MyApplication.getInstance().addToRequestQueue(myRequest, "tag");
            myRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(myRequest);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ans = intent.getExtras().getString("ans");
            Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_SHORT);
            if(caller == 1){
                String correct = learnDigits.get(quesNo).answer.toString();
                if(correct.equals(ans)){
                    Intent mintent = new Intent(SpeachQuestionActivity.this, AnswerAcceptActivity.class);
                    mintent.putExtra("caller", caller);
                    mintent.putExtra("learn",learn);
                    mintent.putExtra("ques", quesNo);
                    mintent.putExtra("clickQ",q);
                    if(loading!=null && loading.isShowing()){
                        loading.dismiss();
                    }
                    startActivity(mintent);
                    finish();
                }else{
                    Intent mintent = new Intent(SpeachQuestionActivity.this, AnswerRejectActivity.class);
                    mintent.putExtra("caller", caller);
                    mintent.putExtra("learn",learn);
                    mintent.putExtra("ques", quesNo);
                    mintent.putExtra("clickQ",q);
                    if(loading!=null && loading.isShowing()){
                        loading.dismiss();
                    }
                    startActivity(mintent);
                    finish();
                }
            }else{
                String correct = learnAlphabets.get(quesNo).answer.toString();
                if(correct.equals(ans)){
                    Intent mintent = new Intent(SpeachQuestionActivity.this, AnswerAcceptActivity.class);
                    mintent.putExtra("caller", caller);
                    mintent.putExtra("ques", quesNo);
                    mintent.putExtra("learn",learn);
                    mintent.putExtra("clickQ",q);
                    if(loading!=null && loading.isShowing()){
                        loading.dismiss();
                    }
                    startActivity(mintent);
                    finish();
                }else{
                    Intent mintent = new Intent(SpeachQuestionActivity.this, AnswerRejectActivity.class);
                    mintent.putExtra("caller", caller);
                    mintent.putExtra("ques", quesNo);
                    mintent.putExtra("learn",learn);
                    mintent.putExtra("clickQ",q);
                    if(loading!=null && loading.isShowing()){
                        loading.dismiss();
                    }
                    startActivity(mintent);
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("Answer")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}