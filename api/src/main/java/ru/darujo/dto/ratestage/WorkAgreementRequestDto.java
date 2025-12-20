package ru.darujo.dto.ratestage;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.user.UserFio;

import java.sql.Timestamp;
import java.util.List;

public class WorkAgreementRequestDto implements UserFio {
    private String firstName;
    private String lastName;
    private String patronymic;

    @SuppressWarnings("unused")
    public WorkAgreementRequestDto() {
    }

    private Long id;
    private String nikName;
    private Timestamp timestamp;
    @SuppressWarnings("unused")
    private String timestampStr;
    private String version;
    private String comment;
    private Timestamp term;
    @SuppressWarnings("unused")
    private String termStr;
    private StatusRequest status;
    private Long workId;
    private List<WorkAgreementResponseDto> listResponse;

    public WorkAgreementRequestDto(
            Long id,
            String nikName,
            Timestamp timestamp,
            String version,
            String comment,
            Timestamp term,
            StatusRequest status,
            Long workId,
            List<WorkAgreementResponseDto> listResponse) {
        this.id = id;
        this.nikName = nikName;
        this.timestamp = timestamp;
        this.version = version;
        this.comment = comment;
        this.term = term;
        this.status = status;
        this.workId = workId;
        this.listResponse = listResponse;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getNikName() {
        return nikName;
    }

    @SuppressWarnings("unused")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused")
    public String getVersion() {
        return version;
    }

    @SuppressWarnings("unused")
    public String getComment() {
        return comment;
    }

    @SuppressWarnings("unused")
    public Timestamp getTerm() {
        return term;
    }

    @SuppressWarnings("unused")
    public StatusRequest getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public String getStatusName() {
        return status.getName();
    }

    @SuppressWarnings("unused")
    public Long getWorkId() {
        return workId;
    }

    @SuppressWarnings("unused")
    public List<WorkAgreementResponseDto> getListResponse() {
        return listResponse;
    }

    @SuppressWarnings("unused")
    public String getTimestampStr() {
        return DateHelper.dateTimeToStr(timestamp);
    }

    @SuppressWarnings("unused")
    public String getTermStr() {
        return DateHelper.dateToDDMMYYYY(term);
    }

    public void setFirstName(String authorFirstName) {
        this.firstName = authorFirstName;
    }

    public void setLastName(String authorLastName) {
        this.lastName = authorLastName;
    }

    public void setPatronymic(String authorPatronymic) {
        this.patronymic = authorPatronymic;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }
}
