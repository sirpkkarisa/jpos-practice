package com.pkteq.JPOSServer.task.repositories;

import com.pkteq.JPOSServer.task.entities.business.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDetailRepo extends JpaRepository<AccountDetail, Long> {
//    @Query(value = "SELECT * FROM account_detail where account_number=:accountNumber",nativeQuery = true)
    Optional<AccountDetail> findAccountDetailByAccountNumber(String accountNumber);

}
