package ru.darujo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Entity
@Table(name = "message_information")
public class MessageInformation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author")
    private String author;

    @Column(name = "type")
    private String type;
    @Column(name = "text")
    private String text;
    @Column(name = "is_send")
    private boolean isSend;

    @Column(name = "date_time")
    private Timestamp dateTime;

    public MessageInformation(Long id, String author, String type, String text, boolean isSend, Timestamp dateTime) {
        this.id = id;
        this.author = author;
        this.type = type;
        this.text = text;
        this.isSend = isSend;
        this.dateTime = dateTime == null ? new Timestamp(System.currentTimeMillis()) : dateTime;

    }
}
