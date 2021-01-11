package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class ExampleDialogFragment extends DialogFragment {
    private final MainActivity mainActivity;
    Locale[][] egLanguage = new Locale[2][15];
    String[][] egListString = new String[2][15];

    public ExampleDialogFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] exampleList = {"Numbers", "Chinese vs English (Color)", "Japanese vs English (Color)", "Chinese vs English (Body parts)", "Japanese vs English (Body parts)"
                               , "Chinese vs English (Date & time)", "Japanese vs English (Date & time)", "Chinese vs English (Direction & position)", "Japanese vs English (Direction & position)"
                               , "Chinese vs English (Transportation)", "Japanese vs English (Transportation)", "Chinese vs English (Food)", "Japanese vs English (Food)"
                                , "Chinese vs English (Conversation)", "Japanese vs English (Conversation)"};
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
                }).setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
        egLanguage[0][3] = Locale.CHINA;
        egLanguage[1][3] = Locale.US;
        egLanguage[0][4] = Locale.JAPAN;
        egLanguage[1][4] = Locale.US;
        egLanguage[0][5] = Locale.CHINA;
        egLanguage[1][5] = Locale.US;
        egLanguage[0][6] = Locale.JAPAN;
        egLanguage[1][6] = Locale.US;
        egLanguage[0][7] = Locale.CHINA;
        egLanguage[1][7] = Locale.US;
        egLanguage[0][8] = Locale.JAPAN;
        egLanguage[1][8] = Locale.US;
        egLanguage[0][9] = Locale.CHINA;
        egLanguage[1][9] = Locale.US;
        egLanguage[0][10] = Locale.JAPAN;
        egLanguage[1][10] = Locale.US;
        egLanguage[0][11] = Locale.CHINA;
        egLanguage[1][11] = Locale.US;
        egLanguage[0][12] = Locale.JAPAN;
        egLanguage[1][12] = Locale.US;
        egLanguage[0][13] = Locale.CHINA;
        egLanguage[1][13] = Locale.US;
        egLanguage[0][14] = Locale.JAPAN;
        egLanguage[1][14] = Locale.US;
        egListString[0][0] = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n30\n40\n50\n60\n70\n80\n90\n100\n200\n300\n400\n500\n600\n700\n800\n900\n1000\n2000\n3000\n4000\n5000\n6000\n7000\n8000\n9000\n10000\n100000\n1000000\n10000000\n100000000\n1000000000";
        egListString[1][0] = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n30\n40\n50\n60\n70\n80\n90\n100\n200\n300\n400\n500\n600\n700\n800\n900\n1000\n2000\n3000\n4000\n5000\n6000\n7000\n8000\n9000\n10000\n100000\n1000000\n10000000\n100000000\n1000000000";
        egListString[0][1] = mainActivity.getString(R.string.cnExampleColor);
        egListString[1][1] = "red\norange\nyellow\ngreen\nblue\npurple\nblack\nwhite\ngrey\nbrown\npink";
        egListString[0][2] = "赤\nオレンジ色\n黄色\n緑\n青\n紫\n黒\n白\n灰色\n茶色\nピンク";
        egListString[1][2] = "red\norange\nyellow\ngreen\nblue\npurple\nblack\nwhite\ngrey\nbrown\npink";
        egListString[0][3] = "頭\n眼睛\n眉毛\n耳朵\n鼻子\n口\n牙齒\n臉頰\n前額\n頭髮\n頸部\n身體\n肚子\n肩膀\n手臂\n手\n手指\n大腿\n腳";
        egListString[1][3] = "head\neye\neyebrow\near\nnose\nmouth\ntooth\ncheek\nforehead\nhair\nneck\nbody\nstomach\nshoulder\narm\nhand\nfinger\nthigh\nfoot";
        egListString[0][4] = "頭\n目\n眉毛\n耳\n鼻\n口\n歯\n頰\n額\n髪\n首\n体\nお腹\n肩\n腕\n手\n指\n腿\n足";
        egListString[1][4] = "head\neye\neyebrow\near\nnose\nmouth\ntooth\ncheek\nforehead\nhair\nneck\nbody\nstomach\nshoulder\narm\nhand\nfinger\nthigh\nfoot";
    /*    egListString[0][5]
        egListString[1][5]
        egListString[0][6]
        egListString[1][6]
        egListString[0][7]
        egListString[1][7]
        egListString[0][8]
        egListString[1][8]
        egListString[0][9]
        egListString[1][9]
        egListString[0][10]
        egListString[1][10]
        egListString[0][11]
        egListString[1][11]
        egListString[0][12]
        egListString[1][12]
        egListString[0][13]
        egListString[1][13]
        egListString[0][14]
        egListString[1][14]*/
    }
}
