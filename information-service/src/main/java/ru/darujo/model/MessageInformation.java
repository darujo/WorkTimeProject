package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.darujo.dto.information.MessageType;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "massage_information")
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


//    @OneToMany
//    private List<UserSend> users;

}
