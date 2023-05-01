package com.spring.mongodbPractice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * this is for log
 *
 * @author raihan
 */
@Slf4j
@Component
public class LogUtils {
    /**
     * printing log without user
     *
     * @param message
     * @param path
     * @author riahan
     */
    public void printErrorLog(String message, String path) {
        log.error("Error happened due to: {}.", message);
        log.error("Error Path: {}", path);
    }

    /**
     * printing log with user
     *
     * @param message
     * @param path
     * @param user
     * @author raihan
     */
    public void printErrorLog(String message, String path, String user) {
        log.error("Error happened due to: {}. Requested by {}", message, user);
        log.error("Error Path: {}", path);
    }
}
