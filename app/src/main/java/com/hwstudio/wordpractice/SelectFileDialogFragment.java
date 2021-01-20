package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SelectFileDialogFragment extends DialogFragment {
    private final SettingsActivity settingsActivity;

    private Set<String> currentFiles;
    private boolean[] checkedFiles;

    public SelectFileDialogFragment(SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentFiles = new HashSet<>();
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/");
        String[] listedArray = directory.list(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String name) {
                        return name.endsWith(".txt");
                    }
                }
        );
        if (listedArray == null||listedArray.length==0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.noFilesErr)
                    .setPositiveButton(R.string.okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            return builder.create();
        } else {
            checkedFiles = new boolean[listedArray.length];
            for (int i = 0; i < listedArray.length; i++) {
                if (MainActivity.selectedFilenames.contains(listedArray[i])) {
                    checkedFiles[i] = true;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.selectFileDialogTitle)
                    .setMultiChoiceItems(listedArray, checkedFiles
                            , new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                                    if (isChecked) {
                                        currentFiles.add(listedArray[i]);
                                    } else {
                                        currentFiles.remove(listedArray[i]);
                                    }
                                }
                            })
                    .setPositiveButton(R.string.okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.selectedFilenames = currentFiles;
                            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putStringSet(getString(R.string.prefSelectedFiles), currentFiles);
                            editor.apply();
                        }
                    })
                    .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            return builder.create();
        }
    }
}
