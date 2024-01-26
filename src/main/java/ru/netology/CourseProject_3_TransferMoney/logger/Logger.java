package ru.netology.CourseProject_3_TransferMoney.logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Configuration
@Scope("singleton")
public class Logger {
    private final static String TIME = "HH:mm:ss";

    @Value("${log-file.name}")
    private String fileName;

    private final FileWriter logFW = new FileWriter(fileName, true);
    //private final FileWriter logFW = new FileWriter("File.log", true);

    public Logger() throws IOException {
    }

    public String dateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME);
        return LocalDate.now() + ", " + LocalTime.now().format(dtf);
    }

    public void log(String msg) throws IOException {
        logFW.write(dateTime() + ": " + msg + "\n");
        logFW.flush();
    }
}
