package com.hwstudio.wordpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Settings
    int[] pitch = new int[2];
    int[] speechRate = new int[2];
    Locale[] language = new Locale[2];
    float[] soundVolume = new float[2];

    // Variables
    TextToSpeech[] tts = new TextToSpeech[2];
    UtteranceProgressListener[] utterance = new UtteranceProgressListener[2];
    String[] listString = new String[2];
    String[] wordString = new String[2];
    int[] wordStart = new int[2];
    int[] wordEnd = new int[2];
    boolean isEnd = false;
    EditText lang1EditText, lang2EditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariable(0);
        initVariable(1);
        initTTS(tts[0], pitch[0], speechRate[0]);
        initTTS(tts[1], pitch[1], speechRate[1]);
        utterance[0] = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                pickWord(1);
            }

            @Override
            public void onError(String s) {

            }
        };
        utterance[1] = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                if (!isEnd) {
                    pickWord(0);
                }
            }

            @Override
            public void onError(String s) {

            }
        };
        lang1EditText = findViewById(R.id.lang1EditText);
        lang2EditText = findViewById(R.id.lang2EditText);

    }

    private void initVariable(int listNum) {
        wordStart[listNum] = 0;
        tts[listNum] = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });

        // debug: to be implemented
        pitch[listNum] = 3;
        speechRate[listNum] = 3;
        language[0] = Locale.CHINESE;
        language[1] = Locale.ENGLISH;
        soundVolume[listNum] = 100f;
        // debug
    }

    public void initTTS(TextToSpeech getTTS, int getPitch, int getSpeechRate) {
//        textToSpeech = getTTS;
//        language = getLanguage;
//        soundVolume = getSoundVolume;
        float[] pitchFloat = {0.4f, 0.6f, 0.8f, 1f, 1.5f, 2f, 2.5f};
        getTTS.setPitch(pitchFloat[getPitch]);
        float[] speechRateFloat = {0.1f, 0.4f, 0.75f, 1f, 1.5f, 2f, 2.5f};
        getTTS.setSpeechRate(speechRateFloat[getSpeechRate]);
    }

    public void clickPlay(View view) {
        listString[0] = lang1EditText.getText().toString();
        listString[1] = lang2EditText.getText().toString();
        pickWord(0);
    }

    private void pickWord(int listNum) {
        //boolean isEnd = false;
        wordEnd[listNum] = listString[listNum].indexOf(10, wordStart[listNum]);
        if (wordEnd[listNum] > -1) {
            wordString[listNum] = listString[listNum].substring(wordStart[listNum], wordEnd[listNum]);
        } else {
            wordString[listNum] = listString[listNum].substring(wordStart[listNum]);
            if (listNum == 1) {
                isEnd = true;
            }
        }
        Toast.makeText(this, wordString[listNum], Toast.LENGTH_SHORT).show();
        speakString(tts[listNum], language[listNum], soundVolume[listNum], wordString[listNum], utterance[listNum]);
    }


    public void speakString(TextToSpeech getTTS, Locale language, float soundVolume, String getString, UtteranceProgressListener utterance) {
        getTTS.setOnUtteranceProgressListener(utterance);
        getTTS.setLanguage(language);
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, soundVolume / 100f);
        getTTS.speak(getString, TextToSpeech.QUEUE_ADD, params
                , TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }

    @Override
    protected void onDestroy() {
        tts[0].shutdown();
        tts[1].shutdown();
        super.onDestroy();
    }
}