package org.burrow_studios.obelisk.commons.logging;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.logging.*;

public final class LogTools {
    private final File dir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private LogTools(@NotNull File dir) {
        this.dir = dir;
        this.dir.mkdirs();
    }

    public static @NotNull LogTools get(@NotNull File logDir) {
        return new LogTools(logDir);
    }

    public void init() throws IOException {
        SimpleFormatter formatter = new SimpleFormatter();

        Logger rootLogger = Logger.getLogger("");

        for (Handler handler : rootLogger.getHandlers())
            if (handler instanceof ConsoleHandler)
                handler.setFormatter(formatter);

        try {
            rootLogger.addHandler(getFileHandler(formatter));
        } catch (IOException e) {
            throw new IOException("Could not create logging file handler", e);
        }
    }

    public @NotNull FileHandler getFileHandler(@NotNull Formatter formatter) throws IOException {
        String datePrefix = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()));

        // retrieve all existing log files from today
        File[] files = this.dir.listFiles((dir, name) -> name.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d_\\d+\\.log$") && name.startsWith(datePrefix));
        if (files == null)
            throw new NotDirectoryException(this.dir.getName());

        String fileName = datePrefix + "_" + files.length + ".log";

        // create File
        File logFile = new File(this.dir, fileName);


        // create FileHandler
        FileHandler fileHandler = new FileHandler(logFile.getPath(), true);
        fileHandler.setFormatter(formatter);
        return fileHandler;
    }
}
