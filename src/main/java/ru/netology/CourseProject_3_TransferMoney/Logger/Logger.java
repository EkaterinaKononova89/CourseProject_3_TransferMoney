package ru.netology.CourseProject_3_TransferMoney.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Logger {
    private static Logger logger;
    private final FileWriter logFW = new FileWriter("File.log", true);

    private Logger() throws IOException {}

    public String dateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalDate.now() + ", " + LocalTime.now().format(dtf);
    }

    public void log(String msg) throws IOException {
        logFW.write(dateTime()  + ": " + msg + "\n");
        logFW.flush();
    }

    public static Logger getInstance() throws IOException {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }
}
