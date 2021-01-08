package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class ExampleDialogFragment extends DialogFragment {
    private final MainActivity mainActivity;
    
    public ExampleDialogFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] exampleList = {"Numbers", "Chinese vs English (Color)", "Japanese vs English (Color)"};
        Locale[][] language = new Locale[2][3];
        String[][] listString = new String[2][3];
        language[0][0] = Locale.CHINA;
        language[1][0] = Locale.US;
        language[0][1] = Locale.CHINESE;
        language[1][1] = Locale.ENGLISH;
        language[0][2] = Locale.JAPAN;
        language[1][2] = Locale.ENGLISH;
        listString[0][0] = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20";
        listString[1][0] = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20";
        listString[0][1] = getString(R.string.cnExampleColor);
        listString[1][1] = "red\norange\nyellow\ngreen\nblue\npurple\nblack\nwhite\ngrey";
        listString[0][2] = "赤\nオレンジ\n黄\n緑\n青い\n紫の\n黒\n白い\nグレー";
        listString[1][2] = "red\norange\nyellow\ngreen\nblue\npurple\nblack\nwhite\ngrey";

        // Code start
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.exampleTitle)
                .setItems(exampleList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        for (int listNum = 0; listNum < 2; listNum++) {
                            mainActivity.showExample(item, language[0][item], language[1][item]
                                    , listString[0][item], listString[1][item]);
                        }
//                        SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPref.edit();
//                        editor.putString(getString(R.string.prefLanguage0), MainActivity.language[0].toString());
//                        editor.putString(getString(R.string.prefLanguage1), MainActivity.language[1].toString());
//                        editor.apply();
                    }
                });
        return builder.create();
    }
    
    public void chooseExample(int item){
        mainActivity.showExample(item, language[0][item], language[1][item]
                                    , listString[0][item], listString[1][item]);
    }
}
