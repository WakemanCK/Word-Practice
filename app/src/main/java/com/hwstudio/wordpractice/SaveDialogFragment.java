package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SaveDialogFragment extends DialogFragment {

    private final MainActivity mainActivity;

    public SaveDialogFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        final EditText saveEditText = new EditText(getActivity());
        alertBuilder.setMessage(getString(R.string.saveDialogTextView))
                .setView(saveEditText)
                .setPositiveButton(getString(R.string.saveButton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String s = saveEditText.getText().toString();
                        if (s.equals("")) {
                            s = "WordPracticeList";
                        }
                        mainActivity.saveFile(s, getActivity());
                    }
                })
                .setNegativeButton(getString(R.string.cancelButton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return alertBuilder.create();
    }
}
