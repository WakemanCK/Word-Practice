package com.hwstudio.wordpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
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
    private static final int REQUEST_WRITE_PERMISSION = 30;

    // Settings
    public static Locale[] language = new Locale[2];
    public static int wordDelay, lineDelay, repeatNum;
    public static boolean hasListBackground;
    public static int[] speechRate = new int[2];
    public static int[] soundVolume = new int[2];
    public static int[] pitch = new int[2];
    public static int[] textSize = new int[2];

    // Variables
    private static final TextToSpeech[] tts = new TextToSpeech[2];
    private final UtteranceProgressListener[] utterance = new UtteranceProgressListener[2];
    private static String[] listString = new String[2];
    private String[] wordString = new String[2];
//    private SpannableString[] spanString = new SpannableString[2];
    private ForegroundColorSpan wordSpan;// = new ForegroundColorSpan[2];
    //    private boolean[] isSpanning = new boolean[2];
    //private int[] wordStart = new int[2];
    //private int[] wordEnd = new int[2];
    private int currentLine;
    private boolean isRepeating;
    private int playingState;  // 0 = stopped; 1 = playing; 2 = paused
    private int repeatCount;
    private final EditText[] listEditText = new EditText[2];
    private final Button[] langButton = new Button[2];
    private ImageButton playButton, pauseButton, rewindButton;
    private ScrollView mainScrollView;
    private RecyclerView[] listRecyclerView = new RecyclerView[2];
    private ListAdapter[] listAdapter = new ListAdapter[2];
    private RecyclerView.LayoutManager[] layoutManager = new RecyclerView.LayoutManager[2];
    //private TextView[] backgroundTextView = new TextView[2];
    private Handler delayHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSettings();
        for (int i = 0; i < 2; i++) {
            initVariable(i);
            initTTS(i);
        }
        initView();
        utterance[0] = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onDone(String s) {
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
                if (playingState == 1) {
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (repeatCount > 1) {
                                repeatCount--;
                                isRepeating = true;
                                speakString(0);
                            } else {
                            //    if (isEnd) {
                              //      clickStop(null);
                                //} else {
                                    isRepeating = false;
                                    repeatCount = repeatNum;
                                    currentLine++
                                        if (currentLine >= ListAdapter[0].getItemCount() || currentLine >= ListAdapter[1].getItemCount(){
                                            clickStop(null);
                                        } else{
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
    }

    private void loadSettings() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
        wordDelay = sharedPref.getInt(getString(R.string.prefWordDelay), 0);
        lineDelay = sharedPref.getInt(getString(R.string.prefLineDelay), 1);
        repeatNum = sharedPref.getInt(getString(R.string.prefRepeatNum), 2);
        hasListBackground = sharedPref.getBoolean(getString(R.string.prefHasListBackground), false);
        speechRate[0] = sharedPref.getInt(getString(R.string.prefSpeechRate0), 3);
        speechRate[1] = sharedPref.getInt(getString(R.string.prefSpeechRate1), 3);
        soundVolume[0] = sharedPref.getInt(getString(R.string.prefSoundVolume0), 6);
        soundVolume[1] = sharedPref.getInt(getString(R.string.prefSoundVolume1), 6);
        pitch[0] = sharedPref.getInt(getString(R.string.prefPitch0), 3);
        pitch[1] = sharedPref.getInt(getString(R.string.prefPitch1), 3);
        textSize[0] = sharedPref.getInt(getString(R.string.prefTextSize0), 36);
        textSize[1] = sharedPref.getInt(getString(R.string.prefTextSize1), 36);
        language[0] = new Locale(sharedPref.getString(getString(R.string.prefLanguage0), "zh_CN"));
        language[1] = new Locale(sharedPref.getString(getString(R.string.prefLanguage1), "en_US"));
    }

    private void initVariable(int listNum) {
        //wordStart[listNum] = 0;
        //currentLine = 0;
        tts[listNum] = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        delayHandler = new Handler();
        wordSpan = new ForegroundColorSpan(Color.RED);
//        wordSpan[1] = new ForegroundColorSpan(Color.RED);
    }

    private void initTTS(int listNum) {
        setLanguage(listNum);
        setSpeechRate(listNum);
        setPitch(listNum);
    }

    private void initView() {
        listEditText[0] = findViewById(R.id.list0EditText);
        listEditText[1] = findViewById(R.id.list1EditText);
        listEditText[0].setTextSize(textSize[0]);
        listEditText[1].setTextSize(textSize[1]);
//        backgroundTextView[0] = findViewById(R.id.background0TextView);
//        backgroundTextView[1] = findViewById(R.id.background1TextView);
        listRecyclerView[0]=findViewById(R.id.list0RecyclerView);
        listRecyclerView[1]=findViewById(R.id.list1RecyclerView);
        listRecyclerView[0].setHasFixedSize(true);
        listRecyclerView[1].setHasFixedSize(true);
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
 /*       listEditText[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listString[0] = editable.toString();
            }
        });
        listEditText[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listString[1] = editable.toString();
            }
        });*/
//        addBackgroundSpan();
    }

    public void clickFinish(View view){
        int[] lineCount = new int[2];
        for (int listNum = 0; listNum < 2; listNum++) {
            if (listString[listNum] != null) {
                String tempList = listString[listNum].replaceAll("\n", "");
                lineCount[listNum] = listString[listNum].length() - tempList.length();
                String[] tempString = new String[lineCount[listNum] + 1];
                int wordStart = 0, wordEnd;
                for (int i = 0; i < lineCount[listNum]; i++) {
                    wordEnd = listString[listNum].indexOf(10, wordStart);
                    tempString[i] = listString[listNum].substring(wordStart, wordEnd);
                    wordStart = wordEnd + 1;
                }
                tempString[lineCount[listNum]] = listString[listNum].substring(wordStart);
                layoutManager[listNum] = new LinearLayoutManager(this);
                listRecyclerView[listNum].setLayoutManager(layoutManager[listNum]);
                listAdapter[listNum] = new ListAdapter(tempString);
                listRecyclerView[listNum].setAdapter(listAdapter[listNum]);
                listRecyclerView[listNum].setVisibility(View.VISIBLE);
                listEditText[listNum].setVisibility(View.INVISIBLE);
                findViewById(R.id.list1EditText).setVisibility(View.INVISIBLE);
            }
        }
        if (lineCount[0] != lineCount[1]) {
            Toast.makeText(this, R.string.unequalLengthErr, Toast.LENGTH_SHORT).show();
        }
    }
//
//    private void addBackgroundSpan() {
//        int[] lineCount = new int[2];
//        for (int listNum = 0; listNum < 2; listNum++) {
//            if (listString[listNum] != null) {
//                String tempList = listString[listNum].replaceAll("\n", "");
//                lineCount[listNum] = listString[listNum].length() - tempList.length() + 1;
//                if (hasListBackground) {
//                    StringBuilder spaceString = new StringBuilder("                    ");
//                    for (int i = 0; i < 48 - textSize[listNum]; i++) {
//                        spaceString.append(" ");
//                    }
//                    spaceString.append("\n");
//                    SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
//                    int[] color = new int[5];
//                    color[0] = R.color.gray0;
//                    color[1] = R.color.gray1;
//                    color[2] = R.color.gray2;
//                    color[3] = R.color.gray3;
//                    color[4] = R.color.gray4;
//                    int k = 0;
//                    for (int j = 0; j < lineCount[listNum]; j++) {
//                        spanBuilder.append(spaceString, new BackgroundColorSpan(getResources().getColor(color[k])), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                        k++;
//                        if (k > 4) {
//                            k = 0;
//                        }
//                    }
//                    backgroundTextView[listNum].setTextSize(textSize[listNum]);
//                    backgroundTextView[listNum].setText(spanBuilder);
//                } else {
//                    backgroundTextView[listNum].setText("");
//                }
//            }
//        }

//    }

    public void clickPlay(View view) {
        if (listEditText[0].getText().length() == 0 || listEditText[1].getText().length() == 0) {
            Toast.makeText(this, R.string.emptyListErr, Toast.LENGTH_SHORT).show();
            return;
        }
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        rewindButton.setEnabled(true);
        listEditText[0].setEnabled(false);
        listEditText[1].setEnabled(false);
        if (playingState == 2) {
            playingState = 1;
            utterance[1].onDone("");
        } else {
            playingState = 1;
            for (int i = 0; i < 2; i++) {
                listString[i] = listEditText[i].getText().toString();
                wordEnd[i] = -1;
            }
         //   isEnd = false;
            isRepeating = false;
            repeatCount = repeatNum;
//            addBackgroundSpan();
//            spanString[0] = new SpannableString(listString[0]);
//            spanString[1] = new SpannableString(listString[1]);
            pickWord(0);
        }
    }

    private void pickWord(int listNum) {
       /* wordStart[listNum] = wordEnd[listNum] + 1;
        wordEnd[listNum] = listString[listNum].indexOf(10, wordStart[listNum]);
        if (wordEnd[listNum] == -1) {
            wordEnd[listNum] = listString[listNum].length();
            isEnd = true;
        }
        wordString[listNum] = listString[listNum].substring(wordStart[listNum], wordEnd[listNum]);*/
        wordString[listNum] = listRecyclerView[listNum].findViewHolderForAdapterPosition(currentLine).getText();
        speakString(listNum);
    }

    private void setLanguage(int listNum) {
        tts[listNum].setLanguage(language[listNum]);
    }

    public static void setSpeechRate(int listNum) {
        float[] speechRateFloat = {0.1f, 0.4f, 0.75f, 1f, 1.5f, 2f, 2.5f};
        tts[listNum].setSpeechRate(speechRateFloat[speechRate[listNum]]);
    }

    public static void setPitch(int listNum) {
        float[] pitchFloat = {0.4f, 0.6f, 0.8f, 1f, 1.5f, 2f, 2.5f};
        tts[listNum].setPitch(pitchFloat[pitch[listNum]]);
    }

    private void speakString(int listNum) {
        tts[listNum].setOnUtteranceProgressListener(utterance[listNum]);
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, (soundVolume[listNum] + 1) / 7f);
        tts[listNum].speak(wordString[listNum], TextToSpeech.QUEUE_ADD, params
                , TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        // Highlight word
////        isSpanning[listNum] = true;
//        spanString[listNum].setSpan(wordSpan, wordStart[listNum]
//                , wordEnd[listNum], Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        listEditText[listNum].setText(spanString[listNum]);
////        isSpanning[listNum] = false;
//        if (listNum == 0) {
//            spanString[1].removeSpan(wordSpan);
//            listEditText[1].setText(spanString[1]);
//        } else {
//            spanString[0].removeSpan(wordSpan);
//            listEditText[0].setText(spanString[0]);
//        }
//        mainScrollView.smoothScrollTo(0, listEditText[listNum].getHeight() * wordStart[listNum] / listEditText[listNum].length());
    }

    public void clickPause(View view) {
        playingState = 2;
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        rewindButton.setEnabled(true);
        listEditText[0].setEnabled(false);
        listEditText[1].setEnabled(false);
    }

    public void clickStop(View view) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                if (!analyzeFileSuccess(loadedString.toString())) {
                    Toast.makeText(this, R.string.fileLoadErr, Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == OPEN_SETTINGS) {
//            Toast.makeText(this, "debug saving", Toast.LENGTH_SHORT).show();
            //  saveSettings();
            listEditText[0].setTextSize(textSize[0]);
            listEditText[1].setTextSize(textSize[1]);
//            addBackgroundSpan();
        }
    }

//    private void saveSettings() {
//        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(getString(R.string.prefWordDelay), wordDelay);
//        editor.putInt(getString(R.string.prefLineDelay), lineDelay);
//        editor.putInt(getString(R.string.prefRepeatNum), repeatNum);
//        editor.putBoolean(getString(R.string.prefHasListBackground), hasListBackground);
//        editor.putInt(getString(R.string.prefSpeechRate0), speechRate[0]);
//        editor.putInt(getString(R.string.prefSpeechRate1), speechRate[1]);
//        editor.putFloat(getString(R.string.prefSoundVolume0), soundVolume[0]);
//        editor.putFloat(getString(R.string.prefSoundVolume1), soundVolume[1]);
//        editor.putInt(getString(R.string.prefPitch0), pitch[0]);
//        editor.putInt(getString(R.string.prefPitch1), pitch[1]);
//        editor.putInt(getString(R.string.prefTextSize0), textSize[0]);
//        editor.putInt(getString(R.string.prefTextSize1), textSize[1]);
//        editor.apply();
//        // Update text size and background span
//        listEditText[0].setTextSize(textSize[0]);
//        listEditText[1].setTextSize(textSize[1]);
//        addBackgroundSpan();
//    }

    private void clickSave() {
        // Check has permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            saveList();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);  // Request Code 30 = get storage permission
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION) {
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
        String saveString = // Format:   !LANGUAGE_1! Locale name here !1!
                "!LANGUAGE_1!" + language[0].toString() + "!1!\n" + listString[0] +
                        "\n\n!LANGUAGE_2!" + language[1].toString() + "!2!\n" + listString[1];
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

    private boolean analyzeFileSuccess(String loadedString) {
        int[] index = new int[4];
        index[0] = loadedString.indexOf("!LANGUAGE_1!");
        index[1] = loadedString.indexOf("!1!");
        index[2] = loadedString.indexOf("!LANGUAGE_2!");
        index[3] = loadedString.indexOf("!2!");
        for (int i = 0; i < 4; i++) {
            if (index[i] == -1) {
                return false;
            }
        }
        String tempLang0 = loadedString.substring(index[0] + 12, index[1]);
        language[0] = new Locale(tempLang0);
        setLanguage(0);
        langButton[0].setText(tempLang0);
        String tempLang1 = loadedString.substring(index[2] + 12, index[3]);
        language[1] = new Locale(tempLang1);
        setLanguage(1);
        langButton[1].setText(tempLang1);
        String tempList0 = loadedString.substring(index[1] + 4, index[2] - 2);
        listEditText[0].setText(tempList0);
        String tempList1 = loadedString.substring(index[3] + 4, loadedString.length() - 1);
        listEditText[1].setText(tempList1);
//        addBackgroundSpan();
        return true;
    }

    private void loadExamples() {
        //debug
        listEditText[0].setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
        listEditText[1].setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
        //listEditText[0].setText("1\n蘋果\n橙\n香蕉");
//        addBackgroundSpan(0);
        //listEditText[1].setText("1\napple\norange\nbanana");
//        addBackgroundSpan();
        language[0] = Locale.JAPANESE;
        language[1] = new Locale("zh_hk");
        setLanguage(0);
        setLanguage(1);
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
