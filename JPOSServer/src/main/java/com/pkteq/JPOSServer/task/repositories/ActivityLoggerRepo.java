package com.pkteq.JPOSServer.task.repositories;

import com.pkteq.JPOSServer.task.entities.audit.ActivityLogger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLoggerRepo extends JpaRepository<ActivityLogger,Long> {
}
