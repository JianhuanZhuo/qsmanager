package cn.keepfight.qsmanager;

import javafx.application.Application;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * loader of client application.
 * Created by tom on 2017/5/30.
 */
public class AppLoader {
    private static Logger logger;

    public static void main(String[] args) throws IOException {
        System.setProperty("rootPath", new File("./").getCanonicalFile().getAbsolutePath());
        logger = Logger.getLogger(AppLoader.class);
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println(t.toString());
            System.err.println("Throwable: " + e.getMessage());
            logger.error("DefaultUncaughtExceptionHandler from " + t, e);
        });
        Application.launch(cn.keepfight.qsmanager.QSApp.class, args);
    }
}
