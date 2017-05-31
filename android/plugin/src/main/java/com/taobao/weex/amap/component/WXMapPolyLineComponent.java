package com.taobao.weex.amap.component;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;

import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.amap.util.Constant;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by budao on 2017/3/3.
 */
@WeexComponent(names = {"weex-amap-polyline"})
public class WXMapPolyLineComponent extends AbstractMapWidgetComponent<Polyline> {
  ArrayList<LatLng> mPosition = new ArrayList<>();
  private int mColor = 0;
  private String mStyle;
  private float mWeight = 1.0f;

  public WXMapPolyLineComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);
  }

  @Override
  protected View initComponentHostView(@NonNull Context context) {
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      initPolyLine();
    }
    // FixMe： 只是为了绕过updateProperties中的逻辑检查
    return new ViewStub(context);
  }

  @WXComponentProp(name = Constant.Name.PATH)
  public void setPath(String param) {
    try {
      JSONArray path = new JSONArray(param);
      if (path != null) {
        for (int i = 0; i < path.length(); i++) {
          JSONArray position = path.getJSONArray(i);
          mPosition.add(new LatLng(position.getDouble(1), position.getDouble(0)));
        }
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }
    execAfterWidgetReady("setPath", new Runnable() {
      @Override
      public void run() {
        Polyline polyline = getWidget();
        if (polyline != null) {
          polyline.setPoints(mPosition);
        }
      }
    });
  }

  @WXComponentProp(name = Constant.Name.STROKE_COLOR)
  public void setStrokeColor(String param) {
    mColor = Color.parseColor(param);
    execAfterWidgetReady("setStrokeColor", new Runnable() {
      @Override
      public void run() {
        Polyline polyline = getWidget();
        if (polyline != null) {
          polyline.setColor(mColor);
        }
      }
    });
  }

  @WXComponentProp(name = Constant.Name.STROKE_WIDTH)
  public void setStrokeWeight(float param) {
    mWeight = param;
    execAfterWidgetReady("setStrokeWeight", new Runnable() {
      @Override
      public void run() {
        Polyline polyline = getWidget();
        if (polyline != null) {
          polyline.setWidth(mWeight);
        }
      }
    });
  }

  @WXComponentProp(name = Constant.Name.STROKE_STYLE)
  public void setStrokeStyle(String param) {
    mStyle = param;
    execAfterWidgetReady("setStrokeStyle", new Runnable() {
      @Override
      public void run() {
        Polyline polyline = getWidget();
        if (polyline != null) {
          polyline.setDottedLine("dashed".equalsIgnoreCase(mStyle));
        }
      }
    });
  }

  private void initPolyLine() {
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      postMapOperationTask((WXMapViewComponent) getParent(), new WXMapViewComponent.MapOperationTask() {
        @Override
        public void execute(TextureMapView mapView) {
          PolylineOptions polylineOptions = new PolylineOptions();
          polylineOptions.setPoints(mPosition);
          polylineOptions.color(mColor);
          polylineOptions.width(mWeight);
          polylineOptions.setDottedLine("dashed".equalsIgnoreCase(mStyle));
          setWidget(mapView.getMap().addPolyline(polylineOptions));
        }
      });
    }
  }
}
