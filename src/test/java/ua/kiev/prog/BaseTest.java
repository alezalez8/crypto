package ua.kiev.prog;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

public abstract class BaseTest {
    final static Logger logger = Logger.getLogger(BaseTest.class);

    protected void log(String message) {
        logger.trace(message);
    }

    protected void deleteFiles(List<String> files) {
        files.forEach((file) -> new File(file).delete());
    }
}

