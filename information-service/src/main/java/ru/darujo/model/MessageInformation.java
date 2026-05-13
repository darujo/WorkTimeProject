package ru.darujo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Column(name = "text", length = 4000)
    private String text;
    @Column(name = "is_send")
    private boolean isSend;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "file_for_disk")
    private String fileForDisk;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "title")
    private String title;

    public MessageInformation(Long id,
                              String author,
                              String type,
                              String title,
                              String text,
                              boolean isSend,
                              LocalDateTime dateTime,
                              Long projectId) {
        this.id = id;
        this.author = author;
        this.type = type;
        this.title = title;
        this.text = text;
        this.isSend = isSend;
        this.dateTime = dateTime == null ? LocalDateTime.now() : dateTime;
        this.projectId = projectId;
    }
}
