package com.necor.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "necor.log")
public class LogbackProperties {

    private String level = "INFO";

    private FileProperties file;

    private Map<String, AppenderProperties> appenders = new HashMap<>();

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    // Getters and Setters
    public FileProperties getFile() {
        return file;
    }

    public void setFile(FileProperties file) {
        this.file = file;
    }

    public Map<String, AppenderProperties> getAppenders() {
        return appenders;
    }

    public void setAppenders(Map<String, AppenderProperties> appenders) {
        this.appenders = appenders;
    }

    public static class FileProperties {
        private String prefix;
        private String path;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

    public static class AppenderProperties {

        private long maxFileSize = 100;
        private int maxHistory = 30;
        private String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n";

        private String markers;

        private String filters = "com.necor.log.filter.LogFilter";

        private BotProperties bot;

        public String getFilters() {
            return filters;
        }

        public void setFilters(String filters) {
            this.filters = filters;
        }

        public String getMarkers() {
            return markers;
        }

        public void setMarkers(String markers) {
            this.markers = markers;
        }

        public long getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public int getMaxHistory() {
            return maxHistory;
        }

        public void setMaxHistory(int maxHistory) {
            this.maxHistory = maxHistory;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public BotProperties getBot() {
            return bot;
        }

        public void setBot(BotProperties bot) {
            this.bot = bot;
        }

        public static class BotProperties {
            private String weixin;

            private String lark;

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public String getLark() {
                return lark;
            }

            public void setLark(String lark) {
                this.lark = lark;
            }
        }
    }

}
