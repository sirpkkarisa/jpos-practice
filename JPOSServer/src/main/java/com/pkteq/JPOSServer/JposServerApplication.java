package com.pkteq.JPOSServer;

import org.jpos.q2.Q2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JposServerApplication {

	public static void main(String[] args) {
		Q2 q2 = new Q2("JPOSServer/deploy/");
		SpringApplication.run(JposServerApplication.class, args);
		q2.start();
	}

}
