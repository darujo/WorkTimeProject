package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @Column(name = "send")
    private Boolean send;
    @ManyToOne
    @JoinColumn(name = "mes_info_id")
    private MessageInformation messageInformation;

    public UserSend(String chatId, MessageInformation messageInformation) {
        this.chatId = chatId;
        this.messageInformation = messageInformation;
    }
}
