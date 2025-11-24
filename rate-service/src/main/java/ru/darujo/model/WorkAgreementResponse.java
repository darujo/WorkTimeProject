package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.darujo.assistant.helper.CompareHelper;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.ratestage.StatusResponse;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work_agreement_response")
public class WorkAgreementResponse {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    @Column(name = "timestamp")
    private Timestamp timestamp;
    @Column(name = "comment")
    private String comment;
    @Column(name = "status")
    private String status;
    @Column(name = "work_id")
    private Long workId;
    @ManyToOne()
    @JoinColumn(name = "request_id")
    private WorkAgreementRequest request;

    @Override
    public String toString() {
        return
                "Пользователь: " + nikName + "\n" +
                        "Время: " + DataHelper.dateTimeToStr(timestamp) + "\n" +
                        (comment != null && !comment.isBlank() ? ("Комментарий: " + comment + "\n") : "") +
                        "Статус: " + StatusResponse.valueOf(status).getName() + "\n";

    }

    public String compareObj(WorkAgreementResponse old) {
        if (old == null) {
            return toString();
        }
        return
                compareField("Пользователь", old.getNikName(), nikName) +
                        compareField("Время", DataHelper.dateTimeToStr(old.getTimestamp()), DataHelper.dateTimeToStr(timestamp)) +
                        compareField("Комментарий", old.getComment(), comment) +
                        compareField("Статус", StatusResponse.valueOf(old.getStatus()).getName(), StatusResponse.valueOf(status).getName());
    }

    private String compareField(String name, String oldStr, String newStr) {
        return CompareHelper.compareField(name,oldStr,newStr);
    }
}
