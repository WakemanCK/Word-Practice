package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SelectFileDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Set<String> currentFiles = new HashSet<>();
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/");
        String[] listedArray = directory.list(
                (file, name) -> name.endsWith(".txt")
        );
        if (listedArray == null || listedArray.length == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.noFilesErr)
                    .setPositiveButton(R.string.okText, (dialogInterface, i) -> {
                    });
            return builder.create();
        } else {
            boolean[] checkedFiles = new boolean[listedArray.length];
            for (int i = 0; i < listedArray.length; i++) {
                if (MainActivity.selectedFilenames.contains(listedArray[i])) {
                    checkedFiles[i] = true;
                    currentFiles.add(listedArray[i]);
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.selectFileDialogTitle)
                    .setMultiChoiceItems(listedArray, checkedFiles
                            , (dialogInterface, i, isChecked) -> {
                                if (isChecked) {
                                    currentFiles.add(listedArray[i]);
                                } else {
                                    currentFiles.remove(listedArray[i]);
                                }
                            })
                    .setPositiveButton(R.string.okText, (dialogInterface, i) -> {
                        MainActivity.selectedFilenames = currentFiles;
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet(getString(R.string.prefSelectedFiles), currentFiles);
                        editor.apply();
                    })
                    .setNegativeButton(R.string.cancelButton, (dialogInterface, i) -> {
                    });
            return builder.create();
        }
    }
}
