package com.example.mailserver.email.repository;

import com.example.mailserver.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {

    List<Email> findAllBySenderOrderByCreatedDateDesc(String sender);

    List<Email> findAllByRecipientOrderByCreatedDateDesc(String sender);
}
