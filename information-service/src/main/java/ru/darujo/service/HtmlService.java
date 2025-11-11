package ru.darujo.service;

import org.springframework.stereotype.Service;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.WorkRepDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class HtmlService {
    public String printRep(List<WorkRepDto> works) {

        StringBuilder sb = new StringBuilder();
        getHead(sb);
        sb.append("<body>");
        sb.append("<div  class=\"wrapper\">");
        sb.append("<table>");

        sb.append("<tr>");
        sb.append("<td class=\"table_head1\" rowspan=\"5\"> № п/п</td>");
        sb.append("<td class=\"table_head2\" rowspan=\"5\">Код Зи</td>");
        sb.append("<td class=\"sticky-col first-col table_head1\" rowspan=\"5\"> Наименование </td>");
        sb.append("<td class=\"table_head2\" colspan=\"4\"> 0 этап</td>");
        sb.append("<td class=\"table_head2\" colspan=\"2\" rowspan=\"4\"> Дата начала доработки</td>");
        sb.append("<td class=\"table_head1\" colspan=\"6\"> I этап</td>");
        sb.append("<td class=\"table_head2\" colspan=\"4\"> II этап</td>");
        sb.append("<td class=\"table_col1\" rowspan=\"5\">");
        sb.append("№ релиза");
        sb.append("</td>");
        sb.append("<td class=\"table_col1\" colspan = \"2\" rowspan = \"3\" > Выдача релиза </td >");
        sb.append("<td class=\"table_head1\" colspan = \"4\" > III этап </td >");
        sb.append("<td class=\"table_head2\" colspan = \"4\" > IV этап </td >");
        sb.append("<td class=\"table_head1\" colspan = \"2\" rowspan = \"4\" > Общие трудозатраты на доработку по ЗИ </td >");
        sb.append("<td rowspan = \"5\" > Вендерка </td >");

        sb.append("</tr >");
        sb.append("<tr >");
        sb.append("<td class=\"table_head2\" colspan = \"4\" > Согласование требований к доработке </td >");
        sb.append("<td class=\"table_head1\" colspan = \"6\" > Разработка прототипа </td >");
        sb.append("<td class=\"table_head2\" colspan = \"4\" > Стабилизация прототипа(30дней) </td >");
        sb.append("<td class=\"table_head1\" colspan = \"4\" > Стабилизация релиза(30дней) </td >");
        sb.append("<td class=\"table_head2\" colspan = \"4\" > ОПЭ релиза(30дней) </td >");

        sb.append("</tr >");
        sb.append("<tr > ");
        sb.append("<td class=\"table_head2\" colspan = \"4\" > Результат:внутреннее согласование ТЗ </td >");
        sb.append("<td class=\"table_head1\" colspan = \"6\" > Результат:выдача прототипа в соответствии с ТЗ</td >");
        sb.append("<td class=\"table_head2\" colspan = \"4\" > Результат:тестирования ЦК Рязань завершено</td >");
        sb.append("<td class=\"table_head1\" colspan = \"4\" > Результат:регресс тестирование ЦК Рязань завершено </td >");
        sb.append("<td class=\"table_head2\" colspan = \"4\" > Результат:завершение ОПЭ</td >");

        sb.append("</tr >");
        sb.append("<tr >");
        sb.append("<td class=\"table_head2\" colspan = \"2\" > Дата передачи ТЗ в работу</td >");
        sb.append("<td class=\"table_head2\" colspan = \"2\" > Трудозатраты, чел / час </td >");
        sb.append("<td class=\"table_head1\" colspan = \"2\" > Трудозатраты, чел / час </td >");
        sb.append("<td class=\"table_head1\" colspan = \"2\" > Дата окончания разработки</td >");
        sb.append("<td class=\"table_head1\" colspan = \"2\" > Дата выдачи прототипа</td >");
        sb.append("<td class=\"table_head2\" colspan = \"2\" > Дата </td >");
        sb.append("<td class=\"table_head2\" colspan = \"2\" > Трудозатраты, чел / час </td >");
        sb.append("<td class=\"table_col1\" colspan = \"2\" > Дата </td >");
        sb.append("<td class=\"table_head1\" colspan = \"2\" > Дата </td >");
        sb.append("<td class=\"table_head1\" colspan = \"2\" > Трудозатраты, чел / час </td >");
        sb.append("<td class=\"table_head2\" colspan = \"2\" > Дата </td >");
        sb.append("<td class=\"table_head2\" colspan = \"2\" > Трудозатраты, чел / час </td >");

        sb.append("</tr >");
        sb.append("<tr > ");
        sb.append("<td class=\"table_head2\" > План </td >");
        sb.append("<td class=\"table_head2\" > Факт </td >");
        sb.append("<td class=\"table_head2\" > План </td >");
        sb.append("<td class=\"table_head2\" > Факт </td >");
        sb.append("<td class=\"table_head2\" > План </td >");
        sb.append("<td class=\"table_head2\" > Факт </td >");
        sb.append("<td class=\"table_head2\" >");
        sb.append("План</td >");
        sb.append("<td class=\"table_head2\" >");
        sb.append(" Факт </td >");
        sb.append("<td class=\"table_head1\" > План </td >");
        sb.append("<td class=\"table_head1\" > Факт </td >");
        sb.append("<td class=\"table_col2\" > План </td >");
        sb.append("<td class=\"table_col2\" > Факт </td >");
        sb.append("<td class=\"table_head2\" > План </td >");
        sb.append("<td class=\"table_head2\" > Факт </td >");
        sb.append("<td class=\"table_head2\" > План </td >");
        sb.append("<td class=\"table_head2\" > Факт </td >");
        sb.append("<td class=\"table_col1\" > План </td >");
        sb.append("<td class=\"table_col1\" > Факт </td >");
        sb.append("<td class=\"table_head1\" > План </td >");
        sb.append("<td class=\"table_head1\" > Факт </td >");
        sb.append("<td class=\"table_head1\" > План </td >");
        sb.append("<td class=\"table_head1\" > Факт </td >");
        sb.append("<td class=\"table_head2\" > План </td >");
        sb.append("<td class=\"table_head2\" > Факт </td >");
        sb.append("<td class=\"table_head2\" > План </td >");
        sb.append("<td class=\"table_head2\" > Факт </td >");
        sb.append("<td class=\"table_head1\" > План </td >");
        sb.append("<td class=\"table_head1\" > Факт </td >");

        sb.append("</tr >");
        sb.append("<tbody >");
        works.forEach(work -> {
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(work.getId());
            sb.append("</td>");
            sb.append("<td class=\"text_not_wrap\" >");
            sb.append(work.getCodeZI());
            sb.append("</td>");
            sb.append("<td class=\"sticky-col first-col\" >");
            sb.append(work.getName());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getAnaliseEndPlanStr());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getAnaliseEndFactStr());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getLaborAnalise());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getTimeAnalise());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getStartTaskPlanStr());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getStartTaskFactStr());
            sb.append("</td>");

            sb.append("<td>");
            sb.append(work.getLaborDevelop());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getTimeDevelop());
            sb.append("</td>");
            sb.append("<td class=\"table_col2\" >");
            sb.append(work.getDevelopEndPlanStr());
            sb.append("</td>");
            sb.append("<td class=\"table_col2\"> ");
            sb.append(work.getDevelopEndFactStr());
            sb.append("</td>");
            sb.append("<td class=\"table_col2\" >");
            sb.append(work.getIssuePrototypePlanStr());
            sb.append("</td>");
            sb.append("<td class=\"table_col2\" > ");
            sb.append(work.getIssuePrototypeFactStr());
            sb.append("</td>");

            sb.append("<td> ");
            sb.append(work.getDebugEndPlanStr());
            sb.append("</td>");
            sb.append("<td> ");
            sb.append(work.getDebugEndFactStr());
            sb.append("</td>");

            sb.append("<td> ");
            sb.append(work.getLaborDebug());
            sb.append("</td>");
            sb.append("<td> ");
            sb.append(work.getTimeDebug());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getRelease());
            sb.append("</td>");
            sb.append("<td class=\"table_col1\" >");
            sb.append(work.getIssuingReleasePlanStr());
            sb.append("</td>");
            sb.append("<td class=\"table_col1\" >");
            sb.append(work.getIssuingReleaseFactStr());
            sb.append("</td>");
            sb.append("<td> ");
            sb.append(work.getReleaseEndPlanStr());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getReleaseEndFactStr());
            sb.append("</td>");

            sb.append("<td>");
            sb.append(work.getLaborRelease());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getTimeRelease());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getOpeEndPlanStr());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getOpeEndFactStr());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getLaborOPE());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getTimeOPE());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getTimePlan());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(work.getTimeFact());
            sb.append("</td>");

            sb.append("<td>");
            sb.append(work.getTimeWender());
            sb.append("</td>");


            sb.append("</tr>");
        });
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");


        sb.append("</tbody>");
        sb.append("</body>");
        sb.append("</head>");
        return sb.toString();

    }

    private void getHead(StringBuilder sb) {
        sb.append("<!DOCTYPE HTML>");

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<style type=\"text/css\">");
        addStyle(sb);
        sb.append("</style>");
        sb.append("</head>");
    }

    public String getWeekWork(boolean ziSplit,
                              boolean workTask,
                              boolean workTime,
                              boolean workPercent,
                              List<AttrDto<Integer>> taskListType,
                              List<WorkUserTime> weekWorkList) {

        StringBuilder sb = new StringBuilder();
        getHead(sb);
        sb.append("<body>");

        sb.append("<h1>Факт загрузки </h1>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td class=\"table_head1\" rowspan=\"2\">№ п/п</td>");
        if (ziSplit) {
            sb.append("<td class=\"table_head2 two_date\" rowspan=\"2\">ЗИ</td>");
        } else {
            sb.append("<td class=\"table_head2 two_date \" rowspan=\" 2 \">Период</td>");
        }
        sb.append("<td class=\"table_head1 field_fio\" rowspan=\"2\">Исполнитель</td>");
        sb.append("<td class=\"table_head2\" colspan=\"").append(taskListType.size()).append("\">Факт трудозатрат, чел/час</td>");
        if (!ziSplit) {
            sb.append("<td class=\"table_head1 week_work_plan_time \" rowspan=\"2\">Плановые трудозатраты за период,");
            sb.append("чел/час");
            sb.append("</td>");
        }
        sb.append("<td class=\"table_head2\"rowspan=\"2\">Итого за период</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        taskListType.forEach(taskType -> {
            sb.append("<td class=\" table_head2 week_work_plan_time\" >");
            sb.append(taskType.getValue());
            sb.append("</td>");
        });

        sb.append("</tr>");
        AtomicReference<Integer> i = new AtomicReference<>(0);
        sb.append("<tbody >");

        weekWorkList.forEach(work_zi -> {
            i.getAndSet(i.get() + 1);
            AtomicReference<Integer> j = new AtomicReference<>(0);
            work_zi.getUserWorkFormDTOs().forEach(work -> {
                j.getAndSet(j.get() + 1);
                sb.append("<tr>");
                sb.append("<td>");
                sb.append("<div class=\"horiz\">");
                if (ziSplit) {
                    sb.append("<p>").append(i).append(".</p>");
                }
                sb.append("<p>").append(j).append("</p>");
                sb.append("</div>");
                sb.append("</td>");
                if (ziSplit && work.getUserCol() != null) {
                    sb.append("<td rowspan=\"").append(work.getUserCol()).append("\">");
                    sb.append(work_zi.getName());
                    sb.append("</td>");
                }
                if (!ziSplit && work.getUserCol() != null) {
                    sb.append("<td rowspan=\"").append(work.getUserCol()).append("\">");
                    sb.append(work.getDateStartStr());
                    sb.append("-");
                    sb.append(work.getDateEndStr());
                    sb.append("</td>");
                }
                if (work.getAuthorFirstName() == null) {
                    sb.append("<td >").append(work.getNikName() == null ? "Итого" : work.getNikName()).append("</td>");
                } else
                    sb.append("<td >")
                            .append(work.getAuthorLastName())
                            .append(" ")
                            .append(work.getAuthorFirstName())
                            .append(" ")
                            .append(work.getAuthorPatronymic())
                            .append("</td>");

                taskListType.forEach(taskType -> {
                    sb.append("<td>");
                    sb.append("<div class=\"horiz\">");
                    if (workTask) {
                        sb.append("<div class=\"div-type\" >");
                        sb.append("<button class=\"p-td\" ng-click=\" openTask(work.workTask, taskType.codeInt)\">");
                        sb.append(searchJson(work.getWorkTaskColAttr(), taskType.getCodeInt()));
                        sb.append("</button>");
                        sb.append("</div>");
                    }
                    if (workTime) {
                        sb.append("<div class=\"div-type\">");
                        sb.append("<button class=\"p-td\" ng-click=\" openWorkTime(work.nikName, work.workTask, taskType.codeInt, work.dateStart, work.dateEnd)\">");
                        sb.append(searchJson(work.getWorkTimeAttr(), taskType.getCodeInt()));
                        sb.append("</button>");
                        sb.append("</div>");
                    }
                    if (workPercent) {
                        sb.append("<div class=\"div-type\" >");
                        sb.append("<p class=\"p-td\">");
                        sb.append(searchJson(work.getWorkPercent(), taskType.getCodeInt()));
                        sb.append("</p>");
                        sb.append("</div>");
                    }
                    sb.append("</div>");
                    sb.append("</td>");
                });

                if (!ziSplit && work.getUserCol() != null) {
                    sb.append("<td rowspan=\"")
                            .append(work.getUserCol())
                            .append("\">")
                            .append(work.getWorkPlan())
                            .append("</td>");
                }
                sb.append("<td>")
                        .append(work.getWorkAllFact())
                        .append("</td>");
                sb.append("</tr>");
            });
        });
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</tbody>");
        sb.append("</body>");
        sb.append("</head>");
        return sb.toString();

    }

    private String searchJson(List<AttrDto<Integer>> workTaskColAttr, Integer codeInt) {
        AttrDto<Integer> attrDto = workTaskColAttr.stream().filter(attrDtoInt -> attrDtoInt.getCodeInt().equals(codeInt)).findFirst().orElse(null);
        return attrDto != null ? attrDto.getValue() : "";
    }

    private void addStyle(StringBuilder stringBuilder) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("style.css")) {
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
