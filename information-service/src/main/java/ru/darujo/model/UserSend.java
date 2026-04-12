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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mes_info_id")
    private MessageInformation messageInformation;
    @Column(name = "sender_type")
    private String senderType;

    public UserSend(String senderType, String chatId, Integer threadId, Integer originMessageId, MessageInformation messageInformation) {
        this.senderType = senderType;
        this.chatId = chatId;
        this.messageInformation = messageInformation;
        this.send = false;
        this.threadId = threadId;
        this.originMessageId = originMessageId;
    }
}
