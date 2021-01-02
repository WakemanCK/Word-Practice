package com.hwstudio.wordpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar wordDelaySeekBar, lineDelaySeekBar, repeatNumSeekBar, speechRate0SeekBar, speechRate1SeekBar, volume0SeekBar, volume1SeekBar, pitch0SeekBar, pitch1SeekBar;
    private CheckBox listBackgroundCheckBox;
    private EditText textSize0EditText, textSize1EditText;
    private TextView wordDelayTextView, lineDelayTextView, repeatNumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        setView();
        setListener();
    }

    private void initView() {
        wordDelayTextView = findViewById(R.id.wordDelayTextView);
        lineDelayTextView = findViewById(R.id.lineDelayTextView);
        repeatNumTextView = findViewById(R.id.repeatNumTextView);
        wordDelaySeekBar = findViewById(R.id.wordDelaySeekBar);
        lineDelaySeekBar = findViewById(R.id.lineDelaySeekBar);
        repeatNumSeekBar = findViewById(R.id.repeatNumSeekBar);
        listBackgroundCheckBox = findViewById(R.id.listBackgroundCheckBox);
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
        listBackgroundCheckBox.setChecked(MainActivity.hasListBackground);
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
        listBackgroundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.hasListBackground = b;
            }
        });
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

    public void clickReset(View view){
        wordDelaySeekBar.setProgress(0);
        lineDelaySeekBar.setProgress(1);
        repeatNumSeekBar.setProgress(2);
        listBackgroundCheckBox.setChecked(false);
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
        editor.putBoolean(getString(R.string.prefHasListBackground), MainActivity.hasListBackground);
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