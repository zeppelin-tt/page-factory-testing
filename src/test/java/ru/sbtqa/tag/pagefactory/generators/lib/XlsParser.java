package ru.sbtqa.tag.pagefactory.generators.lib;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;


public class XlsParser {
    public XlsParser(){}

    public String getRandomRow(final String filePath, final int sheetNumber) {
        String result = null;
        try {
            InputStream file = XlsParser.class.getClassLoader().getResourceAsStream(filePath);
            //Create workbook instance
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //read sheet
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
            Row row = null;
            Iterator<Row> rowIterator = sheet.iterator();
            int randomRow = new Random().nextInt(sheet.getLastRowNum());
            for (int i = 0; i <= randomRow; i++)
                row = rowIterator.next();
            result = row.getCell(0).toString();
            file.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
