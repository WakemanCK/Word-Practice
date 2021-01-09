package com.hwstudio.wordpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SelectFileDialogFragment extends DialogFragment {

    private List<File> listedFiles;
    public List<File> selectedFiles;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        listedFiles = new ArrayList<>();
        selectedFiles = new ArrayList<>();
        for (File file : Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).listFiles()) {
            if (file.isFile() && file.getPath().endsWith(".txt")) {
                listedFiles.add(file);
            }
        }
        String[] filesArray = new String[listedFiles.size()];
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please select files that will be played:")
                .setMultiChoiceItems(filesArray, null
                        , new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                                if (isChecked) {
                                    selectedFiles.add(listedFiles.get(i));
                                } else {
                                    selectedFiles.remove(listedFiles.get(i));
                                }
                            }
                        })
                .setPositiveButton(R.string.okText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Set<String> selectedFilenames = new HashSet<>();
                        for (File file : selectedFiles) {
                            selectedFilenames.add(file.getName());
                        }
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.prefSharedPref), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet(getString(R.string.prefSelectedFiles), selectedFilenames);
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
