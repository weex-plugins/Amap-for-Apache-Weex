package com.alibaba.weex.extend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class WXMapViewComponent extends WXComponent implements LocationSource, AMapLocationListener {

  private MapView mMapView;
  private AMap mAMap;
  private UiSettings uiSettings;

  private boolean isScaleEnable = true;
  private boolean isZoomEnable = true;
  private boolean compass = true;
  private boolean myLocation = false;
  private int gesture = 0xF;
  private boolean indoorSwitch = false;

  private OnLocationChangedListener mListener;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;

  public WXMapViewComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
    super(instance, dom, parent, isLazy);
    registerActivityStateListener();
  }

  @Override
  protected View initComponentHostView(@NonNull Context context) {
    mMapView = new MapView(context);
    mMapView.onCreate(null);
    initMap();
    return mMapView;
  }

  private void initMap() {
    if (mAMap == null) {
      mAMap = mMapView.getMap();
      setUpMap();
    }
  }

  private void setUpMap() {
    uiSettings = mAMap.getUiSettings();

    uiSettings.setScaleControlsEnabled(isScaleEnable);
    uiSettings.setZoomControlsEnabled(isZoomEnable);
    uiSettings.setCompassEnabled(compass);
    uiSettings.setIndoorSwitchEnabled(indoorSwitch);

    setMyLocationStatus(myLocation);
    updateGestureSetting();

  }

  private void updateGestureSetting() {
    if ((gesture & 0xF) == 0xF) {
      uiSettings.setAllGesturesEnabled(true);
    } else {
      if ((gesture & Constant.Value.SCROLLGESTURE) == Constant.Value.SCROLLGESTURE) {
        uiSettings.setScrollGesturesEnabled(true);
      } else {
        uiSettings.setScrollGesturesEnabled(false);
      }

      if ((gesture & Constant.Value.ZOOMGESTURE) == Constant.Value.ZOOMGESTURE) {
        uiSettings.setZoomGesturesEnabled(true);
      } else {
        uiSettings.setZoomGesturesEnabled(false);
      }

      if ((gesture & Constant.Value.TILTGESTURE) == Constant.Value.TILTGESTURE) {
        uiSettings.setTiltGesturesEnabled(true);
      } else {
        uiSettings.setTiltGesturesEnabled(false);
      }

      if ((gesture & Constant.Value.ROTATEGESTURE) == Constant.Value.ROTATEGESTURE) {
        uiSettings.setRotateGesturesEnabled(true);
      } else {
        uiSettings.setRotateGesturesEnabled(false);
      }
    }
  }

  @JSMethod
  public void setMyLocationButtonEnabled(boolean enabled) {

    if (uiSettings != null) {
      uiSettings.setMyLocationButtonEnabled(enabled);

    }
  }

  @Override
  public void onActivityCreate() {
    Log.e("weex", "onActivityCreate");
  }

  @Override
  public void onActivityPause() {
    mMapView.onPause();
    deactivate();
  }

  @Override
  public void onActivityResume() {
    mMapView.onResume();
  }

  @Override
  public void onActivityDestroy() {
    mMapView.onDestroy();
    if (mLocationClient != null) {
      mLocationClient.onDestroy();
    }
  }

  @WXComponentProp(name = Constant.Name.SCALECONTROL)
  public void setScaleEnable(boolean scaleEnable) {
    this.isScaleEnable = scaleEnable;
    uiSettings.setScaleControlsEnabled(scaleEnable);
  }

  @WXComponentProp(name = Constant.Name.ZOOM_ENABLE)
  public void setZoomEnable(boolean zoomEnable) {
    this.isZoomEnable = zoomEnable;
    uiSettings.setZoomControlsEnabled(zoomEnable);
  }

  @WXComponentProp(name = Constant.Name.ZOOM)
  public void setZoom(int level) {
    mAMap.moveCamera(CameraUpdateFactory.zoomTo(level));
  }

  @WXComponentProp(name = Constant.Name.COMPASS)
  public void setCompass(boolean compass) {
    this.compass = compass;
    uiSettings.setCompassEnabled(compass);
  }

  @WXComponentProp(name = Constant.Name.GEOLOCATION)
  public void setMyLocation(boolean myLocation) {
    this.myLocation = myLocation;
    setMyLocationStatus(myLocation);
  }

  @WXComponentProp(name = Constant.Name.CENTER)
  public void setCenter(String location) {
    try {
      JSONArray jsonArray = new JSONArray(location);
      LatLng latLng = new LatLng(jsonArray.optDouble(1), jsonArray.optDouble(0));
      mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @WXComponentProp(name = Constant.Name.MARKER)
  public void setMarker(String markers) {
    try {
      JSONArray jsonArray = new JSONArray(markers);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.optJSONObject(i);
        if (jsonObject != null) {
          JSONArray position = jsonObject.optJSONArray("position");
          String title = jsonObject.optString("title");
          String icon = jsonObject.optString("icon");
          if (position != null) {
            LatLng latLng = new LatLng(position.optDouble(1), position.optDouble(0));
            final MarkerOptions markerOptions = new MarkerOptions();
            //设置Marker可拖动
            markerOptions.draggable(true);
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOptions.setFlat(true);
            if (latLng != null) {
              markerOptions.position(latLng);
            }
            if (!TextUtils.isEmpty(title)) {
              markerOptions.title(title);
            }
            if (!TextUtils.isEmpty(icon)) {
              IWXImgLoaderAdapter adapter = WXSDKManager.getInstance().getIWXImgLoaderAdapter();
              ImageView imageView = new ImageView(getContext());
              imageView.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
              imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
              if (adapter != null) {
                WXImageStrategy wxImageStrategy = new WXImageStrategy();
                wxImageStrategy.setImageListener(new WXImageStrategy.ImageListener() {
                  @Override
                  public void onImageFinish(String url, ImageView imageView, boolean result, Map extra) {
                    imageView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    markerOptions.icon(BitmapDescriptorFactory.fromView(imageView));
                    mAMap.addMarker(markerOptions);
                  }
                });
                wxImageStrategy.placeHolder = icon;
                adapter.setImage(icon, imageView, WXImageQuality.NORMAL, wxImageStrategy);

              }
            } else {
              mAMap.addMarker(markerOptions);
            }
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @WXComponentProp(name = Constant.Name.GESTURE)
  public void setGesture(int gesture) {
    this.gesture = gesture;
    updateGestureSetting();
  }

  @WXComponentProp(name = Constant.Name.INDOORSWITCH)
  public void setIndoorSwitch(boolean indoorSwitch) {
    this.indoorSwitch = indoorSwitch;
    uiSettings.setIndoorSwitchEnabled(indoorSwitch);
  }

  public void setMyLocationStatus(boolean isActive) {

    if (isActive) {
      mAMap.setLocationSource(this);// 设置定位监听
      uiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
      mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
      // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
      mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    } else {
      deactivate();
      mAMap.setLocationSource(null);
      mAMap.setMyLocationEnabled(false);
      uiSettings.setMyLocationButtonEnabled(false);
    }
  }

  @Override
  public void activate(OnLocationChangedListener listener) {
    mListener = listener;
    if (mLocationClient == null) {
      mLocationClient = new AMapLocationClient(getContext());
      mLocationOption = new AMapLocationClientOption();
      //设置定位监听
      mLocationClient.setLocationListener(this);
      //设置为高精度定位模式
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      //设置定位参数
      mLocationClient.setLocationOption(mLocationOption);
      // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
      // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
      // 在定位结束后，在合适的生命周期调用onDestroy()方法
      // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
      mLocationClient.startLocation();
    }
  }

  @Override
  public void deactivate() {
    mListener = null;
    if (mLocationClient != null) {
      mLocationClient.stopLocation();
      mLocationClient.onDestroy();
    }
    mLocationClient = null;
  }

  @Override
  public void onLocationChanged(AMapLocation amapLocation) {
    if (mListener != null && amapLocation != null) {
      if (amapLocation != null
          && amapLocation.getErrorCode() == 0) {
        mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
        // mAMap.moveCamera(CameraUpdateFactory.zoomTo(18));
      } else {
        String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
        WXLogUtils.e("AmapErr", errText);
      }
    }
  }
}
