package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageDialogFragment extends DialogFragment {
    private final MainActivity mainActivity;
    private final String dialogTitle;

    public LanguageDialogFragment(MainActivity mainActivity, String getTitle){
        this.mainActivity = mainActivity;
        dialogTitle = getTitle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Locale[] locales = Locale.getAvailableLocales();
        List<Locale> localeList = new ArrayList<>();
        List<String> localeString = new ArrayList<>();
        for (Locale locale : locales) {
            int res = MainActivity.tts[0].isLanguageAvailable(locale);
            if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                localeList.add(locale);
            }
            localeString.add(locale.toString());
        }
        String[] stringArray = localeString.toArray(new String[localeString.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogTitle)
            .setItems(stringArray, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int item) {
               int listNum;
               if (dialog.toString().equals(getString(R.string.pickLanguage0Title))){
                   listNum = 0;
               }else{
                   listNum = 1;
               }
                   MainActivity.language[listNum] = new Locale(stringArray[item]);
                   mainActivity.setLanguage(listNum);
                   mainActivity.langButton[listNum].setText(MainActivity.language[listNum].getDisplayLanguage());
           }
        });
        return builder.create();
    }
}
