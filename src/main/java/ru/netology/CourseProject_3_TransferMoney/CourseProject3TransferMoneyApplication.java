package ru.netology.CourseProject_3_TransferMoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.netology.CourseProject_3_TransferMoney.Logger.Logger;

import java.io.IOException;

@SpringBootApplication
@ComponentScan
public class CourseProject3TransferMoneyApplication {

	public static void main(String[] args) throws IOException {

		Logger logger = Logger.getInstance();
		logger.log("start");

		SpringApplication.run(CourseProject3TransferMoneyApplication.class, args);
	}

}
