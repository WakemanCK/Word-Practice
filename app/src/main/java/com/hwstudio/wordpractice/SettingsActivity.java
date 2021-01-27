package com.hwstudio.wordpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PERMISSION = 31;
    private SeekBar wordDelaySeekBar, lineDelaySeekBar, repeatNumSeekBar, speechRate0SeekBar, speechRate1SeekBar, volume0SeekBar, volume1SeekBar, pitch0SeekBar, pitch1SeekBar;
    private RadioGroup repeatAtEndGroup;
    private Button selectButton;
    private CheckBox listBackgroundCheckBox, lockOrientationCheckBox;
    private EditText textSize0EditText, textSize1EditText;
    private TextView wordDelayTextView, lineDelayTextView, repeatNumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Load ad
        MobileService mainAd = new MobileService();
        FrameLayout adContainerView = findViewById(R.id.adFrameLayout);
        Display display = getWindowManager().getDefaultDisplay();
        mainAd.initBanner(this, adContainerView, display);

        initView();
        setView();
        setListener();
        // Added for hiding keyboard
        final View activityRootView = findViewById(R.id.settingScrollView);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            final int rootViewHeight = activityRootView.getHeight();
            activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                if (rootViewHeight - activityRootView.getHeight() > 100) {
                    findViewById(R.id.settingDummyTextView).setVisibility(View.VISIBLE);
                }
            });
        }, 100);
    }

    public void settingCloseKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e3) {
            e3.printStackTrace();
        }
        findViewById(R.id.settingDummyTextView).setVisibility(View.INVISIBLE);
    }

    private void initView() {
        wordDelayTextView = findViewById(R.id.wordDelayTextView);
        lineDelayTextView = findViewById(R.id.lineDelayTextView);
        repeatNumTextView = findViewById(R.id.repeatNumTextView);
        wordDelaySeekBar = findViewById(R.id.wordDelaySeekBar);
        lineDelaySeekBar = findViewById(R.id.lineDelaySeekBar);
        repeatNumSeekBar = findViewById(R.id.repeatNumSeekBar);
        repeatAtEndGroup = findViewById(R.id.repeatAtEndGroup);
        selectButton = findViewById(R.id.selectFileButton);
        listBackgroundCheckBox = findViewById(R.id.listBackgroundCheckBox);
        lockOrientationCheckBox = findViewById(R.id.lockOrientationCheckBox);
        speechRate0SeekBar = findViewById(R.id.speechRate0SeekBar);
        speechRate1SeekBar = findViewById(R.id.speechRate1SeekBar);
        volume0SeekBar = findViewById(R.id.volume0SeekBar);
        volume1SeekBar = findViewById(R.id.volume1SeekBar);
        pitch0SeekBar = findViewById(R.id.pitch0SeekBar);
        pitch1SeekBar = findViewById(R.id.pitch1SeekBar);
        textSize0EditText = findViewById(R.id.textSize0EditText);
        textSize1EditText = findViewById(R.id.textSize1EditText);
    }

    private void setView() {
        String s = getString(R.string.wordDelayTextView) + MainActivity.wordDelay * 0.5f + "s";
        wordDelayTextView.setText(s);
        s = getString(R.string.lineDelayTextView) + MainActivity.lineDelay * 0.5f + "s";
        lineDelayTextView.setText(s);
        s = getString(R.string.repeatNumTextView) + MainActivity.repeatNum;
        repeatNumTextView.setText(s);
        wordDelaySeekBar.setProgress(MainActivity.wordDelay);
        lineDelaySeekBar.setProgress(MainActivity.lineDelay);
        repeatNumSeekBar.setProgress(MainActivity.repeatNum - 1);
        ((RadioButton) repeatAtEndGroup.getChildAt(MainActivity.repeatAtEnd)).setChecked(true);
        if (MainActivity.repeatAtEnd > 1) {
            selectButton.setVisibility(View.VISIBLE);
        }
        listBackgroundCheckBox.setChecked(MainActivity.hasListBackground);
        lockOrientationCheckBox.setChecked(MainActivity.lockOrientation);
        speechRate0SeekBar.setProgress(MainActivity.speechRate[0]);
        speechRate1SeekBar.setProgress(MainActivity.speechRate[1]);
        volume0SeekBar.setProgress(MainActivity.soundVolume[0]);
        volume1SeekBar.setProgress(MainActivity.soundVolume[1]);
        pitch0SeekBar.setProgress(MainActivity.pitch[0]);
        pitch1SeekBar.setProgress(MainActivity.pitch[1]);
        textSize0EditText.setText(String.valueOf(MainActivity.textSize[0]));
        textSize1EditText.setText(String.valueOf(MainActivity.textSize[1]));
    }

    private void setListener() {
        wordDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.wordDelay = i;
                String s = getString(R.string.wordDelayTextView) + MainActivity.wordDelay * 0.5f + "s";
                wordDelayTextView.setText(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        lineDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.lineDelay = i;
                String s = getString(R.string.lineDelayTextView) + MainActivity.lineDelay * 0.5f + "s";
                lineDelayTextView.setText(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        repeatNumSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.repeatNum = i + 1;
                String s = getString(R.string.repeatNumTextView) + MainActivity.repeatNum;
                repeatNumTextView.setText(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        listBackgroundCheckBox.setOnCheckedChangeListener((compoundButton, b) -> MainActivity.hasListBackground = b);
        lockOrientationCheckBox.setOnCheckedChangeListener((compoundButton, b) -> MainActivity.lockOrientation = b);
        speechRate0SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.speechRate[0] = i;
                MainActivity.setSpeechRate(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        speechRate1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.speechRate[1] = i;
                MainActivity.setSpeechRate(1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        volume0SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.soundVolume[0] = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        volume1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.soundVolume[1] = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pitch0SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.pitch[0] = i;
                MainActivity.setPitch(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pitch1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.pitch[1] = i;
                MainActivity.setPitch(1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void clickDoNothing(View view) {
        MainActivity.repeatAtEnd = 0;
        selectButton.setVisibility(View.INVISIBLE);
    }

    public void clickRepeat(View view) {
        MainActivity.repeatAtEnd = 1;
        selectButton.setVisibility(View.INVISIBLE);
    }

    public void clickPlayNextFile(View view) {
        MainActivity.repeatAtEnd = 2;
        selectButton.setVisibility(View.VISIBLE);
    }

    public void clickPlayRandomFile(View view) {
        MainActivity.repeatAtEnd = 3;
        selectButton.setVisibility(View.VISIBLE);
    }

    public void clickSelectFile(View view) {
            // Check has read permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                clickSelectFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_READ_PERMISSION);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
            if (requestCode == REQUEST_READ_PERMISSION) {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clickSelectFile();
                } else {
                    Toast.makeText(SettingsActivity.this, getString(R.string.noReadPermissionErr), Toast.LENGTH_SHORT).show();
                }
            }
        }

    public void clickSelectFile() {
        SelectFileDialogFragment selectFileDialogFragment = new SelectFileDialogFragment();
        selectFileDialogFragment.show(getSupportFragmentManager(), "selectFiles");
    }

    public void clickClear(View view) {
        MainActivity.playingState = 0;
        MainActivity.listString[0] = "";
        MainActivity.listString[1] = "";
        Toast.makeText(this, R.string.clearListToast, Toast.LENGTH_SHORT).show();
    }

    public void clickReset(View view) {
        wordDelaySeekBar.setProgress(0);
        lineDelaySeekBar.setProgress(1);
        repeatNumSeekBar.setProgress(2);
        ((RadioButton) findViewById(R.id.repeatButton)).setChecked(true);
        selectButton.setVisibility(View.INVISIBLE);
        listBackgroundCheckBox.setChecked(false);
        lockOrientationCheckBox.setChecked(true);
        speechRate0SeekBar.setProgress(3);
        speechRate1SeekBar.setProgress(3);
        volume0SeekBar.setProgress(6);
        volume1SeekBar.setProgress(6);
        pitch0SeekBar.setProgress(3);
        pitch1SeekBar.setProgress(3);
        textSize0EditText.setText("36");
        textSize1EditText.setText("36");
    }

    @Override
    protected void onPause() {
        if (textSize0EditText.getText().toString().equals("") || Integer.parseInt(textSize0EditText.getText().toString()) < 18) {
            textSize0EditText.setText("18");
        } else if (Integer.parseInt(textSize0EditText.getText().toString()) > 48) {
            textSize0EditText.setText("48");
        }
        if (textSize1EditText.getText().toString().equals("") || Integer.parseInt(textSize1EditText.getText().toString()) < 18) {
            textSize1EditText.setText("18");
        } else if (Integer.parseInt(textSize1EditText.getText().toString()) > 48) {
            textSize1EditText.setText("48");
        }
        MainActivity.textSize[0] = Integer.parseInt(textSize0EditText.getText().toString());
        MainActivity.textSize[1] = Integer.parseInt(textSize1EditText.getText().toString());
        saveSettings();
        super.onPause();
    }

    private void saveSettings() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.prefWordDelay), MainActivity.wordDelay);
        editor.putInt(getString(R.string.prefLineDelay), MainActivity.lineDelay);
        editor.putInt(getString(R.string.prefRepeatNum), MainActivity.repeatNum);
        editor.putInt(getString(R.string.prefRepeatAtEnd), MainActivity.repeatAtEnd);
        editor.putBoolean(getString(R.string.prefHasListBackground), MainActivity.hasListBackground);
        editor.putBoolean(getString(R.string.prefLockOrientation), MainActivity.lockOrientation);
        editor.putInt(getString(R.string.prefSpeechRate0), MainActivity.speechRate[0]);
        editor.putInt(getString(R.string.prefSpeechRate1), MainActivity.speechRate[1]);
        editor.putInt(getString(R.string.prefSoundVolume0), MainActivity.soundVolume[0]);
        editor.putInt(getString(R.string.prefSoundVolume1), MainActivity.soundVolume[1]);
        editor.putInt(getString(R.string.prefPitch0), MainActivity.pitch[0]);
        editor.putInt(getString(R.string.prefPitch1), MainActivity.pitch[1]);
        editor.putInt(getString(R.string.prefTextSize0), MainActivity.textSize[0]);
        editor.putInt(getString(R.string.prefTextSize1), MainActivity.textSize[1]);
        editor.apply();
    }
}
