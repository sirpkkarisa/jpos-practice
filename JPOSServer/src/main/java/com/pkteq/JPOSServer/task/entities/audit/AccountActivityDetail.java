package com.pkteq.JPOSServer.task.entities.audit;


import com.pkteq.JPOSServer.task.entities.business.CustomerDetail;
import com.pkteq.JPOSServer.task.entities.business.TransactionDetails;
import com.pkteq.JPOSServer.task.enums.ActivityStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class AccountActivityDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private ActivityStatus activityStatus;
    @ManyToOne
    @JoinColumn(name = "initiator_customer_id", nullable = false)
    private CustomerDetail customerDetail;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionDetails transactionDetails;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",nullable = false)
    private LocalDateTime receivedAt;
    private LocalDateTime completedAt;
}
