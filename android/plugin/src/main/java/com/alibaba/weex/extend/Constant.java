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

        String SCALECONTROL = "scalecontrol";
        String ZOOMCONTROL = "zoomcontrol";
        String COMPASS = "compass";
        String MYLOCATION = "mylocation";
        String GESTURE = "gesture";
        String INDOORSWITCH = "indoorswitch";

    }
}
