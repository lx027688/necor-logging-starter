package com.necor.log.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.necor.log.bot.BotMsgPusherFactory;
import com.necor.log.bot.PushUtil;
import com.necor.log.config.LogbackProperties;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

public class LogFilter extends Filter<ILoggingEvent> {

    private LogbackProperties.AppenderProperties appenderProperties;


    public LogbackProperties.AppenderProperties getAppenderProperties() {
        return appenderProperties;
    }

    public void setAppenderProperties(LogbackProperties.AppenderProperties appenderProperties) {
        this.appenderProperties = appenderProperties;
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String markers = appenderProperties.getMarkers();
        if (event.getMarker() != null && !ObjectUtils.isEmpty(markers) &&
                Arrays.asList(markers.split(",")).stream().filter(x-> event.getMarker().contains(x)).count() > 0) {
            PushUtil.multiplePush(BotMsgPusherFactory.getBotMsgPusher(appenderProperties.getBot()), event);
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}
