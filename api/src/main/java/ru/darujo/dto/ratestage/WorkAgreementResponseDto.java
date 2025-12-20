package ru.darujo.dto.ratestage;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.user.UserFio;

import java.sql.Timestamp;

public class WorkAgreementResponseDto implements UserFio {
    private String firstName;
    private String lastName;
    private String patronymic;

    @SuppressWarnings("unused")
    public WorkAgreementResponseDto() {
    }

    private Long id;
    private String nikName;
    private Timestamp timestamp;
    @SuppressWarnings("unused")
    private String timestampStr;
    private String comment;
    private StatusResponse status;
    private Long workId;
    private Long requestId;

    public WorkAgreementResponseDto(Long id, String nikName, Timestamp timestamp, String comment, StatusResponse status, Long workId, Long requestId) {
        this.id = id;
        this.nikName = nikName;
        this.timestamp = timestamp;
        this.comment = comment;
        this.status = status;
        this.workId = workId;
        this.requestId = requestId;
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
    public String getComment() {
        return comment;
    }

    @SuppressWarnings("unused")
    public StatusResponse getStatus() {
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
    public Long getRequestId() {
        return requestId;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @SuppressWarnings("unused")
    public String getTimestampStr() {
        return DateHelper.dateTimeToStr(timestamp);
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
