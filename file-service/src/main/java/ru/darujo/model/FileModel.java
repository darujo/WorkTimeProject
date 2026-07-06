package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "file_model")
public class FileModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "object_type")
    private String objectType;
    @Column(name = "object_id")
    private String objectId;
    @Column(name = "file_for_disk")
    private String fileForDisk;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "delete")
    private Boolean delete;
    @Column(name = "time_create")
    private LocalDateTime create_time;

}
