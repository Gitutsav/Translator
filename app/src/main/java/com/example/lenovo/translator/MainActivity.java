package com.example.lenovo.translator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
EditText inputtext, editText;
    Button bf,bg,bs;
    EditText e;
    String sf,sg,ss;
    TextToSpeech tf,tg,ts;
    TextView french, german, spanish;
    String result = "", result1=" ", result2=" ", result3=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bf = (Button)findViewById(R.id.texttospeechFrench);
        bg=(Button)findViewById(R.id.texttospeechGerman);
        bs=(Button)findViewById(R.id.texttospeechSpanish);
        e = (EditText)findViewById(R.id.editText);
        tf = new TextToSpeech((Context)this, new TextToSpeech.OnInitListener(){

            public void onInit(int n) {
                tf.setLanguage(Locale.FRENCH);
                tf.setSpeechRate(0.2f);
            }
        });
        tg = new TextToSpeech((Context)this, new TextToSpeech.OnInitListener(){

            public void onInit(int n) {
                tg.setLanguage(Locale.GERMAN);
                tg.setSpeechRate(0.2f);
            }
        });
        ts = new TextToSpeech((Context)this, new TextToSpeech.OnInitListener(){

            public void onInit(int n) {
                ts.setLanguage(Locale.GERMAN);
                ts.setSpeechRate(0.2f);
            }
        });

        bf.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                sf = result1;
                tf.speak(sf, 0, null);
            }
        });
        bg.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                sg = result2;
                tg.speak(sg, 0, null);
            }
        });
        bs.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                ss = result3;
                ts.speak(ss, 0, null);
            }
        });
        editText = (EditText) findViewById(R.id.editText);
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    editText.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        findViewById(R.id.button1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        editText.setText("");
                        editText.setHint("Listening...");
                        break;
                }
                return false;
            }
        });
    }


    public void onTranslateclick(View view) {
        inputtext=(EditText)findViewById(R.id.editText);
        if(!isEmpty(inputtext))
        {
            Toast.makeText(this, "getting translations",Toast.LENGTH_LONG).show();
            new Savethefeed().execute();
        }
        else{
            Toast.makeText(this, "enter words to translate",Toast.LENGTH_LONG).show();

        }
    }

    private boolean isEmpty(EditText inputtext) {
        return inputtext.getText().toString().trim().length()==0 ;
    }
    class Savethefeed extends AsyncTask<Void, Void, Void>{
        String jsonString = "";


        @Override
        protected Void doInBackground(Void... voids) {
            EditText translateeditText = (EditText)findViewById(R.id.editText);
            String wordsToTranslate = translateeditText.getText().toString();
            wordsToTranslate=wordsToTranslate.replace(" " , "+");
            DefaultHttpClient httpClient=new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://newjustin.com/translateit.php?action=translations&english_words=" + wordsToTranslate);
            httpPost.setHeader("Content-type","application/json");
            InputStream inputStream =null;
            try{
                HttpResponse httpResponse= httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream= httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line=reader.readLine())!=null){
                    sb.append(line + "\n");
                }
                jsonString=sb.toString();
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray= jsonObject.getJSONArray("translations");
                outputTranslations(jsonArray);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           //TextView translationTextView = (TextView)findViewById(R.id.translationTextView);
           TextView french=(TextView)findViewById(R.id.translationTextViewFrench);
           TextView german=(TextView)findViewById(R.id.translationTextViewGerman);
           TextView spanish=(TextView)findViewById(R.id.translationTextViewSpanich);
            french.setText("FRENCH: "+result1);
            german.setText("GERMAN: "+result2);
            spanish.setText("SPANISH: "+result3);
            //translationTextView.setText(result1);
        }

        protected  void outputTranslations(JSONArray jsonArray){
            String[] languages= {"arabic", "chinese","danish","dutch", "french","german", "italian","portuguese","russian","spanish"};
            try {
      //      for (int i=0;i<jsonArray.length();i++)
        //    {
                JSONObject translationObject=jsonArray.getJSONObject(4);
                result1=translationObject.getString(languages[4])+"\n";
                JSONObject translationObject1=jsonArray.getJSONObject(5);
                result2=translationObject1.getString(languages[5])+"\n";
                JSONObject translationObject2=jsonArray.getJSONObject(9);
                result3=translationObject2.getString(languages[9]);
                //+"\n"+languages[6]+":"+ translationObject.getString(languages[6])+"\n"+languages[9]+":"+ translationObject.getString(languages[9]);
           // }
                result=languages[4]+":"+result1+languages[5]+":"+ result2+languages[9]+":"+ result3;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}
