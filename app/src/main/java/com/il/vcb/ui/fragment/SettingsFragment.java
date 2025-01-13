package com.il.vcb.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.il.vcb.R;
import com.il.vcb.data.jpa.entity.Word;
import com.il.vcb.data.jpa.provide.AppDatabase;
import com.il.vcb.data.jpa.provide.WordDao;
import com.il.vcb.ui.activity.MainActivity;
import com.il.vcb.ui.custom.component.BaseFragment;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.il.vcb.service.util.WorkbookUtil.read;
import static com.il.vcb.service.valid.WordValidService.isValid;

public class SettingsFragment extends BaseFragment {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void init() {
        Button loadFileButton = findViewById(R.id.btn_load_data_from_file);
        loadFileButton.setOnClickListener(v -> openFilePicker());
        defineFilePicker(loadFileButton);

        Button clearDB = findViewById(R.id.btn_clear_db);
        clearDB.setOnClickListener(v -> {
            runAsync(wordDao::deleteAll);
        });

    }

    private void defineFilePicker(Button loadFileButton) {
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri fileUri = result.getData().getData();

                // Disable the button to prevent multiple file selection
                loadFileButton.setEnabled(false);

                runAsync(() -> {
                    try {
                        // Use ContentResolver to open the file
                        ContentResolver resolver = Objects.requireNonNull(getActivity()).getContentResolver();
                        InputStream inputStream = resolver.openInputStream(fileUri);
                        if (inputStream == null) {
                            // Handle the case where inputStream is null
                            post(() -> loadFileButton.setEnabled(true));
                            return;
                        }

                        // Read the file (assuming you're using Apache POI or a similar library to process Excel files)
                        addWordsFromFile(inputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        post(() -> loadFileButton.setEnabled(true));  // Re-enable the button on error
                    }
                });
            }
        });
    }

    private void addWordsFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);  // Using WorkbookFactory to create the workbook
        Sheet sheet = workbook.getSheetAt(0);
        List<Word> lw = new LinkedList<>();
        for (Row row : sheet) {
            Word word = new Word();
            word.setLearnLangWord(row.getCell(0).getStringCellValue().trim());
            word.setNativeLangWord(row.getCell(1).getStringCellValue().trim());
            if (isValid(word)) {
                lw.add(word);
            }
        }

        // Insert all words into the database
        wordDao.insertAll(lw);
    }

    private void openFilePicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(intent);
    }

}