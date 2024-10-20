package com.pkteq.JPOSServer.task.entities.business;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class AccountDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private String accountType;
    @Column(nullable = false)
    private String branch;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private String accountStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id")
    private AccountBalanceDetail accountBalanceDetails;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "accountDetail")
    private List<TransactionDetails> transactionDetails = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerDetail customerDetail;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime accountOpeningDate;
    private LocalDateTime accountDeletionDate;
    private LocalDateTime accountUpdateDate;
    @Column(nullable = false)
    private String accountClass;
}
