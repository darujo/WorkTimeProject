package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "user_info_type")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "telegram_id")
    private Long telegramId;
    @Column(name = "thread_id")
    private Integer threadId;
    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public UserInfoType( String code, User user) {
        this.code = code;
        this.user =user;
    }

}
