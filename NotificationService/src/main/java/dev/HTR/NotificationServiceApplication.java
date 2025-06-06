package dev.HTR;

import dev.HTR.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {


	private static JwtUtils jwtUtils;

	@Autowired
	public void setCacheServ(JwtUtils jwtUtils){
		NotificationServiceApplication.jwtUtils = jwtUtils;
	}

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);

		System.out.println(jwtUtils.generateTokenWithUserId(1L));
	}

}
