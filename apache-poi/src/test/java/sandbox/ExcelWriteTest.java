package sandbox;

import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static support.ResourceAccessor.excelFileWithName;

public class ExcelWriteTest {

    @Test
    public void canWriteOneCell() throws Exception {
        InputStream writing = new FileInputStream(excelFileWithName("prime-factors-scenarios"));
        Workbook wb = WorkbookFactory.create(writing);
        writing.close();
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(4);
        Cell cell = row.createCell(4);
        cell.setCellValue("seen");
        FileOutputStream fileOut = new FileOutputStream(excelFileWithName("prime-factors-scenarios"));
        wb.write(fileOut);
        fileOut.close();

        InputStream reading = new FileInputStream(excelFileWithName("prime-factors-scenarios"));
        Workbook newWb = WorkbookFactory.create(reading);
        reading.close();
        Sheet nSheet = newWb.getSheetAt(0);
        Row nRow = nSheet.getRow(4);
        Cell nCell = nRow.getCell(4);
        String text = nCell.getStringCellValue();

        assertThat(text, equalTo("seen"));
    }
}
