package ru.netology.cloudstorage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(name = "files", schema = "dbo")
public class File {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;
    @Column(name = "TYPE", nullable = false)
    private String type;
    @Column(name = "CONTENT", nullable = false)
    private byte[] content;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;
    @Column(name = "FILE_SIZE", nullable = false)
    private long size;
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;


    public File(long id, String fileName, byte[] content, User user) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.user = user;
    }

    public File(long id, String fileName, User user) {
        this.id = id;
        this.fileName = fileName;
        this.user = user;
    }
}
