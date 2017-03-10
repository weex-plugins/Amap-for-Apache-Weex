package com.alibaba.weex.amap.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.weex.amap.util.Constant;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXUtils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by budao on 2017/2/9.
 */

public class WXMapInfoWindowComponent extends WXVContainer<LinearLayout> {
  LinearLayout frameLayout;
  private Marker mMarker;
  private MapView mMapView;
  private AMap.InfoWindowAdapter mInfoWindowAdapter;

  public WXMapInfoWindowComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);

  }

  @Override
  protected LinearLayout initComponentHostView(@NonNull Context context) {
    frameLayout = new LinearLayout(context);
    frameLayout.setLayoutParams(new LinearLayout.LayoutParams(1,1));
    // frameLayout.setBackgroundColor(Color.TRANSPARENT);
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      mMapView = ((WXMapViewComponent) getParent()).getHostView();
      boolean open = (Boolean) getDomObject().getAttrs().get(Constant.Name.OPEN);
      String offset = (String) getDomObject().getAttrs().get(Constant.Name.ICON);
      String position = getDomObject().getAttrs().get(Constant.Name.POSITION).toString();
      initMarker(open, position, offset);
    }
    // FixMe： 只是为了绕过updateProperties中的逻辑检查
    return frameLayout;
  }

  @Override
  protected boolean setProperty(String key, Object param) {
    switch (key) {
      case Constants.Name.POSITION:
        String position = WXUtils.getString(param, null);
        if (position != null)
          setPosition(position);
        return true;
    }
    return super.setProperty(key, param);
  }

  @WXComponentProp(name = Constant.Name.TITLE)
  public void setTitle(String title) {
    setMarkerTitle(title);
  }

  @WXComponentProp(name = Constant.Name.POSITION)
  public void setPosition(String position) {
    setMarkerPosition(position);
    Log.v("WXMapInfoWindow ", position);
  }

  @WXComponentProp(name = Constant.Name.OFFSET)
  public void setOffset(String offset) {
    //mMarker.showInfoWindow();
    Log.v("WXMapInfoWindow ", offset);
  }

  @WXComponentProp(name = Constant.Name.OPEN)
  public void setOpened(Boolean offset) {
    if (offset) {
      mMarker.showInfoWindow();
    } else {
      mMarker.hideInfoWindow();
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    if (mMarker != null) {
      mMarker.remove();
    }
  }

  public Marker getMarker() {
    return mMarker;
  }

  public void onClick() {
    getInstance().fireEvent(getRef(), Constants.Event.CLICK);
  }

  private void initMarker(boolean open, String position, String icon) {
    final MarkerOptions markerOptions = new MarkerOptions();
    //设置Marker可拖动
    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
    markerOptions.setFlat(true);
    AMap mMap = mMapView.getMap();
    mInfoWindowAdapter = new AMap.InfoWindowAdapter() {

      @Override
      public View getInfoWindow(Marker marker) {
        render(marker, frameLayout);
        return frameLayout;
      }

      @Override
      public View getInfoContents(Marker marker) {
        render(marker, frameLayout);
        return frameLayout;
      }

      public void render(Marker marker, ViewGroup viewGroup) {
        mMapView.removeView(frameLayout);
        Log.v("WXMapInfoWindow ", String.valueOf(viewGroup.getChildCount()));
      }
    };
    mMap.setInfoWindowAdapter(mInfoWindowAdapter);
    mMarker = mMap.addMarker(markerOptions);
    mMarker.setClickable(true);
    if (open) {
      mMarker.showInfoWindow();
    } else {
      mMarker.hideInfoWindow();
    }
    setMarkerTitle("");
    setMarkerPosition(position);
  }

  private void setMarkerPosition(String position) {
    try {
      JSONArray jsonArray = new JSONArray(position);
      LatLng latLng = new LatLng(jsonArray.optDouble(1), jsonArray.optDouble(0));
      mMarker.setPosition(latLng);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void setMarkerTitle(String title) {
    mMarker.setTitle(title);
  }
}
