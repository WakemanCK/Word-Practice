package com.hwstudio.wordpractice;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private boolean canFinish=true, canEdit, canPlay,canPause,canStop, hasRecycler;
    private String listString0, listString1;
    private int currentLine, repeatCount, playingState;
    private boolean isRepeating;
    private TextToSpeech tts0, tts1;
    private ListAdapter.ViewHolder viewHolder0, viewHolder1;
    private UtteranceProgressListener utterance0, utterance1;
    private Handler delayHandler;

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

    public ListAdapter.ViewHolder getViewHolder0() {
        return viewHolder0;
    }

    public void setViewHolder0(ListAdapter.ViewHolder viewHolder0) {
        this.viewHolder0 = viewHolder0;
    }

    public ListAdapter.ViewHolder getViewHolder1() {
        return viewHolder1;
    }

    public void setViewHolder1(ListAdapter.ViewHolder viewHolder1) {
        this.viewHolder1 = viewHolder1;
    }

    public UtteranceProgressListener getUtterance0() {
        return utterance0;
    }

    public void setUtterance0(UtteranceProgressListener utterance0) {
        this.utterance0 = utterance0;
    }

    public UtteranceProgressListener getUtterance1() {
        return utterance1;
    }

    public void setUtterance1(UtteranceProgressListener utterance1) {
        this.utterance1 = utterance1;
    }

    public Handler getDelayHandler() {
        return delayHandler;
    }

    public void setDelayHandler(Handler delayHandler) {
        this.delayHandler = delayHandler;
    }
}
