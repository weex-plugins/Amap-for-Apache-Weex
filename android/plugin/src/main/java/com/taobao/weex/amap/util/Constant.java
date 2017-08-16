package com.taobao.weex.amap.util;

/**
 * Created by aoxiao on 2017/1/4.
 */

public class Constant {

  public interface Value {
    int SCROLLGESTURE = 0x1;
    int ZOOMGESTURE = 0x1 << 1;
    int TILTGESTURE = 0x1 << 2;
    int ROTATEGESTURE = 0x1 << 3;
    String RIGHT_CENTER = "center";
    String RIGHT_BOTTOM = "bottom";
  }

  public interface Name {

    // mapview
    String SCALECONTROL = "scale";
    String ZOOM_ENABLE = "zoomEnable";
    String ZOOM = "zoom";
    String COMPASS = "compass";
    String GEOLOCATION = "geolocation";
    String GESTURE = "gesture";
    String GESTURES = "gestures";
    String INDOORSWITCH = "indoorswitch";
    String CENTER = "center";
    String KEYS = "sdkKey";
    String ZOOM_POSITION = "zoomPosition";
    String MY_LOCATION_ENABLED = "myLocationEnabled";
    String SHOW_MY_LOCATION = "showMyLocation";
    String CUSTOM_STYLE_PATH = "customStylePath";
    String CUSTOM_ENABLED = "customEnabled";

    // marker
    String MARKER = "marker";
    String POSITION = "position";
    String ICON = "icon";
    String TITLE = "title";
    String HIDE_CALL_OUT = "hideCallout";

    // polyline
    String PATH = "path";
    String STROKE_COLOR = "strokeColor";
    String STROKE_WIDTH = "strokeWidth";
    String STROKE_OPACITY = "strokeOpacity";
    String STROKE_STYLE = "strokeStyle";

    // circle
    String RADIUS = "radius";
    String FILL_COLOR = "fillColor";

    // offset
    String OFFSET = "offset";
    String OPEN = "open";
  }


  public static interface EVENT {
    String ZOOM_CHANGE = "zoomchange";
    String DRAG_CHANGE = "dragend";
  }
}
