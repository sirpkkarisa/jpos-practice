package com.pkteq.JPOSServer.task.repositories;

import com.pkteq.JPOSServer.task.entities.business.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailsRepo extends JpaRepository<TransactionDetails,Long> {
}
