package com.alibaba.weex.extend;

/**
 * Created by aoxiao on 2017/1/4.
 */

public class Constant {

    public static interface Value {
        int SCROLLGESTURE = 0x1;
        int ZOOMGESTURE = 0x1 << 1;
        int TILTGESTURE = 0x1 << 2;
        int ROTATEGESTURE = 0x1 << 3;
    }

    public static interface Name {

        String SCALECONTROL = "scale";
        String ZOOM_ENABLE = "zoomEnable";
        String ZOOM = "zoom";
        String COMPASS = "compass";
        String GEOLOCATION = "geolocation";
        String GESTURE = "gesture";
        String INDOORSWITCH = "indoorswitch";
        String CENTER = "center";

    }
}
