package com.hwstudio.wordpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.provider.DocumentsContract;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
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
    private static final int REQUEST_WRITE_PERMISSION = 30;

    // Settings
    public static Locale[] language = new Locale[2];
    public static int wordDelay, lineDelay, repeatNum;
    public static boolean isRepeatAtEnd, hasListBackground;
    public static int[] speechRate = new int[2];
    public static int[] soundVolume = new int[2];
    public static int[] pitch = new int[2];
    public static int[] textSize = new int[2];

    // Variables
    private int repeatCount, currentLine, maxLine;
    private boolean isRepeating, isPlaying2ndLang, isListClicked;
    private int playingState;  // 0 = stopped; 1 = playing; 2 = paused
    public static TextToSpeech[] tts = new TextToSpeech[2];
    ListAdapter.ViewHolder[] viewHolder = new ListAdapter.ViewHolder[2];
    private UtteranceProgressListener[] utterance = new UtteranceProgressListener[2];
    private Handler delayHandler;
    public static String[] listString = new String[2];
    private String[] wordString = new String[2];
    MainViewModel model;

    // Views
    private EditText[] listEditText = new EditText[2];
    public Button[] langButton = new Button[2];
    private Button finishButton, editButton;
    private ImageButton playButton, pauseButton, stopButton, swapButton;
    private HorizontalScrollView[] recyclerScrollView = new HorizontalScrollView[2];
    private RecyclerView[] listRecyclerView = new RecyclerView[2];
    private ListAdapter[] listAdapter = new ListAdapter[2];
    private RecyclerView.LayoutManager[] layoutManager = new RecyclerView.LayoutManager[2];
    private RecyclerView[] bgRecyclerView = new RecyclerView[2];
    private BackgroundAdapter[] bgAdapter = new BackgroundAdapter[2];
    private RecyclerView.LayoutManager[] bgLayoutManager = new RecyclerView.LayoutManager[2];
    private ScrollView listScrollView;
    private ConstraintLayout mainConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSettings();
        initView();
        // Prepare UI
        model = new ViewModelProvider(this).get(MainViewModel.class);
        setMultipleEnable(model.isCanFinish(), model.isCanEdit(), model.isCanPlay(), model.isCanPause(), model.isCanStop());
        listString[0] = model.getListString0();
        listString[1] = model.getListString1();
        if (model.isHasRecycler()) {
            drawRecyclerView();
        }

        // Prepare action
        if (model.isChangingState()) {
            currentLine = model.getCurrentLine();
            maxLine = model.getMaxLine();
            repeatCount = model.getRepeatCount();
            playingState = model.getPlayingState();
            isRepeating = model.isRepeating();
            tts[0] = model.getTts0();
            tts[1] = model.getTts1();
            setUtterance();
            delayHandler = model.getDelayHandler();
            isPlaying2ndLang = model.isPlaying2ndLang();
            isListClicked = model.isListClicked();
            wordString[0] = model.getWordString0();
            wordString[1] = model.getWordString1();
            if (playingState > 0) {
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder[0] = (ListAdapter.ViewHolder) listRecyclerView[0].findViewHolderForAdapterPosition(currentLine);
                        viewHolder[1] = (ListAdapter.ViewHolder) listRecyclerView[1].findViewHolderForAdapterPosition(currentLine);
                        if (isPlaying2ndLang) {
                            viewHolder[1].highlightString();
                        } else {
                            viewHolder[0].highlightString();
                        }
                        scrollToWord();
//                        listScrollView.smoothScrollTo(0, mainConstraintLayout.getHeight() * (currentLine - 5) / maxLine);
                        if (playingState == 1) {
                            clickPlay(null);
                        }
                    }
                }, 100);
            }
        } else {
            initTTS(0);
            initTTS(1);
            setUtterance();
            delayHandler = new Handler();
        }
    }

    private void loadSettings() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
        wordDelay = sharedPref.getInt(getString(R.string.prefWordDelay), 0);
        lineDelay = sharedPref.getInt(getString(R.string.prefLineDelay), 1);
        repeatNum = sharedPref.getInt(getString(R.string.prefRepeatNum), 2);
        isRepeatAtEnd = sharedPref.getBoolean(getString(R.string.prefIsRepeatAtEnd), false);
        hasListBackground = sharedPref.getBoolean(getString(R.string.prefHasListBackground), false);
        speechRate[0] = sharedPref.getInt(getString(R.string.prefSpeechRate0), 3);
        speechRate[1] = sharedPref.getInt(getString(R.string.prefSpeechRate1), 3);
        soundVolume[0] = sharedPref.getInt(getString(R.string.prefSoundVolume0), 6);
        soundVolume[1] = sharedPref.getInt(getString(R.string.prefSoundVolume1), 6);
        pitch[0] = sharedPref.getInt(getString(R.string.prefPitch0), 3);
        pitch[1] = sharedPref.getInt(getString(R.string.prefPitch1), 3);
        textSize[0] = sharedPref.getInt(getString(R.string.prefTextSize0), 36);
        textSize[1] = sharedPref.getInt(getString(R.string.prefTextSize1), 36);
        language[0] = new Locale(sharedPref.getString(getString(R.string.prefLanguage0), "zh_CH_#Hans"));
        language[1] = new Locale(sharedPref.getString(getString(R.string.prefLanguage1), "en_US"));
    }

    private void initView() {
        listEditText[0] = findViewById(R.id.list0EditText);
        listEditText[1] = findViewById(R.id.list1EditText);
        listEditText[0].setTextSize(textSize[0]);
        listEditText[1].setTextSize(textSize[1]);
//        backgroundTextView[0] = findViewById(R.id.background0TextView);
//        backgroundTextView[1] = findViewById(R.id.background1TextView);
        listRecyclerView[0] = findViewById(R.id.list0RecyclerView);
        listRecyclerView[1] = findViewById(R.id.list1RecyclerView);
        listRecyclerView[0].setHasFixedSize(true);
        listRecyclerView[1].setHasFixedSize(true);
        bgRecyclerView[0] = findViewById(R.id.bg0RecyclerView);
        bgRecyclerView[1] = findViewById(R.id.bg1RecyclerView);
        bgRecyclerView[0].setHasFixedSize(true);
        bgRecyclerView[1].setHasFixedSize(true);
//        editTextScrollView[0] = findViewById(R.id.editText0ScrollView);
//        editTextScrollView[1] = findViewById(R.id.editText1ScrollView);
        recyclerScrollView[0] = findViewById(R.id.recycler0ScrollView);
        recyclerScrollView[1] = findViewById(R.id.recycler1ScrollView);
        langButton[0] = findViewById(R.id.lang0Button);
        langButton[1] = findViewById(R.id.lang1Button);
        finishButton = findViewById(R.id.finishButton);
        editButton = findViewById(R.id.editButton);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        swapButton = findViewById(R.id.swapButton);
        listScrollView = findViewById(R.id.mainScrollView);
        mainConstraintLayout = findViewById(R.id.mainConstraintLayout);
    }

    private void initTTS(int listNum) {
        tts[listNum] = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        setLanguageTtsButton(listNum);
        setSpeechRate(listNum);
        setPitch(listNum);
    }

    private void setUtterance() {
        utterance[0] = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                isPlaying2ndLang = false;
            }

            @Override
            public void onDone(String s) {
                if (playingState == 1) {
                    viewHolder[0].clearHighlight();
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
                if (playingState == 0) {
                    viewHolder[0].clearHighlight();
                    if (isListClicked) {
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isListClicked = false;
                                clickPlay(null);
                            }
                        }, lineDelay * 500);
                    }else {
                        currentLine=0;
                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        };
        utterance[1] = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                isPlaying2ndLang = true;
            }

            @Override
            public void onDone(String s) {
                if (playingState == 1) {
                    viewHolder[1].clearHighlight();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (repeatCount > 1) {
                                repeatCount--;
                                isRepeating = true;
                                speakString(0);
                            } else {
                                isRepeating = false;
                                repeatCount = repeatNum;
                                currentLine++;
                                if (currentLine >= listAdapter[0].getItemCount() || currentLine >= listAdapter[1].getItemCount()) { // Either list ended
                                    clickStop(null);
                                    Toast.makeText(MainActivity.this, R.string.endOfListToast, Toast.LENGTH_SHORT).show();
                                    MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.long_beep);
                                    mediaPlayer.start();
                                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            mediaPlayer.release();
                                            if (isRepeatAtEnd) {
                                                clickPlay(null);
                                            }
                                        }
                                    });
                                } else {
                                    pickWord(0);
                                }
                            }
                        }
                    }, lineDelay * 500);
                }
                if (playingState == 0) {
                    viewHolder[1].clearHighlight();
                    if (isListClicked) {
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isListClicked = false;
                                clickPlay(null);
                            }
                        }, lineDelay * 500);
                    }else {
                        currentLine=0;
                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        };
    }

    private void setMultipleEnable(boolean canFinish, boolean canEdit, boolean canPlay, boolean canPause, boolean canStop) {
        finishButton.setEnabled(canFinish);
        editButton.setEnabled(canEdit);
        playButton.setEnabled(canPlay);
        pauseButton.setEnabled(canPause);
        stopButton.setEnabled(canStop);
    }

    public void clickFinish(View view) {
        setMultipleEnable(false, true, true, false, false);
        currentLine = 0;
        playingState = 0;
        listString[0] = listEditText[0].getText().toString();
        listString[1] = listEditText[1].getText().toString();
        drawRecyclerView();
    }

    private void drawRecyclerView() {
        int[] lineCount = new int[2];
//        listString[0] = getListString0;
//        listString[1] = getListString1;
        for (int listNum = 0; listNum < 2; listNum++) {
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
            listAdapter[listNum] = new ListAdapter(tempString, textSize[listNum]);
            listRecyclerView[listNum].setAdapter(listAdapter[listNum]);
            if (hasListBackground) {
                bgLayoutManager[listNum] = new LinearLayoutManager(this);
                bgRecyclerView[listNum].setLayoutManager(bgLayoutManager[listNum]);
                bgAdapter[listNum] = new BackgroundAdapter(tempString, textSize[listNum]);
                bgRecyclerView[listNum].setAdapter(bgAdapter[listNum]);
                bgRecyclerView[listNum].setVisibility(View.VISIBLE);
            } else {
                bgRecyclerView[listNum].setVisibility(View.INVISIBLE);
            }
            listEditText[listNum].setVisibility(View.INVISIBLE);
            recyclerScrollView[listNum].setVisibility(View.VISIBLE);
            listAdapter[listNum].setOnWordClickedListener(new ListAdapter.OnWordClickedListener() {
                @Override
                public void onWordClicked(int position) {
                    if (!isListClicked) {
                        int currentState = playingState;
                        clickStop(null);
                        currentLine = position;
                        if (currentState == 1) {
                            isListClicked = true;
                        } else {
                            if (viewHolder[0] != null) {
                                viewHolder[0].clearHighlight();
                            }
                            if (viewHolder[1] != null) {
                                viewHolder[1].clearHighlight();
                            }
                            clickPlay(null);
                        }
                    }
                }
            });
        }
        maxLine = lineCount[0];
        if (lineCount[0] != lineCount[1]) {
            Toast.makeText(this, R.string.unequalLengthErr, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickEdit(View view) {
        setMultipleEnable(true, false, false, false, false);
        playingState = 0;
        for (int listNum = 0; listNum < 2; listNum++) {
            listEditText[listNum].setVisibility(View.VISIBLE);
            recyclerScrollView[listNum].setVisibility(View.INVISIBLE);
            bgRecyclerView[listNum].setVisibility(View.INVISIBLE);
        }
    }

    public void clickPlay(View view) {
        if (listEditText[0].getText().length() == 0 || listEditText[1].getText().length() == 0) {
            Toast.makeText(this, R.string.emptyListErr, Toast.LENGTH_SHORT).show();
            return;
        }
        setMultipleEnable(false, true, false, true, true);
//        listEditText[0].setEnabled(false);
//        listEditText[1].setEnabled(false);
        if (playingState == 2) {
            playingState = 1;
            if (isPlaying2ndLang) {
                utterance[1].onDone("");
            } else {
                utterance[0].onDone("");
            }
        } else {
            playingState = 1;
            for (int i = 0; i < 2; i++) {
                listString[i] = listEditText[i].getText().toString();
            }
            isRepeating = false;
            repeatCount = repeatNum;
            pickWord(0);
        }
    }

    private void pickWord(int listNum) {
        listRecyclerView[listNum].scrollToPosition(currentLine);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewHolder[listNum] = (ListAdapter.ViewHolder) listRecyclerView[listNum].findViewHolderForAdapterPosition(currentLine);
                wordString[listNum] = viewHolder[listNum].getText();
                speakString(listNum);
            }
        }, 100);
    }

    public void scrollToWord() {
        listScrollView.smoothScrollTo(0, mainConstraintLayout.getHeight() * (currentLine - 5) / maxLine);
    }

    public void setLanguageTtsButton(int listNum) {
        tts[listNum].setLanguage(language[listNum]);
        langButton[listNum].setText(language[listNum].getDisplayName());
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
        viewHolder[listNum].highlightString();
        scrollToWord();
//        listScrollView.smoothScrollTo(0, mainConstraintLayout.getHeight() * (currentLine - 5) / maxLine);
        tts[listNum].setOnUtteranceProgressListener(utterance[listNum]);
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, (soundVolume[listNum] + 1) / 7f);
        tts[listNum].speak(wordString[listNum], TextToSpeech.QUEUE_ADD, params
                , TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }

    public void clickPause(View view) {
        playingState = 2;
        setMultipleEnable(false, true, true, false, true);
    }

    public void clickStop(View view) {
        playingState = 0;
//        currentLine = 0;
        setMultipleEnable(false, true, true, false, false);
    }

    public void clickLang0(View view) {
        langButton[0].setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                langButton[0].setEnabled(true);
            }
        }, 100);
        LanguageDialogFragment lang0Fragment = new LanguageDialogFragment(this, getString(R.string.pickLanguage0Title));
        lang0Fragment.show(getSupportFragmentManager(), "lang0");
    }

    public void clickLang1(View view) {
        langButton[1].setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                langButton[1].setEnabled(true);
            }
        }, 100);
        LanguageDialogFragment lang1Fragment = new LanguageDialogFragment(this, getString(R.string.pickLanguage1Title));
        lang1Fragment.show(getSupportFragmentManager(), "lang1");
    }

    public void clickSwap(View view) {
        swapButton.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swapButton.setEnabled(true);
            }
        }, 100);
        swap(language);
        swap(listString);
        swapInt(speechRate);
        swapInt(soundVolume);
        swapInt(pitch);
        swapInt(textSize);
        for (int listNum = 0; listNum < 2; listNum++) {
            listEditText[listNum].setTextSize(textSize[listNum]);
            listEditText[listNum].setText(listString[listNum]);
            initTTS(listNum);
            setLanguageTtsButton(listNum);
            if (recyclerScrollView[0].getVisibility() == View.VISIBLE) {
                drawRecyclerView();
            }
        }
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.prefLanguage0), language[0].toString());
        editor.putString(getString(R.string.prefLanguage1), language[1].toString());
        editor.putInt(getString(R.string.prefSpeechRate0), speechRate[0]);
        editor.putInt(getString(R.string.prefSpeechRate1), speechRate[1]);
        editor.putInt(getString(R.string.prefSoundVolume0), soundVolume[0]);
        editor.putInt(getString(R.string.prefSoundVolume1), soundVolume[1]);
        editor.putInt(getString(R.string.prefPitch0), pitch[0]);
        editor.putInt(getString(R.string.prefPitch1), pitch[1]);
        editor.putInt(getString(R.string.prefTextSize0), textSize[0]);
        editor.putInt(getString(R.string.prefTextSize1), textSize[1]);
        editor.apply();
    }

    private void swap(Object[] object) {
        Object tempObject = object[0];
        object[0] = object[1];
        object[1] = tempObject;
    }

    private void swapInt(int[] object) {
        int tempObject = object[0];
        object[0] = object[1];
        object[1] = tempObject;
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
//            case R.id.loadLists:
//                clickLoad();
//                break;
//            case R.id.saveLists:
//                clickSave();
//                break;
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
            listEditText[0].setTextSize(textSize[0]);
            listEditText[1].setTextSize(textSize[1]);
            listEditText[0].setText(listString[0]);
            listEditText[1].setText(listString[1]);
            if (recyclerScrollView[0].getVisibility() == View.VISIBLE) {
                drawRecyclerView();
            }
        }
    }

    public void clickSave(View view) {
        // Check has permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            saveList();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);
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
        DialogFragment saveDialogFragment = new SaveDialogFragment(this);
        saveDialogFragment.show(getSupportFragmentManager(), "save");
    }

    public void saveFile(String fileName, Context context) {
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

    public void clickLoad(View view) {
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
        setLanguageTtsButton(0);
        String tempLang1 = loadedString.substring(index[2] + 12, index[3]);
        language[1] = new Locale(tempLang1);
        setLanguageTtsButton(1);
        String tempList0 = loadedString.substring(index[1] + 4, index[2] - 2);
        listEditText[0].setText(tempList0);
        String tempList1 = loadedString.substring(index[3] + 4, loadedString.length() - 1);
        listEditText[1].setText(tempList1);
        clickFinish(null);
//        addBackgroundSpan();
        return true;
    }

    private void loadExamples() {
        ExampleDialogFragment exampleDialogFragment = new ExampleDialogFragment(this);
        exampleDialogFragment.show(getSupportFragmentManager(), "example");
        //debug
        //   listEditText[0].setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
        //      listEditText[1].setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
        //listEditText[0].setText("1\n蘋果\n橙\n香蕉");
//        addBackgroundSpan(0);
        //listEditText[1].setText("1\napple\norange\nbanana");
//        addBackgroundSpan();
//        language[0] = Locale.JAPANESE;
        //       language[1] = new Locale("zh_hk");
        //      setLanguageTtsButton(0);
        //       setLanguageTtsButton(1);
        //       clickFinish(null);
    }

    public void showExample(Locale locale0, Locale locale1, String listString0, String listString1) {
        language[0] = locale0;
        language[1] = locale1;
        listString[0] = listString0;
        listString[1] = listString1;
        for (int listNum = 0; listNum < 2; listNum++) {
            setLanguageTtsButton(listNum);
            listEditText[listNum].setText(listString[listNum]);
        }
        if (recyclerScrollView[0].getVisibility() == View.VISIBLE) {
            drawRecyclerView();
        }
        clickFinish(null);
    }

    private void showAbout() {
        AboutDialogFragment aboutDialogFragment = new AboutDialogFragment();
        aboutDialogFragment.show(getSupportFragmentManager(), "about");
    }

    @Override
    protected void onDestroy() {
        // Save UI
        model.setCanFinish(finishButton.isEnabled());
        model.setCanEdit(editButton.isEnabled());
        model.setCanPlay(playButton.isEnabled());
        model.setCanPause(pauseButton.isEnabled());
        model.setCanStop(stopButton.isEnabled());
        model.setHasRecycler(recyclerScrollView[0].getVisibility() == View.VISIBLE);
        model.setListString0(listEditText[0].getText().toString());
        model.setListString1(listEditText[1].getText().toString());
        // Save action
        model.setCurrentLine(currentLine);
        model.setMaxLine(maxLine);
        model.setRepeatCount(repeatCount);
        model.setPlayingState(playingState);
        model.setRepeating(isRepeating);
        model.setTts0(tts[0]);
        model.setTts1(tts[1]);
        model.setDelayHandler(delayHandler);
        delayHandler.removeCallbacksAndMessages(null);
        model.setPlaying2ndLang(isPlaying2ndLang);
        model.setListClicked(isListClicked);
        model.setWordString0(wordString[0]);
        model.setWordString1(wordString[1]);
        model.setChangingState(true);
        if (playingState == 1) {
            clickPause(null);
        }
        super.onDestroy();
    }
}
