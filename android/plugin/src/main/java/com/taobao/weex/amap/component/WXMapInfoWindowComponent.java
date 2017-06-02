package com.taobao.weex.amap.component;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.amap.util.Constant;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

/**
 * Created by budao on 2017/2/9.
 */

@WeexComponent(names = {"weex-amap-info-window"})
public class WXMapInfoWindowComponent extends AbstractMapWidgetContainer<Marker> {

  public WXMapInfoWindowComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);
  }

  @Override
  protected WXFrameLayout initComponentHostView(@NonNull Context context) {
    return new WXFrameLayout(context);
  }

  @Override
  protected void onHostViewInitialized(WXFrameLayout host) {
    super.onHostViewInitialized(host);
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      initMarker();
    }
  }

  @Override
  public void updateProperties(Map<String, Object> props) {
    // 属性重排序
    Object opened = props.remove(Constant.Name.OPEN);
    super.updateProperties(props);
    if (opened != null) {
      setOpened(WXUtils.getBoolean(opened, false));
    }
  }

  @Override
  protected boolean setProperty(String key, Object param) {
    WXLogUtils.d(TAG, "setProperty: " + key);
    switch (key) {
      case Constants.Name.POSITION:
        String position = WXUtils.getString(param, null);
        if (position != null)
          setPosition(position);
        return true;
    }
    return super.setProperty(key, param);
  }

  @WXComponentProp(name = Constant.Name.POSITION)
  public void setPosition(final String position) {
    execAfterWidgetReady("setPosition", new Runnable() {
      @Override
      public void run() {
        setMarkerPosition(getWidget(), position);
      }
    });
  }

  @WXComponentProp(name = Constant.Name.OFFSET)
  public void setOffset(final String offset) {
    execAfterWidgetReady("setOffset", new Runnable() {
      @Override
      public void run() {
        setMarkerInfoWindowOffset(getWidget(), offset);
      }
    });
  }

  @WXComponentProp(name = Constant.Name.OPEN)
  public void setOpened(final Boolean opened) {
    execAfterWidgetReady("setOpened", new Runnable() {
      @Override
      public void run() {
        Marker marker = getWidget();
        if (marker != null) {
          if (opened) {
            marker.showInfoWindow();
          } else {
            marker.hideInfoWindow();
          }
        }
      }
    });
  }

  @Override
  public void destroy() {
    super.destroy();
    if (getWidget() != null) {
      Marker marker = getWidget();
      if (getParent() != null && getParent() instanceof WXMapViewComponent) {
        ((WXMapViewComponent) getParent()).getCachedInfoWindow().remove(marker.getId());
      }
      marker.remove();
    } else {
      WXLogUtils.e(TAG, "Marker is null");
    }
  }

  private void initMarker() {
    final WXComponent parent = getParent();
    if (parent != null && parent instanceof WXMapViewComponent) {
      final WXMapViewComponent wxMapViewComponent = (WXMapViewComponent) parent;
      postMapOperationTask(wxMapViewComponent, new WXMapViewComponent.MapOperationTask() {
        @Override
        public void execute(TextureMapView mapView) {
          final MarkerOptions markerOptions = new MarkerOptions();
          //设置Marker可拖动, 将Marker设置为贴地显示，可以双指下拉地图查看效果
          markerOptions.setFlat(true);
          markerOptions.infoWindowEnable(true);
          //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.infowindow_marker_icon));
          markerOptions.title("");
          AMap mMap = mapView.getMap();
          final Marker marker = mMap.addMarker(markerOptions);
          marker.setClickable(false);
          wxMapViewComponent.getCachedInfoWindow().put(marker.getId(), WXMapInfoWindowComponent.this);
          setWidget(marker);
        }
      });
    }
  }

  private void setMarkerInfoWindowOffset(Marker marker, String position) {
    try {
      JSONArray jsonArray = new JSONArray(position);
      if (marker != null) {
        MarkerOptions markerOptions = marker.getOptions();
        markerOptions.setInfoWindowOffset(jsonArray.optInt(0), jsonArray.optInt(1));
        marker.setMarkerOptions(markerOptions);
      } else {
        WXLogUtils.e(TAG, "Marker is null!");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void setMarkerPosition(Marker marker, String position) {
    try {
      JSONArray jsonArray = new JSONArray(position);
      LatLng latLng = new LatLng(jsonArray.optDouble(1), jsonArray.optDouble(0));
      if (marker != null) {
        MarkerOptions markerOptions = marker.getOptions();
        markerOptions.position(latLng);
        marker.setMarkerOptions(markerOptions);
      } else {
        WXLogUtils.e(TAG, "Marker is null!");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
