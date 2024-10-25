package com.pkteq.JPOSServer.task.repositories;

import com.pkteq.JPOSServer.task.entities.audit.AccountActivityDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountActivityDetailRepo extends JpaRepository<AccountActivityDetail,Long> {
}
