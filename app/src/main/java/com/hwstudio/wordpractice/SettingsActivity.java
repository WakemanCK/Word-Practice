package com.hwstudio.wordpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    }

    private void initView() {
        wordDelayTextView = findViewById(R.id.wordDelayTextView);
        lineDelayTextView = findViewById(R.id.lineDelayTextView);
        repeatNumTextView = findViewById(R.id.repeatNumTextView);
        wordDelaySeekBar = findViewById(R.id.wordDelaySeekBar);
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
        lineDelaySeekBar = findViewById(R.id.lineDelaySeekBar);
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
        repeatNumSeekBar = findViewById(R.id.repeatNumSeekBar);
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
        listBackgroundCheckBox = findViewById(R.id.listBackgroundCheckBox);
        listBackgroundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.hasListBackground = b;
            }
        });
        speechRate0SeekBar = findViewById(R.id.speechRate0SeekBar);
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
        speechRate1SeekBar = findViewById(R.id.speechRate1SeekBar);
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
        volume0SeekBar = findViewById(R.id.volume0SeekBar);
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
        volume1SeekBar = findViewById(R.id.volume1SeekBar);
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
        pitch0SeekBar = findViewById(R.id.pitch0SeekBar);
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
        pitch1SeekBar = findViewById(R.id.pitch1SeekBar);
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
        volume0SeekBar.setProgress((int) MainActivity.soundVolume[0]);
        volume1SeekBar.setProgress((int) MainActivity.soundVolume[1]);
        pitch0SeekBar.setProgress(MainActivity.pitch[0]);
        pitch1SeekBar.setProgress(MainActivity.pitch[1]);
        textSize0EditText.setText(String.valueOf(MainActivity.textSize[0]));
        textSize1EditText.setText(String.valueOf(MainActivity.textSize[1]));
    }

    @Override
    protected void onDestroy() {
        if (textSize0EditText.getText().toString().equals("") || Integer.parseInt(textSize0EditText.getText().toString()) < 12) {
            textSize0EditText.setText("12");
        } else if (Integer.parseInt(textSize0EditText.getText().toString()) > 48) {
            textSize0EditText.setText("48");
        }
        MainActivity.textSize[0] = Integer.parseInt(textSize0EditText.getText().toString());
        MainActivity.textSize[1] = Integer.parseInt(textSize1EditText.getText().toString());
        super.onDestroy();
    }
}