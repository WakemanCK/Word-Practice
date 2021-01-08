package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class ExampleDialogFragment extends DialogFragment {
    private final MainActivity mainActivity;
    Locale[][] egLanguage = new Locale[2][3];
    String[][] egListString = new String[2][3];

    public ExampleDialogFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] exampleList = {"Numbers", "Chinese vs English (Color)", "Japanese vs English (Color)"};
        defineExample();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.exampleTitle)
                .setItems(exampleList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        for (int listNum = 0; listNum < 2; listNum++) {
                            mainActivity.showExample(item, egLanguage[0][item], egLanguage[1][item]
                                    , egListString[0][item], egListString[1][item]);
                        }
                    }
                });
        return builder.create();
    }

    public void chooseExample(int item) {
        defineExample();
        mainActivity.showExample(item, egLanguage[0][item], egLanguage[1][item]
                , egListString[0][item], egListString[1][item]);
    }

    private void defineExample() {
        egLanguage[0][0] = Locale.CHINA;
        egLanguage[1][0] = Locale.US;
        egLanguage[0][1] = Locale.CHINA;
        egLanguage[1][1] = Locale.US;
        egLanguage[0][2] = Locale.JAPAN;
        egLanguage[1][2] = Locale.US;
        egListString[0][0] = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20";
        egListString[1][0] = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20";
        egListString[0][1] = mainActivity.getString(R.string.cnExampleColor);
        egListString[1][1] = "red\norange\nyellow\ngreen\nblue\npurple\nblack\nwhite\ngrey\nbrown\npink";
        egListString[0][2] = "赤\nオレンジ色\n黄色\n緑\n青\n紫\n黒\n白\n灰色\n茶色\nピンク";
        egListString[1][2] = "red\norange\nyellow\ngreen\nblue\npurple\nblack\nwhite\ngrey\nbrown\npink";
    }
}
