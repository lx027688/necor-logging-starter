package com.necor.log.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.necor.log.constant.LogConstant;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class LogFilter  extends Filter<ILoggingEvent> {

    private String markerName;
    private List<String> markerNames = new ArrayList<>();

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public void setMarkerNames(List<String> markerNames) {
        this.markerNames = markerNames;
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMarker() != null) {
            if (ObjectUtils.isEmpty(markerNames) && !ObjectUtils.isEmpty(markerName) && event.getMarker().contains(markerName)) {
                return FilterReply.ACCEPT;
            }else if (!ObjectUtils.isEmpty(markerNames) && markerNames.stream().filter(x-> event.getMarker().contains(x)).count() > 0){
                return FilterReply.ACCEPT;
            }else {
                return FilterReply.DENY;
            }
        }
        return FilterReply.DENY;
    }
}
