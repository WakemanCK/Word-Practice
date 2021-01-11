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

//    private List<File> listedFiles;
    public Set<String> selectedFiles;

    public SelectFileDialogFragment(SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;
    }

    //    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        listedFiles = new ArrayList<>();
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
//        Toast.makeText(settingsActivity, "debug path: " + dir.toString(), Toast.LENGTH_LONG).show();
//        File[] fileArray = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).listFiles();
//        if (fileArray != null) {
//            for (File file : fileArray) {
//                if (file.isFile() && file.getPath().endsWith(".txt")) {
//                    listedFiles.add(file);
//                }
//            }
//        } else {
        if (listedArray==null) {
            Toast.makeText(settingsActivity, getString(R.string.noFilesErr), Toast.LENGTH_SHORT).show();
        }
//        }
//        String[] stringArray = new String[listedFiles.size()];
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
                      /*  Set<String> selectedFilenames = new HashSet<>();
                        for (File file : selectedFiles) {
                            selectedFilenames.add(file.getName());
                        }*/
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

//    public class SelectFiles extends SimpleFileVisitor<Path>{
//        @Override
//        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//            if (file.toString().endsWith(".txt")){
//                selectedItems.add(file);
//                return CONTINUE;
//            }
//        }
//    }
}
