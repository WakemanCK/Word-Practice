/*  Word Practice v1.01
Wakeman Chau
hauwingstudio@hotmail.com
© 2021
All rights reserved
*/

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
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.provider.DocumentsContract;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int OPEN_SETTINGS = 10;
    private static final int LOAD_FILE = 11;
    private static final int REQUEST_WRITE_PERMISSION = 30;

    // Settings
    public static Locale[] language = new Locale[2];
    public static int wordDelay, lineDelay, repeatNum, repeatAtEnd;
    public static Set<String> selectedFilenames = new HashSet<>();
    public static boolean hasListBackground, isSingleLineMode, lockOrientation;
    public static int[] speechRate = new int[2];
    public static int[] soundVolume = new int[2];
    public static int[] pitch = new int[2];
    public static int[] textSize = new int[2];

    // Variables
    private String defaultFile, currentFile = "";
    private int repeatCount, currentLine, maxLine, originalColor, playOrientation;
    private boolean isRepeating, isPlaying2ndLang, isListClicked;
    public static int playingState;  // 0 = stopped; 1 = playing; 2 = paused
    public static TextToSpeech[] tts = new TextToSpeech[2];
    private ListAdapter.ViewHolder[] viewHolder = new ListAdapter.ViewHolder[2];
    private UtteranceProgressListener[] utterance = new UtteranceProgressListener[2];
    private Handler delayHandler;
    public static String[] listString = new String[2];
    private List<String>[] lineString = new List[2];
    private String[] wordString = new String[2];
    public static MainViewModel model;

    // Views
    private ScrollView mainRecyclerScrollView, mainEditTextScrollView;
    private TextView dummyRecyclerTextView;
    private TextView[] floatingTextView = new TextView[2];
    private ConstraintLayout floatingConstraintLayout;
    private EditText[] listEditText = new EditText[2];
    private Button[] langButton = new Button[2];
    private Button finishButton, editButton, saveButton, loadButton;
    private ImageButton playButton, pauseButton, stopButton, swapButton;
    private RecyclerView[] listRecyclerView = new RecyclerView[2];
    private ListAdapter[] listAdapter = new ListAdapter[2];
    private RecyclerView.LayoutManager[] layoutManager = new RecyclerView.LayoutManager[2];
    private RecyclerView[] bgRecyclerView = new RecyclerView[2];
    private BackgroundAdapter[] bgAdapter = new BackgroundAdapter[2];
    private RecyclerView.LayoutManager[] bgLayoutManager = new RecyclerView.LayoutManager[2];
    private MenuItem displayModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load banner ad
        MobileService mainAd = new MobileService();
        FrameLayout adContainerView = findViewById(R.id.adFrameLayout);
        Display display = getWindowManager().getDefaultDisplay();
        mainAd.initBanner(this, adContainerView, display);

        // Init
        loadSettings();
        initView();
        model = new ViewModelProvider(this).get(MainViewModel.class);
        model.setMainActivity(this);
        setMultipleEnable(model.isCanFinish(), model.isCanEdit(), model.isCanPlay(), model.isCanPause(), model.isCanStop());
        listString[0] = model.getListString0();
        listString[1] = model.getListString1();
        if (model.isHasRecycler()) {
            drawRecyclerView();
        }
        if (isSingleLineMode) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    displayModeButton.setIconTintMode(PorterDuff.Mode.XOR);
                }
            }, 100);
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
            currentFile = model.getCurrentFile();
            Handler handler = new Handler();
            if (playingState > 0) {
                if (isSingleLineMode) {
                    floatingConstraintLayout.setVisibility(View.VISIBLE);
                }
                handler.postDelayed(() -> {
                    viewHolder[0] = (ListAdapter.ViewHolder) listRecyclerView[0].findViewHolderForAdapterPosition(currentLine);
                    viewHolder[1] = (ListAdapter.ViewHolder) listRecyclerView[1].findViewHolderForAdapterPosition(currentLine);
                    if (isPlaying2ndLang) {
                        viewHolder[1].highlightString();
                        scrollToWord(1);
                        floatingTextView[1].setTextColor(Color.RED);
                    } else {
                        viewHolder[0].highlightString();
                        scrollToWord(0);
                        floatingTextView[0].setTextColor(Color.RED);
                    }
                    if (playingState == 1) {
                        clickPlay();
                    }
                }, 100);
            }
            Handler handler2 = new Handler();
            handler2.postDelayed(() -> {
                String s = language[0].getDisplayName().replace("(", "\n(");
                langButton[0].setText(s);
                s = language[1].getDisplayName().replace("(", "\n(");
                langButton[1].setText(s);
            }, 500);
        } else {
            initTTS(0);
            initTTS(1);
            ((AudioManager) getSystemService(AUDIO_SERVICE))
                    .registerMediaButtonEventReceiver(new ComponentName(this, ButtonReceiver.class));
            ButtonReceiver.initActivity(this);
            if (defaultFile.startsWith("!NULL!")) {
                showIntroduction();
            } else if (defaultFile.startsWith("!LIST!")) {
                ExampleDialogFragment exampleDialogFragment = new ExampleDialogFragment();
                exampleDialogFragment.newInstance();
                exampleDialogFragment.chooseExample(Integer.parseInt(defaultFile.substring(6)));
            } else if (!loadFile(Uri.parse(defaultFile))) {
                if (!loadSavedFile(defaultFile)) {
                    showIntroduction();
                }
            }
            setUtterance();
            delayHandler = new Handler();
        }

        // Added to hide keyboard
        final View activityRootView = findViewById(R.id.recyclerScrollView);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            final int rootViewHeight = activityRootView.getHeight();
            activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                if (rootViewHeight - activityRootView.getHeight() > 100) {
                    findViewById(R.id.mainDummyTextView).setVisibility(View.VISIBLE);
                }
            });
        }, 100);
    }

    public void mainCloseKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e3) {
            e3.printStackTrace();
        }
        findViewById(R.id.mainDummyTextView).setVisibility(View.INVISIBLE);
    }

    public static class ButtonReceiver extends BroadcastReceiver {
        private static MainActivity activity;

        public static void initActivity(MainActivity getActivity) {
            activity = getActivity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent == null) {
                return;
            }
            int action = keyEvent.getAction();
            if (action == KeyEvent.ACTION_UP || action == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (playingState == 1) {
                    activity.clickPause(null);
                } else {
                    activity.clickPlay();
                }
            }
        }
    }

    private void showIntroduction() {
        IntroDialogFragment introDialogFragment = new IntroDialogFragment();
        introDialogFragment.show(getSupportFragmentManager(), "intro");
        setLanguageTtsButton(0);
        setLanguageTtsButton(1);
    }

    private void loadSettings() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
        wordDelay = sharedPref.getInt(getString(R.string.prefWordDelay), 0);
        lineDelay = sharedPref.getInt(getString(R.string.prefLineDelay), 1);
        repeatNum = sharedPref.getInt(getString(R.string.prefRepeatNum), 3);
        repeatAtEnd = sharedPref.getInt(getString(R.string.prefRepeatAtEnd), 1);
        selectedFilenames = sharedPref.getStringSet(getString(R.string.prefSelectedFiles), null);
        hasListBackground = sharedPref.getBoolean(getString(R.string.prefHasListBackground), false);
        isSingleLineMode = sharedPref.getBoolean(getString(R.string.prefIsSingleLineMode), false);
        lockOrientation = sharedPref.getBoolean(getString(R.string.prefLockOrientation), true);
        speechRate[0] = sharedPref.getInt(getString(R.string.prefSpeechRate0), 3);
        speechRate[1] = sharedPref.getInt(getString(R.string.prefSpeechRate1), 3);
        soundVolume[0] = sharedPref.getInt(getString(R.string.prefSoundVolume0), 6);
        soundVolume[1] = sharedPref.getInt(getString(R.string.prefSoundVolume1), 6);
        pitch[0] = sharedPref.getInt(getString(R.string.prefPitch0), 3);
        pitch[1] = sharedPref.getInt(getString(R.string.prefPitch1), 3);
        textSize[0] = sharedPref.getInt(getString(R.string.prefTextSize0), 36);
        textSize[1] = sharedPref.getInt(getString(R.string.prefTextSize1), 36);
        language[0] = Locale.forLanguageTag(sharedPref.getString(getString(R.string.prefLanguage0), "zh-CN"));
        language[1] = Locale.forLanguageTag(sharedPref.getString(getString(R.string.prefLanguage1), "en-US"));
        defaultFile = sharedPref.getString(getString(R.string.prefDefaultFile), "!NULL!");
    }

    private void initView() {
        mainRecyclerScrollView = findViewById(R.id.recyclerScrollView);
        mainEditTextScrollView = findViewById(R.id.editTextScrollView);
        dummyRecyclerTextView = findViewById(R.id.dummyRecyclerTextView);
        floatingTextView[0] = findViewById(R.id.lang0FloatingTextView);
        floatingTextView[1] = findViewById(R.id.lang1FloatingTextView);
        floatingTextView[0].setTextSize(textSize[0]);
        floatingTextView[1].setTextSize(textSize[1]);
        originalColor = floatingTextView[0].getCurrentTextColor();
        floatingConstraintLayout = findViewById(R.id.floatingConstraintLayout);
        listEditText[0] = findViewById(R.id.list0EditText);
        listEditText[1] = findViewById(R.id.list1EditText);
        listEditText[0].setTextSize(textSize[0]);
        listEditText[1].setTextSize(textSize[1]);
        listRecyclerView[0] = findViewById(R.id.list0RecyclerView);
        listRecyclerView[1] = findViewById(R.id.list1RecyclerView);
        listRecyclerView[0].setHasFixedSize(true);
        listRecyclerView[1].setHasFixedSize(true);
        bgRecyclerView[0] = findViewById(R.id.bg0RecyclerView);
        bgRecyclerView[1] = findViewById(R.id.bg1RecyclerView);
        bgRecyclerView[0].setHasFixedSize(true);
        bgRecyclerView[1].setHasFixedSize(true);
        langButton[0] = findViewById(R.id.lang0Button);
        langButton[1] = findViewById(R.id.lang1Button);
        finishButton = findViewById(R.id.finishButton);
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        swapButton = findViewById(R.id.swapButton);
    }

    private void initTTS(int listNum) {
        tts[listNum] = new TextToSpeech(this, i -> setLanguageTtsButton(listNum));
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
                    if (viewHolder[0] != null) {
                        viewHolder[0].clearHighlight();
                    }
                    floatingTextView[0].setTextColor(originalColor);
                    delayHandler.postDelayed(() -> {
                        if (isRepeating) {
                            speakString(1);
                        } else {
                            pickWord(1);
                        }
                    }, wordDelay * 500);
                }
                if (playingState == 0) {
                    if (viewHolder[0] != null) {
                        viewHolder[0].clearHighlight();
                    }
                    floatingTextView[0].setTextColor(originalColor);
                    if (isListClicked) {
                        delayHandler.postDelayed(() -> {
                            isListClicked = false;
                            clickPlay();
                        }, lineDelay * 500);
                    } else {
                        currentLine = 0;
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
                    if (viewHolder[0] != null) {
                        viewHolder[1].clearHighlight();
                    }
                    floatingTextView[1].setTextColor(originalColor);
                    delayHandler.postDelayed(() -> {
                        if (repeatCount > 1) {
                            repeatCount--;
                            isRepeating = true;
                            speakString(0);
                        } else {
                            isRepeating = false;
                            repeatCount = repeatNum;
                            currentLine++;
                            if (currentLine >= listAdapter[0].getItemCount() || currentLine >= listAdapter[1].getItemCount()) { // Either list ended
                                clickStop();
                                currentLine = 0;
                                scrollToWord(1);
                                Toast.makeText(MainActivity.this, R.string.endOfListToast, Toast.LENGTH_SHORT).show();
                                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.long_beep);
                                mediaPlayer.start();
                                mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                                    mediaPlayer1.release();
                                    switch (repeatAtEnd) {
                                        case 0:
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                            break;
                                        case 1:
                                            clickPlay();
                                            break;
                                        case 2:
                                            playNextFile();
                                            break;
                                        case 3:
                                            playRandomFile();
                                            break;
                                    }
                                });
                            } else {
                                pickWord(0);
                            }
                        }
                    }, lineDelay * 500);
                }
                if (playingState == 0) {
                    if (viewHolder[1] != null) {
                        viewHolder[1].clearHighlight();
                    }
                    floatingTextView[1].setTextColor(originalColor);
                    if (isListClicked) {
                        delayHandler.postDelayed(() -> {
                            isListClicked = false;
                            clickPlay();
                        }, lineDelay * 500);
                    } else {
                        currentLine = 0;
                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        };
    }

    private void playNextFile() {
        if (selectedFilenames == null) {
            return;
        }
        int arraySize = selectedFilenames.size();
        if (arraySize > 0) {
            String[] selectedArray = selectedFilenames.toArray(new String[arraySize]);
            if (currentFile.equals("")) {
                currentFile = selectedArray[0];
            } else {
                int currentInt = -1;
                for (int i = 0; i < arraySize; i++) {
                    if (currentFile.equals(selectedArray[i])) {
                        currentInt = i;
                    }
                }
                if (currentInt == -1 || currentInt >= arraySize - 1) {
                    currentFile = selectedArray[0];
                } else {
                    currentFile = selectedArray[currentInt + 1];
                }
            }
            String s = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .getPath() + "/" + currentFile;
            if (loadSavedFile(s)) {
                clickPlay();
            } else {
                playingState = 1;
                repeatCount = 1;
                currentLine = listAdapter[0].getItemCount();
                utterance[1].onDone("");
            }
        }
    }

    private void playRandomFile() {
        if (selectedFilenames == null) {
            return;
        }
        int arraySize = selectedFilenames.size();
        if (arraySize > 0) {
            String[] selectedArray = selectedFilenames.toArray(new String[arraySize]);
            Random random = new Random();
            currentFile = selectedArray[random.nextInt(arraySize)];
            String s = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .getPath() + "/" + currentFile;
            loadSavedFile(s);
            if (loadSavedFile(s)) {
                clickPlay();
            } else {
                playingState = 1;
                repeatCount = 1;
                currentLine = listAdapter[0].getItemCount();
                utterance[1].onDone("");
            }
        }
    }

    private void setMultipleEnable(boolean canFinish, boolean canEdit, boolean canPlay, boolean canPause, boolean canStop) {
        finishButton.setEnabled(canFinish);
        editButton.setEnabled(canEdit);
        playButton.setEnabled(canPlay);
        pauseButton.setEnabled(canPause);
        stopButton.setEnabled(canStop);
        if (canStop) {
            finishButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            loadButton.setVisibility(View.GONE);
        } else {
            finishButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            loadButton.setVisibility(View.VISIBLE);
        }
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
        for (int listNum = 0; listNum < 2; listNum++) {
            if (listString[listNum] != null) {
                String tempList = listString[listNum].replaceAll("\n", "");
                lineCount[listNum] = listString[listNum].length() - tempList.length();
                int wordStart = 0, wordEnd;
                lineString[listNum] = new ArrayList<>();
                for (int i = 0; i < lineCount[listNum]; i++) {
                    wordEnd = listString[listNum].indexOf(10, wordStart);
                    lineString[listNum].add(listString[listNum].substring(wordStart, wordEnd));
                    wordStart = wordEnd + 1;
                }
                lineString[listNum].add(listString[listNum].substring(wordStart));
                layoutManager[listNum] = new LinearLayoutManager(this);
                listRecyclerView[listNum].setLayoutManager(layoutManager[listNum]);
                listAdapter[listNum] = new ListAdapter(lineString[listNum], textSize[listNum]);
                listRecyclerView[listNum].setAdapter(listAdapter[listNum]);
                if (hasListBackground) {
                    bgLayoutManager[listNum] = new LinearLayoutManager(this);
                    bgRecyclerView[listNum].setLayoutManager(bgLayoutManager[listNum]);
                    bgAdapter[listNum] = new BackgroundAdapter(lineString[listNum], textSize[listNum]);
                    bgRecyclerView[listNum].setAdapter(bgAdapter[listNum]);
                    bgRecyclerView[listNum].setVisibility(View.VISIBLE);
                } else {
                    bgRecyclerView[listNum].setVisibility(View.INVISIBLE);
                }
                listAdapter[listNum].setOnWordClickedListener(position -> {
                    if (!isListClicked) {
                        int currentState = playingState;
                        clickStop();
                        currentLine = position;
                        if (currentState == 1) {
                            isListClicked = true;
                        } else {
                            if (viewHolder[0] != null) {
                                viewHolder[0].clearHighlight();
                                floatingTextView[0].setTextColor(originalColor);
                            }
                            if (viewHolder[1] != null) {
                                viewHolder[1].clearHighlight();
                                floatingTextView[1].setTextColor(originalColor);
                            }
                            clickPlay(null);
                        }
                    }
                });
            }
        }
        maxLine = Math.max(lineCount[0], lineCount[1]);
        dummyRecyclerTextView.setTextSize(Math.max(textSize[0], textSize[1]) * 1.17f);
        StringBuilder dummyString = new StringBuilder();
        for (int i = 0; i < maxLine; i++) {
            dummyString.append("\n");
        }
        dummyRecyclerTextView.setText(dummyString);
        mainEditTextScrollView.setVisibility(View.INVISIBLE);
        mainRecyclerScrollView.setVisibility(View.VISIBLE);
        if (lineCount[0] != lineCount[1]) {
            Toast.makeText(this, R.string.unequalLengthErr, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickEdit(View view) {
        setMultipleEnable(true, false, false, false, false);
        playingState = 0;
        mainEditTextScrollView.setVisibility(View.VISIBLE);
        mainRecyclerScrollView.setVisibility(View.INVISIBLE);
    }

    public void clickPlay(View view) {
        if (lockOrientation) {
            playOrientation = getResources().getConfiguration().orientation;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            if (listEditText[0].getText().length() == 0 || listEditText[1].getText().length() == 0) {
                Toast.makeText(this, R.string.emptyListErr, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        clickPlay();
    }

    public void clickPlay() {
        if (tts[0].isLanguageAvailable(language[0]) < 0){
            Toast.makeText(this, String.format(getString(R.string.languageNotSupportedErr), language[0].getDisplayName()), Toast.LENGTH_SHORT).show();
            return;
        }
        if (tts[1].isLanguageAvailable(language[1]) < 0){
            Toast.makeText(this, String.format(getString(R.string.languageNotSupportedErr), language[1].getDisplayName()), Toast.LENGTH_SHORT).show();
            return;
        }
        setMultipleEnable(false, true, false, true, true);
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
            if (isSingleLineMode) {
                floatingConstraintLayout.setVisibility(View.VISIBLE);
            }
            isRepeating = false;
            repeatCount = repeatNum;
            pickWord(0);
        }
    }

    private void pickWord(int listNum) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            scrollToWord(listNum);
            viewHolder[listNum] = (ListAdapter.ViewHolder) listRecyclerView[listNum].findViewHolderForAdapterPosition(currentLine);
            wordString[listNum] = lineString[listNum].get(currentLine);
            speakString(listNum);
        }, 100);
    }

    public void scrollToWord(int listNum) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainRecyclerScrollView.smoothScrollTo(0, listRecyclerView[listNum].getHeight()*(currentLine-1)/lineString[listNum].size());
        } else {
            mainRecyclerScrollView.smoothScrollTo(0, listRecyclerView[listNum].getHeight()*currentLine/lineString[listNum].size());
        }
    }

    public void setLanguageTtsButton(int listNum) {
        tts[listNum].setLanguage(language[listNum]);
        String s = language[listNum].getDisplayName().replace("(", "\n(");
        langButton[listNum].setText(s);
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
        if (viewHolder[listNum] != null) {
            viewHolder[listNum].highlightString();
        }
        if (isSingleLineMode) {
            if (listNum == 0) {
                floatingTextView[0].setText(wordString[0]);
                floatingTextView[1].setText(lineString[1].get(currentLine));
            }
            floatingTextView[listNum].setTextColor(Color.RED);
        }
        if (wordString[listNum].contains("(")) {
            int start = wordString[listNum].indexOf("(") + 1;
            if (wordString[listNum].substring(start).contains(")")) {
                int end = wordString[listNum].lastIndexOf(")");
                wordString[listNum] = wordString[listNum].substring(start, end);
            }
        } else if (wordString[listNum].contains("（")) {
            int start = wordString[listNum].indexOf("（") + 1;
            if (wordString[listNum].substring(start).contains("）")) {
                int end = wordString[listNum].lastIndexOf("）");
                wordString[listNum] = wordString[listNum].substring(start, end);
            }
        }
        tts[listNum].setOnUtteranceProgressListener(utterance[listNum]);
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, (soundVolume[listNum] + 1) / 7f);
        tts[listNum].stop();
        tts[listNum].speak(wordString[listNum], TextToSpeech.QUEUE_FLUSH, params
                , TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }

    public void clickPause(View view) {
        playingState = 2;
        setMultipleEnable(false, true, true, false, true);
    }

    public void clickStop(View view) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        clickStop();
    }

    public void clickStop() {
        int tempState = playingState;
        playingState = 0;
        setMultipleEnable(false, true, true, false, false);
        floatingConstraintLayout.setVisibility(View.INVISIBLE);
        if (tempState == 2) {
            if (isPlaying2ndLang) {
                utterance[1].onDone(null);
            } else {
                utterance[0].onDone(null);
            }
        }
    }

    public void clickLang0(View view) {
        langButton[0].setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(() -> langButton[0].setEnabled(true), 100);
        LanguageDialogFragment lang0Fragment = new LanguageDialogFragment(this, getString(R.string.pickLanguage0Title));
        lang0Fragment.show(getSupportFragmentManager(), "lang0");
    }

    public void clickLang1(View view) {
        langButton[1].setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(() -> langButton[1].setEnabled(true), 100);
        LanguageDialogFragment lang1Fragment = new LanguageDialogFragment(this, getString(R.string.pickLanguage1Title));
        lang1Fragment.show(getSupportFragmentManager(), "lang1");
    }

    public void clickSwap(View view) {
        swapButton.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(() -> swapButton.setEnabled(true), 100);
        swap(language);
        swap(listString);
        swapInt(speechRate);
        swapInt(soundVolume);
        swapInt(pitch);
        swapInt(textSize);
        for (int listNum = 0; listNum < 2; listNum++) {
            listEditText[listNum].setTextSize(textSize[listNum]);
            listEditText[listNum].setText(listString[listNum]);
            floatingTextView[listNum].setTextSize(textSize[listNum]);
            initTTS(listNum);
            if (mainRecyclerScrollView.getVisibility() == View.VISIBLE) {
                drawRecyclerView();
            }
        }
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.prefLanguage0), language[0].toLanguageTag());
        editor.putString(getString(R.string.prefLanguage1), language[1].toLanguageTag());
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
        displayModeButton = getMenu.findItem(R.id.displayMode);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.displayMode:
                if (isSingleLineMode) {
                    isSingleLineMode = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        displayModeButton.setIconTintMode(PorterDuff.Mode.MULTIPLY);
                    }
                    floatingConstraintLayout.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, R.string.normalListToast, Toast.LENGTH_SHORT).show();
                } else {
                    isSingleLineMode = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        displayModeButton.setIconTintMode(PorterDuff.Mode.XOR);
                    }
                    if (playingState > 0) {
                        floatingConstraintLayout.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(this, R.string.singleLineToast, Toast.LENGTH_SHORT).show();
                }
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.prefIsSingleLineMode), isSingleLineMode);
                editor.apply();
                break;
            case R.id.openSettings:
                intent = new Intent(this, SettingsActivity.class);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                startActivityForResult(intent, OPEN_SETTINGS);
                break;
            case R.id.loadExamples:
                loadExamples();
                break;
            case R.id.openHelpTips:
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
                break;
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
                loadFile(fileUri);
                saveDefaultFile(fileUri.toString());
            }
        }
        if (requestCode == OPEN_SETTINGS) {
            listEditText[0].setTextSize(textSize[0]);
            listEditText[1].setTextSize(textSize[1]);
            floatingTextView[0].setTextSize(textSize[0]);
            floatingTextView[1].setTextSize(textSize[1]);
            listEditText[0].setText(listString[0]);
            listEditText[1].setText(listString[1]);
            if (mainRecyclerScrollView.getVisibility() == View.VISIBLE) {
                drawRecyclerView();
            }
        }
        if (lockOrientation) {
            if (playOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }
    }

    public void clickSave(View view) {
        // Check has write permission
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
                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.noWritePermissionErr), Toast.LENGTH_SHORT).show();
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
        String saveString = // Format:   <LANGUAGE_1> Locale name here <1>
                "<LANGUAGE_1>" + language[0].toLanguageTag() + "<1>\n" + listString[0] +
                        "\n\n<LANGUAGE_2>" + language[1].toLanguageTag() + "<2>\n" + listString[1];
        int i = 0;
        if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .getPath() + "/" + fileName + ".txt").exists()) {
            do {
                i++;
            } while (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .getPath() + "/" + fileName + i + ".txt").exists());
            fileName = fileName + i;
        }
        String fileString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .getPath() + "/" + fileName + ".txt";
        File outputFile = new File(fileString);
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(outputFile)));
            out.writeUTF(saveString);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, String.format(context.getString(R.string.saveListText), fileName), Toast.LENGTH_LONG).show();
        saveDefaultFile(fileString);
    }

    public void clickLoad(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
        startActivityForResult(intent, LOAD_FILE);
    }

    private boolean loadFile(Uri fileUri) {
        StringBuilder loadedString = new StringBuilder();
        currentFile = fileUri.getLastPathSegment();
        try (InputStream inputStream = getContentResolver().openInputStream(fileUri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedString.append(line).append("\n");
            }
        } catch (IOException e) {
            Toast.makeText(this, String.format(getString(R.string.fileNotFoundErr), currentFile), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (analyzeFileSuccess(loadedString.toString())) {
            Toast.makeText(this, String.format(getString(R.string.fileLoadSuccess), currentFile), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, String.format(getString(R.string.fileFormatErr), currentFile), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean loadSavedFile(String defaultFile) {
        File inputFile = new File(defaultFile);
        currentFile = inputFile.getName();
        StringBuilder loadedString = new StringBuilder();
        try (InputStream inputStream = new FileInputStream(inputFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedString.append(line).append("\n");
            }
        } catch (IOException e) {
            Toast.makeText(this, String.format(getString(R.string.fileNotFoundErr), currentFile), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (analyzeFileSuccess(loadedString.toString())) {
            Toast.makeText(this, String.format(getString(R.string.fileLoadSuccess), currentFile), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, String.format(getString(R.string.fileFormatErr), currentFile), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean analyzeFileSuccess(String loadedString) {
        int[] index = new int[4];
        index[0] = loadedString.indexOf("<LANGUAGE_1>");
        index[1] = loadedString.indexOf("<1>");
        index[2] = loadedString.indexOf("<LANGUAGE_2>");
        index[3] = loadedString.indexOf("<2>");
        for (int i = 0; i < 4; i++) {
            if (index[i] == -1) {
                return false;
            }
        }
        String tempLang0 = loadedString.substring(index[0] + 12, index[1]);
        language[0] = Locale.forLanguageTag(tempLang0);
        setLanguageTtsButton(0);
        String tempLang1 = loadedString.substring(index[2] + 12, index[3]);
        language[1] = Locale.forLanguageTag(tempLang1);
        setLanguageTtsButton(1);
        String tempList0 = loadedString.substring(index[1] + 4, index[2] - 2);
        listEditText[0].setText(tempList0);
        String tempList1 = loadedString.substring(index[3] + 4, loadedString.length() - 1);
        listEditText[1].setText(tempList1);
        clickFinish(null);
        return true;
    }

    private void loadExamples() {
        ExampleDialogFragment exampleDialogFragment = new ExampleDialogFragment();
        exampleDialogFragment.show(getSupportFragmentManager(), "example");
    }

    public void showExample(int exampleNum, Locale language0, Locale language1, String listString0, String listString1) {
        language[0] = language0;
        language[1] = language1;
        listString[0] = listString0;
        listString[1] = listString1;
        for (int listNum = 0; listNum < 2; listNum++) {
            setLanguageTtsButton(listNum);
            listEditText[listNum].setText(listString[listNum]);
        }
        if (mainRecyclerScrollView.getVisibility() == View.VISIBLE) {
            drawRecyclerView();
        }
        clickFinish(null);
        saveDefaultFile("!LIST!" + exampleNum);
    }

    public void saveDefaultFile(String fileUri) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.prefDefaultFile), fileUri);
        editor.apply();
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
        model.setHasRecycler(mainRecyclerScrollView.getVisibility() == View.VISIBLE);
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
        model.setCurrentFile(currentFile);
        if (playingState == 1) {
            clickPause(null);
        }
        super.onDestroy();
    }
}