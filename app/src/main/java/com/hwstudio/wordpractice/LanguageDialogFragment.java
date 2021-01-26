package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class LanguageDialogFragment extends DialogFragment {

    private final MainActivity mainActivity;
    private final String dialogTitle;

    public LanguageDialogFragment(MainActivity mainActivity, String getTitle) {
        this.mainActivity = mainActivity;
        dialogTitle = getTitle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Locale[] locales = Locale.getAvailableLocales();
        List<Locale> localeList = new ArrayList<>();
        List<String> localeString = new ArrayList<>();
        for (Locale locale : locales) {
            if ((MainActivity.tts[0].isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
                localeList.add(locale);
                localeString.add(locale.getDisplayName());
            }
        }
        String[] stringArray = localeString.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogTitle)
                .setItems(stringArray, (dialog, item) -> {
                    int listNum;
                    if (getTag().equals("lang0")) {
                        listNum = 0;
                    } else {
                        listNum = 1;
                    }
                    MainActivity.language[listNum] = localeList.get(item);
                    mainActivity.setLanguageTtsButton(listNum);
                    SharedPreferences sharedPref = Objects.requireNonNull(getContext()).getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.prefLanguage0), MainActivity.language[0].toLanguageTag());
                    editor.putString(getString(R.string.prefLanguage1), MainActivity.language[1].toLanguageTag());
                    editor.apply();
                })
                .setNegativeButton(R.string.cancelButton, (dialogInterface, i) -> {
                });
        return builder.create();
    }
}
