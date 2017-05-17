package com.taobao.weex.amap.component;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;

import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.amap.util.Constant;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by budao on 2017/3/3.
 */

@WeexComponent(names = {"weex-amap-circle"})
public class WXMapCircleComponent extends AbstractMapWidgetComponent {
  private Circle mCircle;
  private int mColor = 0;
  private int mFillColor = 0;
  private float mWeight = 1.0f;
  private float mRadius = 1.0f;

  public WXMapCircleComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);
  }

  @Override
  protected View initComponentHostView(@NonNull Context context) {
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      initCircle();
    }
    // FixMe： 只是为了绕过updateProperties中的逻辑检查
    return new ViewStub(context);
  }

  @WXComponentProp(name = Constant.Name.CENTER)
  public void setPath(final String param) {
    postTask(new Runnable() {
      @Override
      public void run() {
        try {
          JSONArray center = new JSONArray(param);
          if (center != null && center.length() == 2) {
            mCircle.setCenter(new LatLng(center.getDouble(1), center.getDouble(0)));
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  @WXComponentProp(name = Constant.Name.STROKE_COLOR)
  public void setStrokeColor(String param) {
    mColor = Color.parseColor(param);
    postTask(new Runnable() {
      @Override
      public void run() {
        mCircle.setStrokeColor(mColor);
      }
    });
  }

  @WXComponentProp(name = Constant.Name.FILL_COLOR)
  public void setFillColor(String param) {
    mFillColor = Color.parseColor(param);
    postTask(new Runnable() {
      @Override
      public void run() {
        mCircle.setFillColor(mFillColor);
      }
    });
  }

  @WXComponentProp(name = Constant.Name.STROKE_WIDTH)
  public void setStrokeWeight(float param) {
    mWeight = param;
    postTask(new Runnable() {
      @Override
      public void run() {
        mCircle.setStrokeWidth(mWeight);
      }
    });
  }

  @WXComponentProp(name = Constant.Name.RADIUS)
  public void setRadius(float param) {
    mRadius = param;
    postTask(new Runnable() {
      @Override
      public void run() {
        mCircle.setRadius(mRadius);
      }
    });
  }

  private void initCircle() {
    postMapOperationTask((WXMapViewComponent) getParent(), new WXMapViewComponent.MapOperationTask() {
      @Override
      public void execute(MapView mapView) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.strokeColor(mColor);
        circleOptions.strokeWidth(mWeight);
        circleOptions.radius(mRadius);
        circleOptions.fillColor(mFillColor);
        mCircle = mapView.getMap().addCircle(circleOptions);
      }
    });
  }
}
