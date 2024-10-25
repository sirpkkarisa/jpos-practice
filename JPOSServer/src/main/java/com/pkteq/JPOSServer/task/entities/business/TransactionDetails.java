package com.pkteq.JPOSServer.task.entities.business;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pkteq.JPOSServer.task.enums.TransactionStatus;
import com.pkteq.JPOSServer.task.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class TransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private AccountDetail accountDetail;
    @Column(nullable = false)
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",nullable = false)
    private LocalDateTime createdAt;
}
