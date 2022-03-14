package com.example.mailserver.email.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMAIL")
public class Email {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "MESSAGE_CONTENT")
    private String messageContent;

    @Column(name = "RECIPIENT")
    private String recipient;

    @Column(name = "SENDER")
    private String sender;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @UpdateTimestamp
    private Date updatedDate;
}
