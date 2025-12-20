package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_send")
public class UserSend {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private String chatId;
    @Column(name = "thread_id")
    private Integer threadId;
    @Column(name = "send")
    private Boolean send;
    @Column(name = "origin_message_id")
    private Integer originMessageId;
    @ManyToOne
    @JoinColumn(name = "mes_info_id")
    private MessageInformation messageInformation;

    public UserSend(String chatId, Integer threadId, Integer originMessageId, MessageInformation messageInformation) {
        this.chatId = chatId;
        this.messageInformation = messageInformation;
        this.send = false;
        this.threadId = threadId;
        this.originMessageId = originMessageId;
    }
}
