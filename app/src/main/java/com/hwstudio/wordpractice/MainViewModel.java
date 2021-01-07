package com.hwstudio.wordpractice;

import android.os.Handler;
import android.speech.tts.TextToSpeech;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private boolean isChangingState;
    private boolean canFinish = true, canEdit, canPlay, canPause, canStop, hasRecycler;
    private String listString0, listString1, wordString0, wordString1;
    private int currentLine, maxLine, repeatCount, playingState;
    private boolean isRepeating, isPlaying2ndLang, isListClicked;
    private TextToSpeech tts0, tts1;
    private Handler delayHandler;

    public boolean isChangingState() {
        return isChangingState;
    }

    public void setChangingState(boolean changingState) {
        isChangingState = changingState;
    }

    public boolean isCanFinish() {
        return canFinish;
    }

    public void setCanFinish(boolean canFinish) {
        this.canFinish = canFinish;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean isCanPause() {
        return canPause;
    }

    public void setCanPause(boolean canPause) {
        this.canPause = canPause;
    }

    public boolean isCanStop() {
        return canStop;
    }

    public void setCanStop(boolean canStop) {
        this.canStop = canStop;
    }

    public boolean isHasRecycler() {
        return hasRecycler;
    }

    public void setHasRecycler(boolean hasRecycler) {
        this.hasRecycler = hasRecycler;
    }

    public String getListString0() {
        return listString0;
    }

    public void setListString0(String listString0) {
        this.listString0 = listString0;
    }

    public String getListString1() {
        return listString1;
    }

    public void setListString1(String listString1) {
        this.listString1 = listString1;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }

    public int getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getPlayingState() {
        return playingState;
    }

    public void setPlayingState(int playingState) {
        this.playingState = playingState;
    }

    public boolean isRepeating() {
        return isRepeating;
    }

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public TextToSpeech getTts0() {
        return tts0;
    }

    public void setTts0(TextToSpeech tts0) {
        this.tts0 = tts0;
    }

    public TextToSpeech getTts1() {
        return tts1;
    }

    public void setTts1(TextToSpeech tts1) {
        this.tts1 = tts1;
    }

    public Handler getDelayHandler() {
        return delayHandler;
    }

    public void setDelayHandler(Handler delayHandler) {
        this.delayHandler = delayHandler;
    }

    public boolean isPlaying2ndLang() {
        return isPlaying2ndLang;
    }

    public void setPlaying2ndLang(boolean playing2ndLang) {
        isPlaying2ndLang = playing2ndLang;
    }

    public String getWordString0() {
        return wordString0;
    }

    public void setWordString0(String wordString0) {
        this.wordString0 = wordString0;
    }

    public String getWordString1() {
        return wordString1;
    }

    public void setWordString1(String wordString1) {
        this.wordString1 = wordString1;
    }

    public boolean isListClicked() {
        return isListClicked;
    }

    public void setListClicked(boolean listClicked) {
        isListClicked = listClicked;
    }
}
