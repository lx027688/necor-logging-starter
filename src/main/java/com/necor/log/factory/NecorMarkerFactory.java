package com.necor.log.factory;

import com.necor.log.constant.LogConstant;
import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMarkerBinder;

public class NecorMarkerFactory {
    static IMarkerFactory MARKER_FACTORY;

    private NecorMarkerFactory() {
    }

    private static IMarkerFactory bwCompatibleGetMarkerFactoryFromBinder() throws NoClassDefFoundError {
        return StaticMarkerBinder.SINGLETON.getMarkerFactory();
    }

    public static Marker getMarker(String name) {
        return MARKER_FACTORY.getMarker(name);
    }

    public static Marker getApiMarker() {
        return MARKER_FACTORY.getMarker(LogConstant.API_MARKER);
    }

    public static Marker getBusiMarker() {
        return MARKER_FACTORY.getMarker(LogConstant.BUSI_MARKER);
    }

    public static Marker getDetachedMarker(String name) {
        return MARKER_FACTORY.getDetachedMarker(name);
    }

    public static IMarkerFactory getIMarkerFactory() {
        return MARKER_FACTORY;
    }

    static {
        try {
            MARKER_FACTORY = bwCompatibleGetMarkerFactoryFromBinder();
        } catch (NoClassDefFoundError var1) {
            MARKER_FACTORY = new BasicMarkerFactory();
        } catch (Exception var2) {
            Util.report("Unexpected failure while binding MarkerFactory", var2);
        }
    }
}
