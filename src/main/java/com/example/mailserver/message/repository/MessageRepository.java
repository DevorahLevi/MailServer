package com.example.mailserver.message.repository;

import com.example.mailserver.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllBySenderAndSentOrderByCreatedDateDesc(String sender, boolean sent);

    List<Message> findAllByRecipientOrderByCreatedDateDesc(String sender);
}