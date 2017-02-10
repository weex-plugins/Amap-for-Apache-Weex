package com.alibaba.weex.amap.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.weex.amap.Constant;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXReflectionUtils;
import com.taobao.weex.utils.WXUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by budao on 2017/2/9.
 */

public class WxMapMarkerComponent extends WXComponent<View> {
  private Marker mMarker;
  private MapView mMapView;
  private AMap mAMap;

  public WxMapMarkerComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, String instanceId, boolean isLazy) {
    super(instance, dom, parent, instanceId, isLazy);
  }

  public WxMapMarkerComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
    super(instance, dom, parent, isLazy);
  }

  public WxMapMarkerComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);

  }

  @Override
  protected View initComponentHostView(@NonNull Context context) {
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      mMapView = ((WXMapViewComponent) getParent()).getHostView();
      mAMap = mMapView.getMap();
      String title = (String) getDomObject().getAttrs().get(Constant.Name.TITLE);
      String icon = (String) getDomObject().getAttrs().get(Constant.Name.ICON);
      String position = getDomObject().getAttrs().get(Constant.Name.POSITION).toString();
      initMarker(title, position, icon);
    }
    return mMapView;
  }

//  public void updateProperties(Map<String, Object> props) {
//    if (props == null) {
//      return;
//    }
//
//    for(String key : props.keySet()) {
//      Object param = props.get(key);
//      String value = WXUtils.getString(param, null);
//      if (TextUtils.isEmpty(value)) {
//        param = convertEmptyProperty(key);
//      }
//      if(!setProperty(key, param)){
//        Invoker invoker = mHost.getPropertyInvoker(key);
//        if (invoker != null) {
//          try {
//            Type[] paramClazzs = invoker.getParameterTypes();
//            if (paramClazzs.length != 1) {
//              WXLogUtils.e("[WXComponent] setX method only one parameter：" + invoker);
//              return;
//            }
//            param = WXReflectionUtils.parseArgument(paramClazzs[0],props.get(key));
//            invoker.invoke(this, param);
//          } catch (Exception e) {
//            WXLogUtils.e("[WXComponent] updateProperties :" + "class:" + getClass() + "method:" + invoker.toString() + " function " + WXLogUtils.getStackTrace(e));
//          }
//        }
//      }
//    }
//  }
  @WXComponentProp(name = Constant.Name.TITLE)
  public void setTitle(String title) {
    setMarkerTitle(title);
  }

  @WXComponentProp(name = Constant.Name.ICON)
  public void setIcon(String icon) {
    setMarkerIcon(icon);
  }

  @WXComponentProp(name = Constant.Name.POSITION)
  public void setPosition(String position) {
    setMarkerPosition(position);
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

  private void initMarker(String title, String position, String icon) {
    final MarkerOptions markerOptions = new MarkerOptions();
    //设置Marker可拖动
    markerOptions.draggable(true);
    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
    markerOptions.setFlat(true);
    mMarker = mAMap.addMarker(markerOptions);
    setMarkerTitle(title);
    setMarkerPosition(position);
    setMarkerIcon(icon);
  }

  private void setMarkerIcon(String icon) {
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
            mMarker.setIcon(BitmapDescriptorFactory.fromView(imageView));
          }
        });
        wxImageStrategy.placeHolder = icon;
        adapter.setImage(icon, imageView, WXImageQuality.NORMAL, wxImageStrategy);
      }
    }
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
