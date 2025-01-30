package com.il.lexicon.service;

import android.content.Context;
import android.net.Uri;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.data.jpa.provide.AppDatabase;
import com.il.lexicon.data.jpa.provide.WordDao;
import com.il.lexicon.ui.activity.MainActivity;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.il.lexicon.service.valid.WordValidService.isValid;

public class WordExcelService extends BaseService {
    private final WordDao wordDao;

    public WordExcelService(Context context) {
        super(context);
        this.wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();
    }

    public void exportToFile(Uri uri) {
        try {
            if (uri != null) {
                List<Word> words = wordDao.getAll();
                Workbook workbook = WorkbookFactory.create(true);
                Sheet sheet = workbook.createSheet("Words");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Learn lang word");
                headerRow.createCell(1).setCellValue("Native lang word");
                headerRow.createCell(2).setCellValue("Count complete repeats");
                headerRow.createCell(3).setCellValue("Count mistakes");
                headerRow.createCell(4).setCellValue("Add date");

                for (int i = 0; i < words.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    Word word = words.get(i);
                    row.createCell(0).setCellValue(word.getLearnLangWord());
                    row.createCell(1).setCellValue(word.getNativeLangWord());
                    row.createCell(2).setCellValue(word.getCountCompleteRepeats());
                    row.createCell(3).setCellValue(word.getCountMistakes());
                    row.createCell(4).setCellValue(word.getAddDate());
                }

                try (OutputStream os = getContentResolver().openOutputStream(uri)) {
                    workbook.write(os);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importFromFile(Uri uri) {
        try (InputStream is = getContentResolver().openInputStream(uri)) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            List<Word> wordsList = new LinkedList<>();
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getPhysicalNumberOfCells() == 0) continue;
                Word word = new Word();
                word.setLearnLangWord(row.getCell(0).getStringCellValue().trim());
                word.setNativeLangWord(row.getCell(1).getStringCellValue().trim());

                Cell cell = row.getCell(2);
                if (cell != null) {
                    try {
                        word.setCountCompleteRepeats((int) cell.getNumericCellValue());
                    } catch (Exception ignore) { /* Empty cell */ }
                }

                cell = row.getCell(3);
                if (cell != null) {
                    try {
                        word.setCountMistakes((int) cell.getNumericCellValue());
                    } catch (Exception ignore) { /* Empty cell */ }
                }

                cell = row.getCell(4);
                if (cell != null) {
                    try {
                        word.setAddDate(cell.getDateCellValue());
                    } catch (Exception e) {
                        word.setAddDate(new Date());
                    }
                } else {
                    word.setAddDate(new Date());
                }

                if (isValid(word)) {
                    wordsList.add(word);
                }
            }

            wordDao.insertAll(wordsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
