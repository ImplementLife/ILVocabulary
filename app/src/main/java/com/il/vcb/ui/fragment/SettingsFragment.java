package com.il.vcb.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import com.il.vcb.R;
import com.il.vcb.data.jpa.entity.Word;
import com.il.vcb.data.jpa.provide.AppDatabase;
import com.il.vcb.data.jpa.provide.WordDao;
import com.il.vcb.ui.activity.MainActivity;
import com.il.vcb.ui.custom.component.BaseFragment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import static com.il.vcb.service.valid.WordValidService.isValid;

public class SettingsFragment extends BaseFragment {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    private ActivityResultLauncher<Intent> fileLoadLauncher;
    private ActivityResultLauncher<Intent> fileSaverLauncher;

    @Override
    protected void init() {
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
            .setPositiveButton("Yes", (dialog, which) -> runAsync(wordDao::deleteAll))
            .setNegativeButton("No", (dialog, which) -> {})
            .show();
    }

    private void requestSaveFile() {
        boolean permissionGranted = checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionGranted) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_TITLE, "words.xlsx");
            fileSaverLauncher.launch(intent);
        }
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
        fileSaverLauncher = registerForActivityResult(new StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri fileSaverUri = result.getData().getData();
                exportWordsToFile(button, fileSaverUri);
            }
        });
        button.setOnClickListener(v -> requestSaveFile());
    }

    private void exportWordsToFile(Button button, Uri uri) {
        button.setEnabled(false);
        runAsync(() -> {
            try {
                List<Word> words = wordDao.getAll();
                Workbook workbook = WorkbookFactory.create(true);
                Sheet sheet = workbook.createSheet("Words");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("LearnLangWord");
                headerRow.createCell(1).setCellValue("NativeLangWord");
                headerRow.createCell(2).setCellValue("CountCompleteRepeats");
                headerRow.createCell(3).setCellValue("CountMistakes");

                for (int i = 0; i < words.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(words.get(i).getLearnLangWord());
                    row.createCell(1).setCellValue(words.get(i).getNativeLangWord());
                    row.createCell(2).setCellValue(words.get(i).getCountCompleteRepeats());
                    row.createCell(3).setCellValue(words.get(i).getCountMistakes());
                }

                if (uri != null) {
                    try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                        workbook.write(outputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        post(() -> button.setEnabled(true));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                post(() -> button.setEnabled(true));
            }
        });
    }

    private void defineFilePicker(Button button) {
        fileLoadLauncher = registerForActivityResult(new StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri fileUri = result.getData().getData();
                button.setEnabled(false);

                runAsync(() -> {
                    try {
                        ContentResolver resolver = getContentResolver();
                        InputStream inputStream = resolver.openInputStream(fileUri);
                        addWordsFromFile(inputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        post(() -> button.setEnabled(true));
                    }
                });
            }
        });
        button.setOnClickListener(v -> requestLoadFile());
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

    private ContentResolver getContentResolver() {
        return Objects.requireNonNull(getActivity()).getContentResolver();
    }

}