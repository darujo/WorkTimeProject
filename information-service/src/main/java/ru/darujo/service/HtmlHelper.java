package ru.darujo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Objects;

public class HtmlHelper {


    public String getFooter() {
        return "</body>";
    }

    protected final ThreadLocal<String> rowClass = new ThreadLocal<>();

    protected void nextRow(Integer integer) {
        rowClass.set("table_row_" + (integer % 2 + 1));
    }

    protected String getTegStartRow(String teg) {
        return getTegStartRow(teg, "");
    }

    protected String getTegDec(boolean excel, String teg, Float plan, Float fact, Float value) {
        return getTegDec(excel, teg, plan != null && fact != null && plan < fact, value);
    }

    protected String getTegDec(boolean excel, String teg, boolean flag, Float value) {
        return getTegDec(excel, teg, null, flag ? "table_row_bed" : rowClass.get(), value);
    }

    protected String getTegDec(boolean excel, String teg, String textClass, Float value) {
        return getTegDec(excel, teg, null, textClass, value);
    }

    protected String getTegDate(boolean excel, String teg, ZonedDateTime plan, ZonedDateTime fact, String value) {
        return getTegDate(excel, teg, null, plan != null && fact != null && plan.isBefore(fact), value);
    }

    protected String getTegDate(boolean excel, String teg, String textClass, ZonedDateTime plan, ZonedDateTime fact, String value) {
        return getTegDate(excel, teg, textClass, plan != null && fact != null && plan.isBefore(fact), value);
    }

    protected String getTegDate(boolean excel, String teg, String textClass, boolean flag, String value) {
        return getTegDate(excel, teg, null, textClass, flag ? "table_row_bed" : rowClass.get(), value);
    }

    protected String getTegStartRow(String teg, String textClass) {
        return getTegStart(teg, textClass, rowClass.get());
    }

    protected String getTegStart(String teg, String textClass, String textClassTwo) {
        return getTegStart(teg, null, textClass, textClassTwo);
    }

    protected String getTegStartParam(String teg, String param) {
        return getTegStart(teg, param, rowClass.get(), null);
    }

    protected String getTegFix(boolean excel, String teg, String param, String textClass, String value) {
        return getTegStart(teg, (excel ? "data-freeze-pane-cell=\"true\" " : "") + (param == null ? "" : param), textClass, null) + String.format("%s</%s>", value, teg);
    }

    protected String getTegDate(boolean excel, String teg, String value) {
        return getTegDate(excel, teg, null, value);
    }

    protected String getTegDate(boolean excel, String teg, String param, String value) {
        return getTegDate(excel, teg, param, null, value);
    }

    protected String getTegDate(boolean excel, String teg, String param, String textClass, String value) {
        return getTegDate(excel, teg, param, textClass, rowClass.get(), value);
    }

    protected String getTegDate(boolean excel, String teg, String param, String textClass, String textClassTwo, String value) {
        return getTegStart(teg, (excel && value != null ? "data-date-cell-format=\"dd.MM.yyyy\" " : "") + (param == null ? "" : param), textClass, textClassTwo) + String.format("%s</%s>", printNotNull(value), teg);
    }

    protected String getTegText(boolean excel, String teg, String param, String value) {
        return getTegText(excel, teg, param, null, value);
    }

    protected String getTegText(boolean excel, String teg, String param, String textClass, String value) {
        return getTegStart(teg, (excel ? "data-text-cell=\"true\" " : "") + (param == null ? "" : param), textClass, rowClass.get()) + String.format("%s</%s>", value, teg);
    }

    protected String getTegText(boolean excel, String teg, String param, String textClass, String textClassTwo, String value) {
        return getTegStart(teg, (excel ? "data-text-cell=\"true\" " : "") + (param == null ? "" : param), textClass, textClassTwo) + String.format("%s</%s>", value, teg);
    }

    protected String getTegDec(boolean excel, String teg, Float value) {
        return getTegDec(excel, teg, "", null, value);
    }

    protected String getTegDec(boolean excel, String teg, String param, String textClass, Float value) {
        return getTegStart(teg, (excel && value != null ? "data-numeric-cell-format=\"#.00\" " : "") + (param == null ? "" : param), textClass, rowClass.get()) + String.format("%s</%s>", printNotNull(value), teg);
    }

    protected String getTableStart(boolean excel, String name) {
        return getTableStart(excel, name, true);
    }

    protected String getTableStart(boolean excel, String name, boolean newSheet) {
        return getTegStartParam("table", excel ? ((newSheet ? "data-new-sheet=\"true\" " : "") + "data-sheet-name=\" " + name + "\"" + " style=\"width:100%;\"") : "");
    }

    protected String getTegStart(String teg, String param, String textClass, String textClassTwo) {
        if (textClass == null && textClassTwo == null) {
            return String.format("<%s %s >", teg, param == null ? "" : param);
        }
        return String.format("<%s %s class=\"%s %s\">", teg, param == null ? "" : param, textClass == null ? "" : textClass, textClassTwo == null ? "" : textClassTwo);
//        return String.format("<%s %s class=\"%s %s\">", teg, param == null ? "" : param, textClass == null ? "" : textClass, textClassTwo == null ? "" : textClassTwo);
    }

    protected void getHead(StringBuilder sb) {
        sb.append("<!DOCTYPE HTML>");

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\"/>");
        sb.append("<meta name=\"color-scheme\" content=\"dark light\"/>");
        sb.append("<style type=\"text/css\">");
        addStyle(sb);


//        sb.append("@page {");
//        sb.append("    size: landscape;");
//        sb.append("    margin: 2%;");
//        sb.append(" }");
//
        sb.append("table {");
        sb.append("border-collapse: collapse;");
        sb.append(" border-spacing: 0;");
        sb.append("}");

        sb.append("th, td{");
//        sb.append("font-family: \"Courier New\";");
        sb.append("font-family: \"Arial\";");
        sb.append("border: thin solid #444444;");
        sb.append("padding-left: 2px;");
        sb.append("padding-right: 2px;");
        sb.append("}");

        sb.append("        th{");
        sb.append("background: #336699;");
        sb.append("color: #eeeeee;");
        sb.append("font-weight: bold;");
        sb.append("font-size: 10px;");
        sb.append("text-align:left;");
        sb.append("}");
//
        sb.append("        td{");
        sb.append("font-size:10px;");
        sb.append("}");
        sb.append("</style>");
        sb.append("</head>");
    }

    protected void addStyle(StringBuilder stringBuilder) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("style2.css")) {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null; ) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String printNotNull(Object objects) {
        if (objects == null) {
            return "";
        } else {
            return objects.toString();
        }

    }

    protected String printNotNull(Float objects) {
        if (objects == null) {
            return "";
        } else {
            return objects.toString().replace(".", ",");
        }

    }

}
