package com.pkteq.JPOSServer.task.repositories;

import com.pkteq.JPOSServer.task.entities.business.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDetailRepo extends JpaRepository<CustomerDetail, Long> {
}
