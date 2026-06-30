package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.project.ProjectDto;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.TaskServiceIntegrationImp;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.integration.WorkServiceIntegrationImp;
import ru.darujo.type.MessageType;
import ru.darujo.type.ReportType;
import uk.co.certait.htmlexporter.writer.excel.ExcelExporter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class ReportService {
    private WorkServiceIntegrationImp workServiceIntegration;
    private TaskServiceIntegrationImp taskServiceIntegration;
    private HtmlService htmlService;
    private UserServiceIntegrationImp userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegrationImp userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegrationImp taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegrationImp workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    public byte[] print(MessageType messageType, ProjectDto project, boolean excel) {
        if (!messageType.getReportTypeList().isEmpty()) {
            StringBuilder report = new StringBuilder(htmlService.getHead());
            for (ReportType reportType : messageType.getReportTypeList()) {
                if (reportType.isProject() && project == null) {
                    userServiceIntegration
                            .getProjects(null, null)
                            .forEach(projectDto ->
                                    report.append(print(reportType, projectDto, excel)));
                } else {
                    report.append(print(reportType, project, excel));
                }
            }
            report.append(htmlService.getFooter());
            if (excel) {
                try {
//            new ExcelExporter().exportHtml(report.toString(), new File("report____.xlsx"));
                    return new ExcelExporter().exportHtml(report.toString());
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            return report.toString().getBytes();
        } else {
            throw new ResourceNotFoundRunTime("Не задан тип отчета для сообщения " + messageType);
        }

    }

    public String print(ReportType reportType, ProjectDto project, boolean excel) {
        Long projectId = null;
        String reportName;
        if (project == null) {
            reportName = reportType.getName();
        } else {
            reportName = reportType.getName() + " " + project.getName();
            projectId = project.getId();
        }
        if (reportType.equals(ReportType.ZI_STATUS) || reportType.equals(ReportType.ZI_STATUS_PROJECT)) {
            return workFull(reportName, projectId, excel);
        } else if (reportType.equals(ReportType.USER_WORK)) {
            return getReportWork(false, projectId, reportName, excel);
        } else if (reportType.equals(ReportType.ZI_WORK) || reportType.equals(ReportType.ZI_WORK_PROJECT)) {
            return getReportWork(true, projectId, reportName, excel);
        }
        throw new ResourceNotFoundRunTime("Не известный тип отчета " + reportType);
    }

    private String getReportWork(boolean ziSplit, Long projectId, String headText, boolean excel) {
        List<AttrDto<Integer>> taskListType = taskServiceIntegration.getTaskTypes();
        Timestamp date = new Timestamp(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000);
        List<WorkUserTime> weekWorkList = workServiceIntegration.getWorkUserTime(ziSplit, projectId, date, new Timestamp(System.currentTimeMillis()));
        return htmlService.getWeekWork(
                headText,
                ziSplit,
                true,
                true,
                true,
                taskListType,
                weekWorkList,
                excel);
    }

    public String workFull(String nameReport, Long projectId, boolean excel) {
        LinkedList<String> sort = new LinkedList<>();
        sort.add("release.sort");
        sort.add("name");
        return htmlService.printRep(
                workServiceIntegration.getTimeWork(
                        null,
                        true,
                        null,
                        null,
                        projectId,
                        sort,
                        true),
                nameReport,
                excel);
    }
}
