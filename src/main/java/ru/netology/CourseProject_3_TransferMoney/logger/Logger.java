package ru.netology.CourseProject_3_TransferMoney.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Logger {
    private static Logger logger = null;
    private final static String TIME = "HH:mm:ss";
    private final FileWriter logFW = new FileWriter("File.log", true);


    private Logger() throws IOException {
    }

    public String dateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME);
        return LocalDate.now() + ", " + LocalTime.now().format(dtf);
    }

    public void log(String msg) throws IOException {
        logFW.write(dateTime() + ": " + msg + "\n");
        logFW.flush();
    }

    public static Logger getInstance() throws IOException {
        if (logger == null) {
            synchronized (Logger.class) {
                if (logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
    }
}
