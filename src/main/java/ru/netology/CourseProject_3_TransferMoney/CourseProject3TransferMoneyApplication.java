package ru.netology.CourseProject_3_TransferMoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.netology.CourseProject_3_TransferMoney.logger.Logger;

import java.io.IOException;

@SpringBootApplication
public class CourseProject3TransferMoneyApplication {

    public static void main(String[] args) throws IOException {

        Logger logger = new Logger();
        logger.log("start");

        SpringApplication.run(CourseProject3TransferMoneyApplication.class, args);
    }
}
