package com.taobao.weex.amap.component;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;

import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
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
@WeexComponent(names = {"weex-amap-polygon"})
public class WXMapPolygonComponent extends AbstractMapWidgetComponent<Polygon> {
  ArrayList<LatLng> mPosition = new ArrayList<>();
  private int mColor = 0;
  private int mFillColor = 0;
  private float mWidth = 1.0f;

  public WXMapPolygonComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);
  }

  @Override
  protected View initComponentHostView(@NonNull Context context) {
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      initPolygon();
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
        Polygon polygon = getWidget();
        if (polygon != null) {
          polygon.setPoints(mPosition);
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
        Polygon polygon = getWidget();
        if (polygon != null) {
          polygon.setStrokeColor(mColor);
        }
      }
    });
  }

  @WXComponentProp(name = Constant.Name.FILL_COLOR)
  public void setFillColor(String param) {
    mFillColor = Color.parseColor(param);
    execAfterWidgetReady("setFillColor", new Runnable() {
      @Override
      public void run() {
        Polygon polygon = getWidget();
        if (polygon != null) {
          polygon.setFillColor(mFillColor);
        }
      }
    });
  }

  @WXComponentProp(name = Constant.Name.STROKE_WIDTH)
  public void setStrokeWidth(float param) {
    mWidth = param;
    execAfterWidgetReady("setStrokeWidth", new Runnable() {
      @Override
      public void run() {
        Polygon polygon = getWidget();
        if (polygon != null) {
          polygon.setStrokeWidth(mWidth);
        }
      }
    });
  }

  private void initPolygon() {
    postMapOperationTask((WXMapViewComponent) getParent(), new WXMapViewComponent.MapOperationTask() {
      @Override
      public void execute(TextureMapView mapView) {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(mPosition);
        polygonOptions.strokeColor(mColor);
        polygonOptions.strokeWidth(mWidth);
        setWidget(mapView.getMap().addPolygon(polygonOptions));
      }
    });
  }

  public boolean contains(LatLng latLng) {
      Polygon polygon = getWidget();
      return polygon != null && polygon.contains(latLng);
  }
}
