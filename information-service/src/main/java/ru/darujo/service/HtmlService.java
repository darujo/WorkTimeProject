package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.workperiod.UserWorkFormDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.dto.workrep.WorkRepProjectDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class HtmlService {

    public String getFooter() {
        return "</body>";
    }

    public String printRep(List<WorkRepDto> works, String headText, boolean excel) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>").append(headText).append("</h1>");
        sb.append(getTegStart("div", "wrapper", null));
        sb.append(getTableStart(excel, headText));

        sb.append("<tr>");
        sb.append(getTegText(excel, "td", "rowspan=\"5\"", "table_head2", null, "ПН"));

        sb.append(getTegText(excel, "td", "rowspan=\"5\"", "table_head2", null, "Код Зи"));
        sb.append(getTegText(excel, "td", "rowspan=\"5\"", "sticky-col first-col table_head1", null, "Наименование"));
        sb.append(getTegFix(excel, "td", "rowspan=\"5\"", "table_head2", "Проект"));
        sb.append(getTegText(excel, "td", "rowspan=\"5\"", "table_head2", null, "Оценено"));
        sb.append(getTegText(excel, "td", "rowspan=\"5\"", "table_head1", null, "№ релиза"));

        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "0 этап"));
        sb.append(getTegText(excel, "td", "colspan=\"2\" rowspan=\"4\"", "table_head2", null, "Дата начала доработки"));
        sb.append(getTegText(excel, "td", "colspan=\"6\"", "table_head1", null, "I этап"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "II этап"));
        sb.append(getTegText(excel, "td", "colspan=\"2\" rowspan=\"3\"", "table_col1", null, "Выдача релиза"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head1", null, "III этап"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "IV этап"));
        sb.append(getTegText(excel, "td", "colspan=\"2\" rowspan=\"4\"", "table_head1", null, "Общие трудозатраты на доработку по ЗИ"));
        sb.append(getTegText(excel, "td", "rowspan=\"5\"", "table_head2", null, "Вендерка"));

        sb.append("</tr >");
        sb.append("<tr >");
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "Согласование требований к доработке"));
        sb.append(getTegText(excel, "td", "colspan=\"6\"", "table_head1", null, "Разработка прототипа"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "Стабилизация прототипа(30дней)"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head1", null, "Стабилизация релиза(30дней)"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "ОПЭ релиза(30дней)"));

        sb.append("</tr >");
        sb.append("<tr > ");
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "Результат:внутреннее согласование ТЗ"));
        sb.append(getTegText(excel, "td", "colspan=\"6\"", "table_head1", null, "Результат:выдача прототипа в соответствии с ТЗ"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "Результат:тестирования ЦК Рязань завершено"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head1", null, "Результат:регресс тестирование ЦК Рязань завершено"));
        sb.append(getTegText(excel, "td", "colspan=\"4\"", "table_head2", null, "Результат:завершение ОПЭ"));

        sb.append("</tr >");
        sb.append("<tr >");
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head2", null, "Дата передачи ТЗ в работу"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head2", null, "Трудозатраты, чел / час"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head1", null, "Трудозатраты, чел / час"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head1", null, "Дата окончания разработки"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head1", null, "Дата выдачи прототипа"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head2", null, "Дата"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head2", null, "Трудозатраты, чел / час"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_col1", null, "Дата"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head1", null, "Дата"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head1", null, "Трудозатраты, чел / час"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head2", null, "Дата"));
        sb.append(getTegText(excel, "td", "colspan=\"2\"", "table_head2", null, "Трудозатраты, чел / час"));

        sb.append("</tr >");
        sb.append("<tr > ");
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_col2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_col2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_col1", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_col1", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head2", "", "Факт"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "План"));
        sb.append(getTegText(excel, "td", null, "table_head1", "", "Факт"));

        sb.append("</tr >");
        sb.append("<tbody >");
        AtomicInteger i = new AtomicInteger();
        works.forEach(workRepDto -> {
            nextRow(i.getAndIncrement());
            AtomicBoolean first = new AtomicBoolean(true);
            if (workRepDto.getWorkRepProjectDtoList().isEmpty()) {
                printWorkRep(excel, workRepDto, new WorkRepProjectDto(), sb, first);
            } else {
                workRepDto.getWorkRepProjectDtoList().forEach(workRepProjectDto -> printWorkRep(excel, workRepDto, workRepProjectDto, sb, first));
            }

        });
        if (excel) {
            sb.append("<tr>");
            rowClass.set("");
            for (int j = 0; j < 35; j++) {
                sb.append(getTegText(excel, "td", "style=\"color: #ffffff;\"", "__________"));
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");
        return sb.toString();
    }

    private void printWorkRep(boolean excel, WorkRepDto workRepDto, WorkRepProjectDto workRepProjectDto, StringBuilder sb, AtomicBoolean first) {
        sb.append("<tr>");
        if (first.getAndSet(false)) {
            sb.append(getTegText(excel, "td", "rowspan=\"" + workRepDto.getWorkRepProjectDtoList().size() + "\"", printNotNull(workRepDto.getId())));
            sb.append(getTegText(excel, "td", "rowspan=\"" + workRepDto.getWorkRepProjectDtoList().size() + "\"", "text_not_wrap", printNotNull(workRepDto.getCodeZI())));
            sb.append(getTegText(excel, "td", "rowspan=\"" + workRepDto.getWorkRepProjectDtoList().size() + "\"", "sticky-col first-col", printNotNull(workRepDto.getName())));
        }
        sb.append(getTegText(excel, "td", null, printNotNull(workRepProjectDto.getProjectCode() + " " + workRepProjectDto.getProjectName())));
        if (!excel) {

            sb.append(getTegStartRow("td"));
            sb.append(getTegText(false, "label", "for=\"RatedTd\"", ""));
            sb.append(getTegText(false, "input", "id=\"RatedTd\" disabled type=\"checkbox\" " + (workRepProjectDto.getRated() != null && workRepProjectDto.getRated() ? "checked" : ""), "vvod check_box_td_23", ""));
            sb.append("</td>");
        } else {
            sb.append(getTegText(true, "td", "", workRepProjectDto.getRated() != null && workRepProjectDto.getRated() ? "Да" : "Нет"));
        }

        sb.append(getTegText(excel, "td", "", printNotNull(workRepProjectDto.getRelease())));
        sb.append(getTegDate(excel, "td", null, workRepProjectDto.getAnaliseEndPlanStr()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getAnaliseEndPlan(), workRepProjectDto.getAnaliseEndFact(), workRepProjectDto.getAnaliseEndFactStr()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborAnalise()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborAnalise(), workRepProjectDto.getTimeAnalise(), workRepProjectDto.getTimeAnalise()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getStartTaskPlanStr()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getStartTaskPlan(), workRepProjectDto.getStartTaskFact(), workRepProjectDto.getStartTaskFactStr()));

        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborDevelop()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborDevelop(), workRepProjectDto.getTimeDevelop(), workRepProjectDto.getTimeDevelop()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getDevelopEndPlanStr()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getDevelopEndPlan(), workRepProjectDto.getDevelopEndFact(), workRepProjectDto.getDevelopEndFactStr()));
        sb.append(getTegDate(excel, "td", null, "table_col2", null, workRepProjectDto.getIssuePrototypePlanStr()));
        sb.append(getTegDate(excel, "td", "table_col2", workRepProjectDto.getIssuePrototypePlan(), workRepProjectDto.getIssuePrototypeFact(), workRepProjectDto.getIssuePrototypeFactStr()));

        sb.append(getTegDate(excel, "td", workRepProjectDto.getDebugEndPlanStr()));

        sb.append(getTegDate(excel, "td", workRepProjectDto.getDebugEndPlan(), workRepProjectDto.getDebugEndFact(), workRepProjectDto.getDebugEndFactStr()));

        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborDebug()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborDebug(), workRepProjectDto.getTimeDebug(), workRepProjectDto.getTimeDebug()));
        sb.append(getTegDate(excel, "td", "table_col1", workRepProjectDto.getIssuingReleasePlanStr()));
        sb.append(getTegDate(excel, "td", "table_col1", workRepProjectDto.getIssuingReleasePlan(), workRepProjectDto.getIssuingReleaseFact(), workRepProjectDto.getIssuingReleaseFactStr()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getReleaseEndPlanStr()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getReleaseEndPlan(), workRepProjectDto.getReleaseEndFact(), workRepProjectDto.getReleaseEndFactStr()));

        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborRelease()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborRelease(), workRepProjectDto.getTimeRelease(), workRepProjectDto.getTimeRelease()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getOpeEndPlanStr()));
        sb.append(getTegDate(excel, "td", workRepProjectDto.getOpeEndPlan(), workRepProjectDto.getOpeEndFact(), workRepProjectDto.getOpeEndFactStr()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborOPE()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getLaborOPE(), workRepProjectDto.getTimeOPE(), workRepProjectDto.getTimeOPE()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getTimePlan()));
        sb.append(getTegDec(excel, "td", workRepProjectDto.getTimePlan(), workRepProjectDto.getTimeFact(), workRepProjectDto.getTimeFact()));

        sb.append(getTegDec(excel, "td", workRepProjectDto.getTimeWender()));
        sb.append("</tr>");
    }

    private final ThreadLocal<String> rowClass = new ThreadLocal<>();

    private void nextRow(Integer integer) {
        rowClass.set("table_row_" + (integer % 2 + 1));
    }

    private String getTegStartRow(String teg) {
        return getTegStartRow(teg, "");
    }

    private String getTegDec(boolean excel, String teg, Float plan, Float fact, Float value) {
        return getTegDec(excel, teg, plan != null && fact != null && plan < fact, value);
    }

    private String getTegDec(boolean excel, String teg, boolean flag, Float value) {
        return getTegDec(excel, teg, null, flag ? "table_row_bed" : rowClass.get(), value);
    }

    private String getTegDec(boolean excel, String teg, String textClass, Float value) {
        return getTegDec(excel, teg, null, textClass, value);
    }

    private String getTegDate(boolean excel, String teg, Timestamp plan, Timestamp fact, String value) {
        return getTegDate(excel, teg, null, plan != null && fact != null && plan.before(fact), value);
    }

    private String getTegDate(boolean excel, String teg, String textClass, Timestamp plan, Timestamp fact, String value) {
        return getTegDate(excel, teg, textClass, plan != null && fact != null && plan.before(fact), value);
    }

    private String getTegDate(boolean excel, String teg, String textClass, boolean flag, String value) {
        return getTegDate(excel, teg, null, textClass, flag ? "table_row_bed" : rowClass.get(), value);
    }

    private String getTegStartRow(String teg, String textClass) {
        return getTegStart(teg, textClass, rowClass.get());
    }

    private String getTegStart(String teg, String textClass, String textClassTwo) {
        return getTegStart(teg, null, textClass, textClassTwo);
    }

    private String getTegStartParam(String teg, String param) {
        return getTegStart(teg, param, rowClass.get(), null);
    }

    private String getTegFix(boolean excel, String teg, String param, String textClass, String value) {
        return getTegStart(teg, (excel ? "data-freeze-pane-cell=\"true\" " : "") + (param == null ? "" : param), textClass, null) + String.format("%s</%s>", value, teg);
    }

    private String getTegDate(boolean excel, String teg, String value) {
        return getTegDate(excel, teg, null, value);
    }

    private String getTegDate(boolean excel, String teg, String param, String value) {
        return getTegDate(excel, teg, param, null, value);
    }

    private String getTegDate(boolean excel, String teg, String param, String textClass, String value) {
        return getTegDate(excel, teg, param, textClass, rowClass.get(), value);
    }

    private String getTegDate(boolean excel, String teg, String param, String textClass, String textClassTwo, String value) {
        return getTegStart(teg, (excel && value != null ? "data-date-cell-format=\"dd.MM.yyyy\" " : "") + (param == null ? "" : param), textClass, textClassTwo) + String.format("%s</%s>", printNotNull(value), teg);
    }

    private String getTegText(boolean excel, String teg, String param, String value) {
        return getTegText(excel, teg, param, null, value);
    }

    private String getTegText(boolean excel, String teg, String param, String textClass, String value) {
        return getTegStart(teg, (excel ? "data-text-cell=\"true\" " : "") + (param == null ? "" : param), textClass, rowClass.get()) + String.format("%s</%s>", value, teg);
    }

    private String getTegText(boolean excel, String teg, String param, String textClass, String textClassTwo, String value) {
        return getTegStart(teg, (excel ? "data-text-cell=\"true\" " : "") + (param == null ? "" : param), textClass, textClassTwo) + String.format("%s</%s>", value, teg);
    }

    private String getTegDec(boolean excel, String teg, Float value) {
        return getTegDec(excel, teg, "", null, value);
    }

    private String getTegDec(boolean excel, String teg, String param, String textClass, Float value) {
        return getTegStart(teg, (excel && value != null ? "data-numeric-cell-format=\"#,##\" " : "") + (param == null ? "" : param), textClass, rowClass.get()) + String.format("%s</%s>", printNotNull(value), teg);
    }

    private String getTableStart(boolean excel, String name) {
        return getTableStart(excel, name, true);
    }

    private String getTableStart(boolean excel, String name, boolean newSheet) {
        return getTegStartParam("table", excel ? ((newSheet ? "data-new-sheet=\"true\" " : "") + "data-sheet-name=\" " + name + "\"" + " style=\"width:100%;\"") : "");
    }

    private String getTegStart(String teg, String param, String textClass, String textClassTwo) {
        if (textClass == null && textClassTwo == null) {
            return String.format("<%s %s >", teg, param == null ? "" : param);
        }
        return String.format("<%s %s class=\"%s %s\">", teg, param == null ? "" : param, textClass == null ? "" : textClass, textClassTwo == null ? "" : textClassTwo);
//        return String.format("<%s %s class=\"%s %s\">", teg, param == null ? "" : param, textClass == null ? "" : textClass, textClassTwo == null ? "" : textClassTwo);
    }

    private void getHead(StringBuilder sb) {
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


    public String getWeekWork(String headText, boolean ziSplit, boolean workTask, boolean workTime, boolean workPercent, List<AttrDto<Integer>> taskListType, List<WorkUserTime> weekWorkList, boolean excel) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>").append(headText).append("</h1>");
        sb.append(getTableStart(excel, headText));
        sb.append("<tr>");
        sb.append(getTegText(excel, "td", "rowspan=\"2\"", "table_head1", "№ п/п"));
        if (ziSplit) {
            sb.append(getTegText(excel, "td", "rowspan=\"2\"", "table_head2 two_date", "ЗИ"));
        } else {
            sb.append(getTegText(excel, "td", "rowspan=\"2\"", "table_head2 two_date", "Период"));
        }
        sb.append(getTegText(excel, "td", "rowspan=\"2\"", "table_head1 field_fio", "Исполнитель"));
        sb.append(getTegFix(excel, "td", "colspan=\"" + taskListType.size() + "\"", "table_head2", "Факт трудозатрат, чел/час"));
        if (!ziSplit) {
            sb.append(getTegText(excel, "td", "rowspan=\"2\"", "table_head1 week_work_plan_time", "Плановые трудозатраты за период, чел/час"));
        }
        sb.append(getTegText(excel, "td", "rowspan=\"2\"", "table_head2", "Итого за период"));
        sb.append("</tr>");
        sb.append("<tr>");
        taskListType.forEach(taskType -> sb.append(getTegText(excel, "td", null, " table_head2 week_work_plan_time", printNotNull(taskType.getValue()))));

        sb.append("</tr>");
        AtomicInteger i = new AtomicInteger(0);
        sb.append("<tbody >");
        AtomicInteger rowI = new AtomicInteger();
        weekWorkList.forEach(work_zi -> {
            nextRow(rowI.getAndIncrement());
            i.incrementAndGet();
            AtomicInteger j = new AtomicInteger(0);
            if (work_zi.getUserWorkFormDTOs().isEmpty()) {
                UserWorkFormDto userWorkFormDto = new UserWorkFormDto();
                userWorkFormDto.setUserCol(1);
                userWorkFormDto.setFirstName("Итого");
                printUserWork(excel, ziSplit, workTask, workTime, workPercent, taskListType, work_zi, userWorkFormDto, sb, i.get(), j.incrementAndGet());
            }
            work_zi.getUserWorkFormDTOs().forEach(work -> printUserWork(excel, ziSplit, workTask, workTime, workPercent, taskListType, work_zi, work, sb, i.get(), j.incrementAndGet()));
        });
        sb.append("</tbody>");
        sb.append("</table>");
        return sb.toString();
    }

    public String getHead() {
        StringBuilder sb = new StringBuilder();
        getHead(sb);
        sb.append("<body>");

        return sb.toString();
    }

    private void printUserWork(boolean excel, boolean ziSplit, boolean workTask, boolean workTime, boolean workPercent, List<AttrDto<Integer>> taskListType, WorkUserTime work_zi, UserWorkFormDto work, StringBuilder sb, Integer i, Integer j) {
        sb.append("<tr>");
        sb.append(getTegText(excel, "td", null, (ziSplit ? i + "." + j : Integer.toString(j))));
        sb.append("</td>");
        if (ziSplit && work.getUserCol() != null) {
            sb.append(getTegText(excel, "td", "rowspan=\"" + work.getUserCol() + "\"", printNotNull(work_zi.getName())));
        }
//                log.info(work.getDateStartStr() + " - " + work.getDateEndStr());
        if (!ziSplit && work.getUserCol() != null) {
            sb.append(getTegText(excel, "td", "rowspan=\"" + work.getUserCol() + "\"", printNotNull(work.getDateStartStr()) + "-" + printNotNull(work.getDateEndStr())));
        }
        if (work.getAuthorFirstName() == null) {
            sb.append(getTegText(excel, "td", null, work.getNikName() == null ? "" : printNotNull(work.getNikName())));
        } else
            sb.append(getTegText(excel, "td ", null,
                    String.format("%s %s %s", printNotNull(work.getAuthorLastName()), printNotNull(work.getAuthorFirstName())
                            , printNotNull(work.getAuthorPatronymic()))));


        taskListType.forEach(taskType -> sb.append(getTegText(excel, "td",
                null,
                String.format("Задач: %s, Часов: %s, Проценты: %s",
                        workTask ? printNotNull(searchJson(work.getWorkTaskColAttr(), taskType.getCodeT())) : "",
                        workTime ? printNotNull(searchJson(work.getWorkTimeAttr(), taskType.getCodeT())) : "",
                        workPercent ? printNotNull(searchJson(work.getWorkPercent(), taskType.getCodeT())) : ""))));

        if (!ziSplit && work.getUserCol() != null) {
            sb.append(getTegDec(excel, "td", "rowspan=\"" + printNotNull(work.getUserCol()) + "\"", work.getWorkPlan()));
        }
        sb.append(getTegDec(excel, "td", work.getWorkAllFact()));
        sb.append("</tr>");
    }

    private String printNotNull(Object objects) {
        if (objects == null) {
            return "";
        } else {
            return objects.toString();
        }

    }

    private String searchJson(List<AttrDto<Integer>> workTaskColAttr, Integer codeInt) {
        AttrDto<Integer> attrDto = workTaskColAttr.stream().filter(attrDtoInt -> attrDtoInt.getCodeT().equals(codeInt)).findFirst().orElse(null);
        return attrDto != null ? attrDto.getValue() : "";
    }

    private void addStyle(StringBuilder stringBuilder) {
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
}
