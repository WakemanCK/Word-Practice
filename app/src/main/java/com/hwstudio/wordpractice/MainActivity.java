package com.hwstudio.wordpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int OPEN_SETTINGS = 10;
    private static final int LOAD_FILE = 11;
    // Settings
    public static Locale[] language = new Locale[2];
    private int[] speechRate = new int[2];
    private float[] soundVolume = new float[2];
    private int[] pitch = new int[2];
    private int wordDelay, lineDelay, repeatNum, repeatCount;

    // Variables
    private final TextToSpeech[] tts = new TextToSpeech[2];
    private final UtteranceProgressListener[] utterance = new UtteranceProgressListener[2];
    private static String[] listString = new String[2];
    private String[] wordString = new String[2];
    private int[] wordStart = new int[2];
    private int[] wordEnd = new int[2];
    private boolean isEnd, isRepeating;
    private int playingState;  // 0 = stopped; 1 = playing; 2 = paused
    private final EditText[] listEditText = new EditText[2];
    private final Button[] langButton = new Button[2];
    private ImageButton playButton, pauseButton, rewindButton;
    private ScrollView mainScrollView;
    private Handler delayHandler;
//    SaveDialogFragment saveDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSettings();
        for (int i = 0; i < 2; i++) {
            initVariable(i);
            initTTS(tts[i], pitch[i], speechRate[i]);
        }
//        saveDialogFragment = new SaveDialogFragment();
        utterance[0] = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
//                delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRepeating) {
                            speakString(1);
                        } else {
                            pickWord(1);
                        }
                    }
                }, wordDelay * 500);
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
//                delayHandler = new Handler();
                if (playingState == 1){
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (repeatCount > 1) {
                            repeatCount--;
                            isRepeating = true;
                            speakString(0);
                        } else {
                            if (isEnd) {
                            clickStop(null);
                            }else{
                                isRepeating = false;
                                repeatCount = repeatNum;
                                pickWord(0);
                            }
                        }
                    }
                }, lineDelay * 500);
              }
            }

            @Override
            public void onError(String s) {

            }
        };
        listEditText[0] = findViewById(R.id.lang0EditText);
        listEditText[1] = findViewById(R.id.lang1EditText);
//        listEditText[0].setEllipsize(TextUtils.TruncateAt.MIDDLE);
//        listEditText[0].setEllipsize(TextUtils.TruncateAt.START);
        langButton[0] = findViewById(R.id.lang0Button);
        langButton[1] = findViewById(R.id.lang1Button);
        langButton[0].setText(language[0].getDisplayLanguage());
        langButton[1].setText(language[1].getDisplayLanguage());
        mainScrollView = findViewById(R.id.mainScrollView);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        rewindButton = findViewById(R.id.stopButton);
        pauseButton.setEnabled(false);
        rewindButton.setEnabled(false);
    }

    private void loadSettings() {
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
        language[0] = new Locale(sharedPref.getString(getString(R.string.prefLanguage0), "zh_CN"));
        language[1] = new Locale(sharedPref.getString(getString(R.string.prefLanguage1), "en_US"));
    }

    private void initVariable(int listNum) {
        wordStart[listNum] = 0;
        tts[listNum] = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        delayHandler = new Handler();
    }

    public void initTTS(TextToSpeech getTTS, int getPitch, int getSpeechRate) {
        float[] pitchFloat = {0.4f, 0.6f, 0.8f, 1f, 1.5f, 2f, 2.5f};
        getTTS.setPitch(pitchFloat[getPitch]);
        float[] speechRateFloat = {0.1f, 0.4f, 0.75f, 1f, 1.5f, 2f, 2.5f};
        getTTS.setSpeechRate(speechRateFloat[getSpeechRate]);
    }

    private void clickSave() {
        // Check has permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            saveList();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    30);  // Request Code 30 = get storage permission
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 30) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveList();
            } else {
                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.noPermissionErr), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveList() {
        listString[0] = listEditText[0].getText().toString();
        listString[1] = listEditText[1].getText().toString();
        DialogFragment saveDialogFragment = new SaveDialogFragment();
        saveDialogFragment.show(getSupportFragmentManager(), "save");
    }

    public static class SaveDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            final EditText saveEditText = new EditText(getActivity());
            alertBuilder.setMessage(getString(R.string.saveDialogTextView))
                    .setView(saveEditText)
                    .setPositiveButton(getString(R.string.saveButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String s = saveEditText.getText().toString();
                            if (s.equals("")) {
                                s = "WordPracticeList";
                            }
                            saveFile(s, getActivity());
                        }
                    })
                    .setNegativeButton(getString(R.string.cancelButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            return alertBuilder.create();
        }
    }

    public static void saveFile(String fileName, Context context) {
        String saveString = // Format:   !LANGUAGE_0! Locale name here !0!
                "!LANGUAGE_0!" + language[0].toString() + "!0!\n" + listString[0] +
                        "\n\n!LANGUAGE_1!" + language[1].toString() + "!1!\n" + listString[1];
        int i = 0;
        if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .getPath() + "/" + fileName + ".txt").exists()) {
            do {
                i++;
            } while (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .getPath() + "/" + fileName + i + ".txt").exists());
            fileName = fileName + i;
        }
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .getPath() + "/" + fileName + ".txt");
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(outputFile)));
            out.writeUTF(saveString);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, String.format(context.getString(R.string.saveListText), fileName), Toast.LENGTH_LONG).show();
    }

    private void clickLoad() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
        startActivityForResult(intent, LOAD_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Uri fileUri;
        if (requestCode == LOAD_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                StringBuilder loadedString = new StringBuilder();
                try (InputStream inputStream = getContentResolver().openInputStream(fileUri);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        loadedString.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (analyzeFileSuccess(loadedString.toString())) {
                    Toast.makeText(this, R.string.fileLoadedText, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.fileLoadErr, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean analyzeFileSuccess(String loadedString) {
        int[] index = new int[4];
        index[0] = loadedString.indexOf("!LANGUAGE_0!");
        index[1] = loadedString.indexOf("!0!");
        index[2] = loadedString.indexOf("!LANGUAGE_1!");
        index[3] = loadedString.indexOf("!1!");
        for (int i = 0; i < 4; i++) {
            if (index[i] == -1) {
                return false;
            }
        }
        String tempLang0 = loadedString.substring(index[0] + 12, index[1]);
        language[0] = new Locale(tempLang0);
        langButton[0].setText(tempLang0);
        String tempLang1 = loadedString.substring(index[2] + 12, index[3]);
        language[1] = new Locale(tempLang1);
        langButton[1].setText(tempLang1);
        String tempList0 = loadedString.substring(index[1] + 4, index[2]-2);
        listEditText[0].setText(tempList0);
        String tempList1 = loadedString.substring(index[3] + 4, loadedString.length()-1);
        listEditText[1].setText(tempList1);
        return true;
    }

    public void clickPlay(View view) {
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        rewindButton.setEnabled(true);
        listEditText[0].setEnabled(false);
        listEditText[1].setEnabled(false);
        if (playingState == 2){
            playingState = 1;
            utterance[1].onDone("");
        }else{
            playingState = 1;
        listString[0] = listEditText[0].getText().toString();
        listString[1] = listEditText[1].getText().toString();
        wordEnd[0] = -1;
        wordEnd[1] = -1;
        isEnd = false;
        isRepeating = false;
        repeatCount = repeatNum;
        pickWord(0);
        }
    }

    private void pickWord(int listNum) {
        wordStart[listNum] = wordEnd[listNum] + 1;
        wordEnd[listNum] = listString[listNum].indexOf(10, wordStart[listNum]);
        if (wordEnd[listNum] == -1) {
            wordEnd[listNum] = listString[listNum].length();
            isEnd = true;
        }
        wordString[listNum] = listString[listNum].substring(wordStart[listNum], wordEnd[listNum]);
        speakString(listNum);
    }


    public void speakString(int listNum) {
        tts[listNum].setOnUtteranceProgressListener(utterance[listNum]);
        tts[listNum].setLanguage(language[listNum]);
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, soundVolume[listNum] / 100f);
        tts[listNum].speak(wordString[listNum], TextToSpeech.QUEUE_ADD, params
                , TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        // Highlight word
        Spannable wordSpan = new SpannableString(listString[listNum]);
        wordSpan.setSpan(new ForegroundColorSpan(Color.RED), wordStart[listNum]
                         ,wordEnd[listNum], Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        listEditText[listNum].setText(wordSpan);
        if (listNum==0){
            listEditText[1].setText(listString[1]);
        }else{
            listEditText[0].setText(listString[0]);
        }
        mainScrollView.smoothScrollTo(0, listEditText[listNum].getHeight()*wordStart[listNum]/listEditText[listNum].length());
    }

    public void clickPause(View view){
        playingState = 2;
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        rewindButton.setEnabled(true);
        listEditText[0].setEnabled(false);
        listEditText[1].setEnabled(false);
    }

    public void clickStop(View view){
        playingState = 0;
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        rewindButton.setEnabled(false);
        listEditText[0].setEnabled(true);
        listEditText[1].setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu getMenu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, getMenu);
        //     muteAlarm = getMenu.findItem(R.id.muteAlarm);
        //      comboTimer = getMenu.findItem(R.id.comboTimer);
        //     comboTimer.setVisible(false);
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
            case R.id.loadLists:
                clickLoad();
                break;
            case R.id.saveLists:
                clickSave();
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

    private void loadExamples() {
        //debug
        listEditText[0].setText("1\n蘋果\n橙\n香蕉");
        listEditText[1].setText("1\napple\norange\nbanana");
        langButton[0].setText(language[0].getDisplayLanguage());
        langButton[1].setText(language[1].getDisplayLanguage());
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
            builder.setTitle(R.string.aboutMenu)
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
        if (tts[0] != null) {
            tts[0].shutdown();
        }
        if (tts[1] != null) {
            tts[1].shutdown();
        }
        if (delayHandler != null) {
            delayHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
