package com.necor.log.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.necor.log.constant.LogConstant;
import com.necor.log.exception.LogMarkerException;
import com.necor.log.exception.LogMaxHistoryException;
import com.necor.log.filter.LogFilter;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class LogbackService {

    private LogbackProperties properties;

    public LogbackService(LogbackProperties properties) {
        this.properties = properties;
        configureLogger();
    }

    private void configureLogger() {
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        Logger rootLogger = (Logger)LoggerFactory.getLogger("ROOT");
        this.properties.getFile().getAppenders().forEach((key, appender) -> {
            int appenderMaxHistory = appender.getMaxHistory();
            if (appenderMaxHistory > 365*10) {
                LogMaxHistoryException.maxHistoryException();
            }
            long appenderMaxFileSize = appender.getMaxFileSize();
            if (appenderMaxFileSize > 1024*10) {
                LogMaxHistoryException.maxHistoryException();
            }

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(loggerContext);
            encoder.setPattern(appender.getPattern());
            encoder.start();

            RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender();
            rollingFileAppender.setContext(rootLogger.getLoggerContext());

            StringBuilder file = new StringBuilder(this.properties.getFile().getPath());
            file.append(LogConstant.FILE_SEPARATION).append(this.properties.getFile().getPrefix()).append(LogConstant.FILE_JOINER).append(key).append(LogConstant.FILE_SUFFIX);

            rollingFileAppender.setFile(file.toString());
            rollingFileAppender.setEncoder(encoder);

            file = new StringBuilder(this.properties.getFile().getPath());
            file.append(LogConstant.FILE_SEPARATION).append(key).append(LogConstant.FILE_SEPARATION).append(this.properties.getFile().getPrefix()).append(LogConstant.FILE_JOINER).append(key).append(LogConstant.FILE_JOINER).append("%d{yyyy-MM-dd}.%i").append(LogConstant.FILE_SUFFIX);

            SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP();
            FileSize maxFileSize = FileSize.valueOf(appenderMaxFileSize+"mb");
            sizeAndTimeBasedFNATP.setMaxFileSize(maxFileSize);

            TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy();
            rollingPolicy.setContext(rootLogger.getLoggerContext());
            rollingPolicy.setParent(rollingFileAppender);
            rollingPolicy.setFileNamePattern(file.toString());
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
            rollingPolicy.setMaxHistory(appenderMaxHistory);
            rollingPolicy.start();


            String filters = appender.getFilters();
            if (ObjectUtils.isEmpty(filters)) {
                String appenderMarkers = appender.getMarkers();
                if (ObjectUtils.isEmpty(appenderMarkers)) {
                    LogMarkerException.markerEmptyException();
                }
                if (appenderMarkers.length() > 30) {
                    LogMarkerException.markerLengthException();
                }
                LogFilter logFilter = new LogFilter();
                List<String> markers = Arrays.asList(appenderMarkers.split(","));
                logFilter.setMarkerNames(markers);
                rollingFileAppender.addFilter(logFilter);
            }else {
                String[] classes = filters.split(",");
                for (String clazz : classes) {
                    try {
                        Class<Filter<ILoggingEvent>> cl = (Class<Filter<ILoggingEvent>>) Class.forName(clazz);
                        Filter<ILoggingEvent> filter = cl.getDeclaredConstructor().newInstance();
                        rollingFileAppender.addFilter(filter);
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            rollingFileAppender.setRollingPolicy(rollingPolicy);
            rollingFileAppender.start();
            rootLogger.setLevel(Level.toLevel(this.properties.getLevel()));
            rootLogger.addAppender(rollingFileAppender);
        });
    }
}
