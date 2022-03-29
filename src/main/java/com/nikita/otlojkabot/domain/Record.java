package com.nikita.otlojkabot.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Record {

    @Id
    private long id;
    private String fileId;
    private String comment;
    private String dataType;
    private LocalDateTime createDateTime;
    private LocalDateTime postDateTime;
    private String author;

}
