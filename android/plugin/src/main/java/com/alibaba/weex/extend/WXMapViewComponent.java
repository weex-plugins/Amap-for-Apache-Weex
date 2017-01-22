package com.alibaba.weex.extend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;

/**
 * create by 2016/12/15
 *
 * @author guibao.ggb
 * @email guibao.ggb@alibaba-inc.com
 *
 */
public class WXMapViewComponent extends WXComponent implements LocationSource, AMapLocationListener {

    private MapView mMapView;
    private AMap mAMap;
    private UiSettings uiSettings;

    private boolean scaleControl = true;
    private boolean zoomControl = true;
    private boolean compass = true;
    private boolean myLocation = false;
    private int gesture = 0xF;
    private boolean indoorSwitch = false;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
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

        uiSettings.setScaleControlsEnabled(scaleControl);
        uiSettings.setZoomControlsEnabled(zoomControl);
        uiSettings.setCompassEnabled(compass);
        uiSettings.setIndoorSwitchEnabled(indoorSwitch);

        setMyLocationStatus(myLocation);
        updateGestureSetting();

    }

    private void updateGestureSetting() {
        if ((gesture & 0xF) == 0xF) {
            uiSettings.setAllGesturesEnabled(true);
        }
        else {
            if ((gesture & Constant.Value.SCROLLGESTURE) == Constant.Value.SCROLLGESTURE) {
                uiSettings.setScrollGesturesEnabled(true);
            }
            else {
                uiSettings.setScrollGesturesEnabled(false);
            }

            if ((gesture & Constant.Value.ZOOMGESTURE) == Constant.Value.ZOOMGESTURE) {
                uiSettings.setZoomGesturesEnabled(true);
            }
            else {
                uiSettings.setZoomGesturesEnabled(false);
            }

            if ((gesture & Constant.Value.TILTGESTURE) == Constant.Value.TILTGESTURE) {
                uiSettings.setTiltGesturesEnabled(true);
            }
            else {
                uiSettings.setTiltGesturesEnabled(false);
            }

            if ((gesture & Constant.Value.ROTATEGESTURE) == Constant.Value.ROTATEGESTURE) {
                uiSettings.setRotateGesturesEnabled(true);
            }
            else {
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
        WXLogUtils.d("weex", "onActivityPause");
        deactivate();
    }

    @Override
    public void onActivityResume() {
        mMapView.onResume();
        WXLogUtils.d("weex", "onActivityResume");
    }

    @Override
    public void onActivityDestroy() {
        mMapView.onDestroy();
        WXLogUtils.d("weex", "onActivityDestroy");

        if(mlocationClient != null){
            mlocationClient.onDestroy();
        }
    }

    @WXComponentProp(name=Constant.Name.SCALECONTROL)
    public void setScaleControl(boolean scaleControl) {
        WXLogUtils.d("WxMapView: scaleControl is set to " + scaleControl);
        this.scaleControl = scaleControl;
        uiSettings.setScaleControlsEnabled(scaleControl);
    }

    @WXComponentProp(name=Constant.Name.ZOOMCONTROL)
    public void setZoomControl(boolean zoomControl) {
        WXLogUtils.d("WxMapView: zoomControl is set to " + zoomControl);
        this.zoomControl = zoomControl;
        uiSettings.setZoomControlsEnabled(zoomControl);
    }

    @WXComponentProp(name=Constant.Name.COMPASS)
    public void setCompass(boolean compass) {
        WXLogUtils.d("WxMapView: compass is set to " + compass);
        this.compass = compass;
        uiSettings.setCompassEnabled(compass);
    }

    @WXComponentProp(name=Constant.Name.MYLOCATION)
    public void setMyLocation(boolean myLocation) {
        WXLogUtils.d("WxMapView: myLocation is set to " + myLocation);
        this.myLocation = myLocation;
        setMyLocationStatus(myLocation);
    }

    @WXComponentProp(name=Constant.Name.GESTURE)
    public void setGesture(int gesture) {
        WXLogUtils.d("WxMapView: gesture is set to " + gesture);
        this.gesture = gesture;
        updateGestureSetting();
    }

    @WXComponentProp(name=Constant.Name.INDOORSWITCH)
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
        }
        else {
            deactivate();
            mAMap.setLocationSource(null);
            mAMap.setMyLocationEnabled(false);
            uiSettings.setMyLocationButtonEnabled(false);
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mAMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                WXLogUtils.e("AmapErr",errText);
            }
        }
    }
}
