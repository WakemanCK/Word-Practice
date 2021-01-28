package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class ExampleDialogFragment extends DialogFragment {
    private MainActivity mainActivity;
    private Locale[][] egLanguage = new Locale[2][15];
    private String[][] egListString = new String[2][15];

    public void newInstance(){
        mainActivity = MainActivity.model.getMainActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mainActivity = MainActivity.model.getMainActivity();
        String[] exampleList = {getString(R.string.egNumber), getString(R.string.egCEColor), getString(R.string.egJEColor), getString(R.string.egCEBody), getString(R.string.egJEBody)
                , getString(R.string.egCEDate), getString(R.string.egJEDate), getString(R.string.egCEPosition), getString(R.string.egJEPosition)
                , getString(R.string.egCETransportation), getString(R.string.egJETransportation), getString(R.string.egCEFood), getString(R.string.egJEFood)
                , getString(R.string.egCEConversation), getString(R.string.egJEConversation)};
        defineExample();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.exampleTitle)
                .setItems(exampleList, (dialog, item) -> {
                    for (int listNum = 0; listNum < 2; listNum++) {
                        mainActivity.showExample(item, egLanguage[0][item], egLanguage[1][item]
                                , egListString[0][item], egListString[1][item]);
                    }
                }).setNegativeButton(R.string.cancelButton, (dialogInterface, i) -> {
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
        egListString[0][3] = mainActivity.getString(R.string.cnExampleBody);
        egListString[1][3] = "head\neye\neyebrow\near\nnose\nmouth\ntooth\ncheek\nforehead\nhair\nneck\nbody\nstomach\nshoulder\narm\nhand\nfinger\nthigh\nfoot";
        egListString[0][4] = "頭\n目\n眉毛\n耳\n鼻\n口\n歯\n頰\n額\n髪\n首\n体\nお腹\n肩\n腕\n手\n指\n腿\n足";
        egListString[1][4] = "head\neye\neyebrow\near\nnose\nmouth\ntooth\ncheek\nforehead\nhair\nneck\nbody\nstomach\nshoulder\narm\nhand\nfinger\nthigh\nfoot";
        egListString[0][5] = mainActivity.getString(R.string.cnExampleDate);
        egListString[1][5] = "Sunday\nMonday\nTuesday\nWednesday\nThursday\nFriday\nSaturday\nJanuary\nFebruary\nMarch\nApril\nMay\nJune\nJuly\nAugust\nSeptember\nOctober\nNovember\nDecember\nmorning\nnoon\nafternoon\nevening\nnight\nyear\nmonth\nweek\nday\nhour\nminute\nsecond";
        egListString[0][6] = "日曜日\n月曜日\n火曜日\n水曜日\n木曜日\n金曜日\n土曜日\n一月\n二月\n三月\n四月\n五月\n六月\n七月\n八月\n九月\n十月\n十一月\n十二月\n朝\n正午\n午後\n夕方\n晚\n年\n月\n週\n日\n時\n分\n秒";
        egListString[1][6] = "Sunday\nMonday\nTuesday\nWednesday\nThursday\nFriday\nSaturday\nJanuary\nFebruary\nMarch\nApril\nMay\nJune\nJuly\nAugust\nSeptember\nOctober\nNovember\nDecember\nmorning\nnoon\nafternoon\nevening\nnight\nyear\nmonth\nweek\nday\nhour\nminute\nsecond";
        egListString[0][7] = mainActivity.getString(R.string.cnExamplePosition);
        egListString[1][7] = "above\nbelow\nleft\nright\nbetween\nin front of\nbehind\ninside\noutside\nnear\nnext to\neast\nsouth\nwest\nnorth";
        egListString[0][8] = "上\n下\n左\n右\n中\n前\n後ろ\n内（うち）\n外\n辺\n隣\n東\n南\n西\n北";
        egListString[1][8] = "above\nbelow\nleft\nright\nbetween\nin front of\nbehind\ninside\noutside\nnear\nnext to\neast\nsouth\nwest\nnorth";
        egListString[0][9] = mainActivity.getString(R.string.cnExampleTransportation);
        egListString[1][9] = "bicycle\ncar\nbus\ntaxi\nmetro\ntrain\nhigh-speed rail\ntrain station\nship\nairplane\nairport\nticket\nbaggage";
        egListString[0][10] = "自転車\n車\nバス\nタクシー\n地下鉄\n列車\n高速鉄道\n鉄道駅\n船\n飛行機\n空港\n切符\n荷物";
        egListString[1][10] = "bicycle\ncar\nbus\ntaxi\nmetro\ntrain\nhigh-speed rail\ntrain station\nship\nairplane\nairport\nticket\nbaggage";
        egListString[0][11] = mainActivity.getString(R.string.cnExampleFood);
        egListString[1][11] = "food\nbreakfast\nlunch\ndinner\nmeat\nfish\nvegetable\negg\nrice\nnoodle\nbread\nfruit\ndrink\nwater\ntea\nmilk\njuice\ncoffee\nwine\nbeer";
        egListString[0][12] = "食べ物\n朝ご飯\n昼ご飯\n晩ご飯\n肉\n魚\n野菜\n卵\nご飯\n麺\nパン\n果物\n飲み物\n水\nお茶\n牛乳\nジュース\nコーヒー\nお酒\nビール";
        egListString[1][12] = "food\nbreakfast\nlunch\ndinner\nmeat\nfish\nvegetable\negg\nrice\nnoodle\nbread\nfruit\ndrink\nwater\ntea\nmilk\njuice\ncoffee\nwine\nbeer";
        egListString[0][13] = mainActivity.getString(R.string.cnExampleConversation);
        egListString[1][13] = "Good morning\nGood afternoon\nGood evening\nHow are you\nThank you\nPlease\nExcuse me\nSorry\nWait a minute\nGood bye\nCan I have the bill please?\nExcuse me, where is the train station?\nExcuse me, is there a toilet nearby?";
        egListString[0][14] = "おはよう\nこんにちは\nこんばんは\nおげんきですか\nありがとう\nおねがいします\nすみません\nごめんなさい\nちょっとまって\nさようなら\nお会計お願いします\nすみません，鉄道駅はどこですか？\nすみません，この辺にトイレがありますか？";
        egListString[1][14] = "Good morning\nGood afternoon\nGood evening\nHow are you\nThank you\nPlease\nExcuse me\nSorry\nWait a minute\nGood bye\nCan I have the bill please?\nExcuse me, where is the train station?\nExcuse me, is there a toilet nearby?";
    }
}
