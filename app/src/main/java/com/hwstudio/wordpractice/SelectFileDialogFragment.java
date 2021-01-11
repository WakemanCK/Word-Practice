package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SelectFileDialogFragment extends DialogFragment {
    private final SettingsActivity settingsActivity;

    public Set<String> selectedFiles;

    public SelectFileDialogFragment(SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedFiles = new HashSet<>();
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"/");
        String[] listedArray = dir.list(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String name) {
                        return name.endsWith(".txt");
                    }
                }
        );
        if (listedArray==null) {
            Toast.makeText(settingsActivity, getString(R.string.noFilesErr), Toast.LENGTH_SHORT).show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectFileDialogTitle)
                .setMultiChoiceItems(listedArray, null
                        , new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                                if (isChecked) {
                                    selectedFiles.add(listedArray[i]);
                                } else {
                                    selectedFiles.remove(listedArray[i]);
                                }
                            }
                        })
                .setPositiveButton(R.string.okText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.selectedFilenames=selectedFiles;
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet(getString(R.string.prefSelectedFiles), selectedFiles);
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
