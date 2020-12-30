package com.hwstudio.wordpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar wordDelaySeekBar, lineDelaySeekBar, repeatNumSeekBar, speechRate0SeekBar, speechRate1SeekBar, volume0SeekBar, volume1SeekBar, pitch0SeekBar, pitch1SeekBar;
    private CheckBox listBackgroundCheckBox;
    private EditText textSize0EditText, textSize1EditText;
//    private boolean[] isAdjusting = new boolean[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        setView();
    }

    private void initView() {
        wordDelaySeekBar = findViewById(R.id.wordDelaySeekBar);
        wordDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.wordDelay = i;
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
                MainActivity.lineDelay=i;
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
                MainActivity.repeatNum = i;
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
                MainActivity.speechRate[1]= i;
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
                MainActivity.soundVolume[0]=i;
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
                MainActivity.soundVolume[1]=i;
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
                MainActivity.pitch[0]=i;
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
                MainActivity.pitch[1]=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        textSize0EditText = findViewById(R.id.textSize0EditText);
//        textSize0EditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!isAdjusting[0]){
//                    checkTextSize(0, editable.toString());
//                }
//            }
//        });
        textSize1EditText = findViewById(R.id.textSize1EditText);
//        textSize1EditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!isAdjusting[1]){
//                    checkTextSize(1, editable.toString());
//                }
//            }
//        });
    }
//
//    private void checkTextSize(int listNum, String editable){
//        isAdjusting[listNum] = true;
//        if (editable.equals("") || Integer.parseInt(editable) <12){
//            editable = "12";
//            updateEditText(listNum, editable);
//        } else if(Integer.parseInt(editable)>48) {
//            editable="48";
//            updateEditText(listNum, editable);
//        }
//        MainActivity.textSize[listNum] = Integer.parseInt(editable);
//        isAdjusting[listNum] = false;
//    }
//
//    private void updateEditText(int listNum, String editable) {
//        if (listNum ==0){
//            textSize0EditText.setText(editable);
//        }else {
//            textSize1EditText.setText(editable);
//        }
//    }

    private void setView() {
        wordDelaySeekBar.setProgress(MainActivity.wordDelay);
        lineDelaySeekBar.setProgress(MainActivity.lineDelay);
        repeatNumSeekBar.setProgress(MainActivity.repeatNum);
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
}