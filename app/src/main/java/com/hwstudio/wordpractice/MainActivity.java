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
    int wordDelay, lineDelay, repeatNum;
   

    // Variables
    TextToSpeech[] tts = new TextToSpeech[2];
    UtteranceProgressListener[] utterance = new UtteranceProgressListener[2];
    String[] listString = new String[2];
    String[] wordString = new String[2];
    int[] wordStart = new int[2];
    int[] wordEnd = new int[2];
    boolean isEnd = false;
    EditText lang0EditText, lang1EditText;
    Button lang0Button, lang1Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSettings();
        for (int i = 0; i < 2; i++){
            initVariable(i);
            initTTS(tts[i], pitch[i], speechRate[i]);
        }
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
        lang0EditText = findViewById(R.id.lang0EditText);
        lang1EditText = findViewById(R.id.lang1EditText);
        lang0Button = findViewById(R.id.lang0Button);
        lang1Button = findViewById(R.id.lang1Button);
        lang0Button.setText(language[0].toLanguageTag());
        lang1Button.setText(language[1].toLanguageTag());
    }

    private void loadSettings(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        wordDelay = sharedPref.getInt(getString(R.string.prefWordDelay), 0);
        lineDelay = sharedPref.getInt(getString(R.string.prefLineDelay), 1);
        repeatNum = sharedPref.getInt(getString(R.string.prefRepeatNum), 2);
        pitch[0] = sharedPref.getInt(getString(R.string.prefPitch0), 3);
        pitch[1] = sharedPref.getInt(getString(R.string.prefPitch1), 3);
        speechRate[0] = sharedPref.getInt(getString(R.string.prefSpeechRate0), 3);
        speechRate[1] = sharedPref.getInt(getString(R.string.prefSpeechRate1), 3);
        soundVolume[0] = sharedPref.getFloat(getString(R.string.prefSoundVolume0), 80);
        soundVolume[1] = sharedPref.getFloat(getString(R.string.prefSoundVolume1), 80);
        language[0] = Locale(sharedPref.getString(getString(R.string.prefLanguage0), "en");
        language[1] = Locale(sharedPref.getString(getString(R.string.prefLanguage1), "cn");
    }
    
    private void initVariable(int listNum) {
        wordStart[listNum] = 0;
        tts[listNum] = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
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
        listString[0] = lang0EditText.getText().toString(); 
        listString[1] = lang1EditText.getText().toString(); 
        pickWord(0);
    }

    private void pickWord(int listNum) {
        //boolean isEnd = false;
        wordEnd[listNum] = listString[listNum].indexOf(10, wordStart[listNum]);
        if (wordEnd[listNum] > -1) {
            wordString[listNum] = listString[listNum].substring(wordStart[listNum], wordEnd[listNum]);
        } else {
            wordString[listNum] = listString[listNum].substring(wordStart[listNum]);
            //if (listNum == 1) {
                isEnd = true;
            //}
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
    public boolean onCreateOptionsMenu(Menu getMenu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, getMenu);
   //     muteAlarm = getMenu.findItem(R.id.muteAlarm);
  //      comboTimer = getMenu.findItem(R.id.comboTimer);
   //     comboTimer.setVisible(false);
   //     medAlarm = getMenu.findItem(R.id.medicationAlarm);
   //     timeItem = getMenu.findItem(R.id.time);
    //    typeItem = getMenu.findItem(R.id.type);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.openSettings:
                intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, OPEN_SETTINGS);
                break;
            case R.id.loadExamples:
                loadExamples();
                break;
            /** case R.id.openHelpTips:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.rateApp:
                MobileService msRate = new MobileService();
                msRate.rateApp(this);
                break;
            case R.id.shareApp:
                MobileService msShare = new MobileService();
                msShare.shareApp(this);
                break;  **/
            case R.id.openAbout:
                showAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
                             
    private void loadExamples(){
        //debug
        lang0EditText.setText("apple\norange\nbanana");
        lang1EditText.setText("蘋果\n橙\n香蕉");
    }
     
    private void showAbout() {
        AboutDialogFragment aboutDialogFragment = new AboutDialogFragment();
        aboutDialogFragment.show(getSupportFragmentManager(), "about");
    }

    public static class AboutDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            PackageInfo packageInfo = null;
            try {
                packageInfo = getContext().getApplicationContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String s = String.format(Locale.ENGLISH, "%s v%s\nWakeman Chau\nhauwingstudio@hotmail.com\n© 2021\nAll rights reserved", getString(R.string.app_name), packageInfo.versionName);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.aboutMenuText)
                    .setMessage(s)
                    .setPositiveButton(R.string.okText, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }        
                             
    @Override
    protected void onDestroy() {
        tts[0].shutdown();
        tts[1].shutdown();
        super.onDestroy();
    }
}
