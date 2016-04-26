/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package internal;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler {

    private static final Logger logger = Logger.getLogger("logger");
    private static FileHandler fh;

    public static void init() throws IOException {
        Date date = new Date();
        String time = date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds() + "_" + date.getDate() + "_" + (date.getMonth() + 1) + "_" + (date.getYear() + 1900);
        fh = new FileHandler("log_" + time);
        logger.addHandler(fh);
        fh.setFormatter(new SimpleFormatter());
        fh.setEncoding("UTF-8");
    }

    public static void logMessage(String message) {
        logger.info(message);
    }

    public static void logException(String className, String method, Throwable exception) {
        logger.log(Level.INFO, "{0} -> {1} throws {2}", new Object[]{className, method, exception.getMessage()});
    }

}
