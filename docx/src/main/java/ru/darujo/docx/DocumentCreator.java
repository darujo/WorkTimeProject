package ru.darujo.docx;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

// https://habr.com/ru/articles/503444/
public class DocumentCreator {
    public DocumentCreator() {

    }
    private void create(){
        File file = new File("C:/username/document.docx");
        FileInputStream fis;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        XWPFDocument document ; // Вот и объект описанного нами класса
        try {
            document = new XWPFDocument(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String documentLine = document.getDocument().toString();
        document.getParagraphs();
        // Внутри документа
        XWPFParagraph lastParagraph = document.createParagraph();
        lastParagraph.createRun();

        XWPFTable table = document.createTable(); //Здесь всё просто, создаем таблицу в документе и работаем с ней.
        XWPFTableCell cell = table.createRow().createCell();//Добавим к таблице ряд, к ряду - ячейку, и используем её.
        XWPFTable innerTable = new XWPFTable(cell.getCTTc().addNewTbl(), cell, 2, 2); // Воспользуемся конструктором для добавления таблицы - возьмем cell и её внутренние свойства, а так же зададим число рядов и колонок вложенной таблицы
        cell.insertTable(cell.getTables().size(), innerTable);

// Внутри таблицы
        document.createTable().createRow().createCell().addParagraph();
    }

    private void  newDoc(){
        XWPFDocument document = new XWPFDocument();
        XWPFTable table = document.createTable(2, 2);
        XWPFParagraph paragraph = document.createParagraph();
        fillTable(table);
        fillParagraph(paragraph);
        try {
            document.write(new FileOutputStream(new File("/path/to/file.docx")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void fillParagraph(XWPFParagraph paragraph) {
        paragraph.setIndentationFirstLine(20); // ????? paragraph.setIndent(20);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(12);
        run.setFontFamily("Times New Roman");
        run.setText("My text");
        run.addBreak();
        run.setText("New line");
    }
    void fillTable(XWPFTable table) {
        XWPFTableRow firstRow = table.getRows().get(0);
        XWPFTableRow secondRow = table.getRows().get(1);
        XWPFTableRow thirdRow = table.createRow();
        fillRow(firstRow);
    }
    void fillRow(XWPFTableRow row) {
        List<XWPFTableCell> cellsList = row.getTableCells();
        cellsList.forEach(cell -> fillParagraph(cell.getXWPFDocument().createParagraph()));
    }
    private void setWidthTable(XWPFTable table){
        CTTblWidth widthRepr = table.getCTTbl().getTblPr().addNewTblW();
        widthRepr.setType(STTblWidth.DXA);
        widthRepr.setW(BigInteger.valueOf(4000));
    }

    public static void main(String[] args) {

    }
}
