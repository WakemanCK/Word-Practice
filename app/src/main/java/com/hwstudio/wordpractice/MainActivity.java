package com.hwstudio.wordpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech tts1, tts2;
    int pitch1, pitch2, speechRate1, speechRate2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickPlay(View view){

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

    public void speakString(TextToSpeech getTTS, Locale language, float soundVolume, String getString, UtteranceProgressListener utterance) {
        getTTS.setOnUtteranceProgressListener(utterance);
                        getTTS.setLanguage(language);
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, soundVolume / 100f);
        getTTS.speak(getString, TextToSpeech.QUEUE_ADD, params
                , TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }
}