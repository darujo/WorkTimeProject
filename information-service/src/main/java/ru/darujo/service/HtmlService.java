package ru.darujo.service;

import org.springframework.stereotype.Service;
import ru.darujo.dto.workrep.WorkRepDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Service
public class HtmlService {
    public String printRep(List<WorkRepDto> works) {

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE HTML>");

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<style type=\"text/css\">");
        addStyle(sb);
        sb.append("</style>");
        sb.append("</head>");
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
        // todo Вывести сообщение если файл не удалось открыть
//        open(file);

    }
    private void addStyle(StringBuilder stringBuilder)  {
        try(InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("style.css")) {
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
