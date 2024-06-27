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
import java.util.Map;

public class LogbackService {

    private LogbackProperties properties;

    public LogbackService(LogbackProperties properties) {
        this.properties = properties;
        init();
        configureLogger();
    }

    /**
     * 初始化配置
     */
    public void init() {
        // 获取自定义的appender
        Map<String, LogbackProperties.AppenderProperties> appenders = this.properties.getAppenders();
        // 加入一个api appender
        addAppenderIfAbsent(appenders, "api", LogConstant.API_MARKER);
        // 加入一个busi appender
        addAppenderIfAbsent(appenders, "busi", LogConstant.BUSI_MARKER);
        // 为appender marker 添加一个默认的marker， 默认的marker为为appender的name (不区分大小写)
        appenders.forEach((key, value) -> {
            if (ObjectUtils.isEmpty(value.getMarkers())) {
                value.setMarkers(key);
            } else {
                String[] markers = value.getMarkers().toLowerCase().split(",");
                if (!Arrays.asList(markers).contains(key.toLowerCase())) {
                    value.setMarkers(key + "," + value.getMarkers());
                }
            }
        });
    }

    /**
     * appenders 不包含某个appender的时候添加这个appender
     * @param appenders appender容器
     * @param key appender name
     * @param marker appender marker
     */
    private void addAppenderIfAbsent(Map<String, LogbackProperties.AppenderProperties> appenders, String key, String marker) {
        appenders.computeIfAbsent(key, k -> {
            LogbackProperties.AppenderProperties appenderProperties = new LogbackProperties.AppenderProperties();
            appenderProperties.setMarkers(marker);
            return appenderProperties;
        });
    }

    private void configureLogger() {
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        Logger rootLogger = (Logger)LoggerFactory.getLogger("ROOT");
        this.properties.getAppenders().forEach((key, appender) -> {
            int appenderMaxHistory = appender.getMaxHistory();
            if (appenderMaxHistory > 365*10) {
                LogMaxHistoryException.maxHistoryException();
            }
            long appenderMaxFileSize = appender.getMaxFileSize();
            if (appenderMaxFileSize > 1024*10) {
                LogMaxHistoryException.maxHistoryException();
            }
            String appenderMarkers = appender.getMarkers();
            if (ObjectUtils.isEmpty(appenderMarkers)) {
                LogMarkerException.markerEmptyException();
            }
            if (appenderMarkers.length() > 30) {
                LogMarkerException.markerLengthException();
            }

            SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP();
            FileSize maxFileSize = FileSize.valueOf(appenderMaxFileSize+"mb");
            sizeAndTimeBasedFNATP.setMaxFileSize(maxFileSize);

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

            TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy();
            rollingPolicy.setContext(rootLogger.getLoggerContext());
            rollingPolicy.setParent(rollingFileAppender);
            rollingPolicy.setFileNamePattern(file.toString());
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
            rollingPolicy.setMaxHistory(appenderMaxHistory);
            rollingPolicy.start();

            String filters = appender.getFilters();
            if (!ObjectUtils.isEmpty(filters)) {
                Arrays.stream(filters.split(",")).forEach(clazz -> {
                    try {
                        Class<Filter<ILoggingEvent>> cl = (Class<Filter<ILoggingEvent>>) Class.forName(clazz);
                        Filter<ILoggingEvent> filter = cl.getDeclaredConstructor().newInstance();
                        if (filter instanceof LogFilter) {
                            ((LogFilter) filter).setAppenderProperties(appender);
                        }
                        rollingFileAppender.addFilter(filter);
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            }


            rollingFileAppender.setRollingPolicy(rollingPolicy);
            rollingFileAppender.start();
            rootLogger.setLevel(Level.toLevel(this.properties.getLevel()));
            rootLogger.addAppender(rollingFileAppender);
        });
    }
}
