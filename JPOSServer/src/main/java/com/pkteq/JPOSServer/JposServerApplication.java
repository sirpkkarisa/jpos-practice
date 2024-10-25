package com.pkteq.JPOSServer;

import com.pkteq.JPOSServer.task.entities.business.AccountBalanceDetail;
import com.pkteq.JPOSServer.task.entities.business.AccountDetail;
import com.pkteq.JPOSServer.task.entities.business.CustomerDetail;
import com.pkteq.JPOSServer.task.repositories.AccountBalanceRepo;
import com.pkteq.JPOSServer.task.repositories.AccountDetailRepo;
import com.pkteq.JPOSServer.task.repositories.CustomerDetailRepo;
import org.jpos.q2.Q2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
@ComponentScan(basePackages = "com.pkteq.JPOSServer")
public class JposServerApplication implements CommandLineRunner {

	private Q2 q2;

	public static void main(String[] args) {
		Q2 q2 = new Q2("JPOSServer/deploy/");
		SpringApplication.run(JposServerApplication.class, args);
	}

	@PostConstruct
	private void initQ2() {
		q2 = new Q2("JPOSServer/deploy/");
		q2.start();
	}

	@Autowired
	private CustomerDetailRepo customerDetailRepo;
	@Autowired
	private AccountBalanceRepo accountBalanceRepo;
	@Autowired
	private AccountDetailRepo accountDetailRepo;

	@Override
	@Transactional
	public void run(String... args) {
//		try {
//			CustomerDetail customer = CustomerDetail.builder()
//					.firstName("Pascal")
//					.lastName("Karisa")
//					.accountDetails(new ArrayList<>())
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			customerDetailRepo.save(customer);
//
//			AccountBalanceDetail accountBalanceDetail = AccountBalanceDetail.builder()
//					.balance(BigDecimal.valueOf(500000))
//					.createdAt(LocalDateTime.now())
//					.build();
//			accountBalanceRepo.save(accountBalanceDetail);
//
//			AccountDetail acc = AccountDetail.builder()
//					.customerDetail(customer)
//					.accountStatus("ACTIVE")
//					.accountClass("Individual")
//					.branch("HEAD OFFICE")
//					.accountType("Savings")
//					.accountNumber("1141231234")
//					.currency("KES")
//					.accountBalanceDetails(accountBalanceDetail)
//					.accountOpeningDate(LocalDateTime.now())
//					.build();
//			accountDetailRepo.save(acc);
//
//		} catch (Exception e) {
//			System.err.println("Error saving account details: " + e.getMessage());
//			e.printStackTrace();
//		}
	}
}
