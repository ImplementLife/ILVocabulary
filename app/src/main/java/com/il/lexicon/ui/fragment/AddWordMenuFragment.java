package com.il.lexicon.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.il.lexicon.R;
import com.il.lexicon.data.jpa.provide.AppDatabase;
import com.il.lexicon.data.jpa.provide.WordDao;
import com.il.lexicon.service.WordExcelService;
import com.il.lexicon.ui.custom.component.NavFragment;

import static android.app.Activity.RESULT_OK;

public class AddWordMenuFragment extends NavFragment {
    private WordDao wordDao;
    private WordExcelService wordExcelService;

    public AddWordMenuFragment() {
        super(R.layout.fragment_add_word_menu);
    }

    private ActivityResultLauncher<Intent> fileLoadLauncher;
    private ActivityResultLauncher<Intent> fileSaverLauncher;

    @Override
    protected void init() {
        wordDao = AppDatabase.getInstance(getContext()).getWordDao();
        wordExcelService = new WordExcelService(getContext());

        Button btnAddOne = findViewById(R.id.btn_add_one);
        btnAddOne.setOnClickListener(v -> {navigate(R.id.fragment_add_word_simple_one);});

        TextView tvTotalCount = findViewById(R.id.tv_total_count);
        TextView tvLearnedCount = findViewById(R.id.tv_learned_count);

        runAsync(() -> {
            int totalCount = wordDao.getCount();
            int learnedCount = wordDao.getCount(2);
            post(() -> {
                tvTotalCount.setText("Total words: " + totalCount);
                tvLearnedCount.setText("Learned words: " + learnedCount);
            });
        });

        Button clearDB = findViewById(R.id.btn_clear_db);
        clearDB.setOnClickListener(v -> deleteAll());

        Button btnImport = findViewById(R.id.btn_import);
        defineFilePicker(btnImport);

        Button btnExport = findViewById(R.id.btn_export);
        defineFileSaver(btnExport);
    }

    private void deleteAll() {
        new AlertDialog.Builder(getContext())
            .setMessage("Are you sure?")
            .setPositiveButton("Yes", (dialog, which) -> runAsync(wordDao::resetTable))
            .setNegativeButton("No", (dialog, which) -> {})
            .show();
    }

    private void requestSaveFile() {
        boolean permissionGranted = checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permissionGranted) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, "words.xlsx");
        fileSaverLauncher.launch(intent);
//        }
    }

    private void requestLoadFile() {
        boolean permissionGranted = checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionGranted) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            fileLoadLauncher.launch(intent);
        }
    }

    private void defineFileSaver(Button button) {
        fileSaverLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri fileSaverUri = result.getData().getData();
                button.setEnabled(false);
                runAsync(() -> {
                    wordExcelService.exportToFile(fileSaverUri);
                    post(() -> button.setEnabled(true));
                });
            }
        });
        button.setOnClickListener(v -> requestSaveFile());
    }

    private void defineFilePicker(Button button) {
        fileLoadLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri fileUri = result.getData().getData();
                button.setEnabled(false);

                runAsync(() -> {
                    wordExcelService.importFromFile(fileUri);
                    post(() -> button.setEnabled(true));
                });
            }
        });
        button.setOnClickListener(v -> requestLoadFile());
    }
}