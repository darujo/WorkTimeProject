package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.darujo.assistant.helper.CompareHelper;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.ratestage.StatusRequest;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work_agreement_request")
public class WorkAgreementRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    @Column(name = "timestamp")
    private Timestamp timestamp;
    @Column(name = "version")
    private String version;
    @Column(name = "comment")
    private String comment;
    @Column(name = "term")
    private Timestamp term;
    @Column(name = "status")
    private String status;
    @Column(name = "work_id")
    private Long workId;

    @Override
    public String toString() {
        return
                "Пользователь: " + nikName + "\n" +
                        "Время: " + DataHelper.dateTimeToStr(timestamp) + "\n" +
                        (version != null ? ("Версия ТЗ: " + version + "\n") : "") +
                        (comment != null && !comment.isBlank() ? ("Комментарий: " + comment + "\n") : "") +
                        (term != null ? ("Срок: " + DataHelper.dateToDDMMYYYY(term) + "\n") : "") +
                        "Статус: " + StatusRequest.valueOf(status).getName() + "\n";

    }

    public String compareObj(WorkAgreementRequest old) {
        if (old == null) {
            return toString();
        }
        return
                compareField("Пользователь", old.getNikName(), nikName) +
                        compareField("Время", DataHelper.dateTimeToStr(old.getTimestamp()), DataHelper.dateTimeToStr(timestamp)) +
                        compareField("Версия ТЗ", old.getVersion(), version) +
                        compareField("Комментарий", old.getComment(), comment) +
                        compareField("Срок", DataHelper.dateToDDMMYYYY(old.getTerm()), DataHelper.dateToDDMMYYYY(term)) +
                        compareField("Статус", StatusRequest.valueOf(old.getStatus()).getName(), StatusRequest.valueOf(status).getName());
    }

    private String compareField(String name, String oldStr, String newStr) {
        return CompareHelper.compareField(name,oldStr,newStr);
    }

}
