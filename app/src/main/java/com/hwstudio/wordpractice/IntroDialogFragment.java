package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class IntroDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please fill in words to the lists, or load example lists from the option menu.")
                .setPositiveButton(R.string.okText, (dialog, id) -> {
                });
        return builder.create();
    }
}
