package com.pkteq.JPOSServer.task.repositories;

import com.pkteq.JPOSServer.task.entities.business.AccountBalanceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepo extends JpaRepository<AccountBalanceDetail,Long> {
}
