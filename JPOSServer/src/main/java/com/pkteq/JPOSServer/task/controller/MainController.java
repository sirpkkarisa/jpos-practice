package com.pkteq.JPOSServer.task.controller;

import com.pkteq.JPOSServer.task.entities.business.AccountBalanceDetail;
import com.pkteq.JPOSServer.task.entities.business.AccountDetail;
import com.pkteq.JPOSServer.task.entities.business.CustomerDetail;
import com.pkteq.JPOSServer.task.repositories.AccountBalanceRepo;
import com.pkteq.JPOSServer.task.repositories.AccountDetailRepo;
import com.pkteq.JPOSServer.task.repositories.CustomerDetailRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class MainController {
    @Autowired
    private CustomerDetailRepo customerDetailRepo;
    @Autowired
    private AccountBalanceRepo accountBalanceRepo;
    @Autowired
    private AccountDetailRepo accountDetailRepo;

//    @PostConstruct
//    public void init() {
//        try {
//            CustomerDetail customer = CustomerDetail.builder()
//                    .firstName("Pascal")
//                    .lastName("Karisa")
//                    .build();
//
//            customerDetailRepo.save(customer);
//
//            AccountBalanceDetail accountBalanceDetail = AccountBalanceDetail.builder()
//                    .balance(BigDecimal.valueOf(500000))
//                    .build();
//            accountBalanceRepo.save(accountBalanceDetail);
//
//
//            AccountDetail acc = AccountDetail.builder()
//                    .customerDetail(customer)
//                    .accountStatus("ACTIVE")
//                    .branch("HEAD OFFICE")
//                    .accountType("Savings")
//                    .accountNumber("1141231234")
//                    .currency("KES")
//                    .accountBalanceDetails(accountBalanceDetail)
//                    .build();
//            accountDetailRepo.save(acc);
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
