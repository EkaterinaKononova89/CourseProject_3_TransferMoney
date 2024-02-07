package ru.netology.CourseProject_3_TransferMoney;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@CommonsLog
public class CourseProject3TransferMoneyApplication {

	public static void main(String[] args) {

		log.info("start");

		SpringApplication.run(CourseProject3TransferMoneyApplication.class, args);
	}
}
