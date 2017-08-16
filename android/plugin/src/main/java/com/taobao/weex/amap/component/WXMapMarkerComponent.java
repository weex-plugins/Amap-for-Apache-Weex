package com.taobao.weex.amap.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;

import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.amap.util.Constant;
import com.taobao.weex.amap.util.GifDecoder;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by budao on 2017/2/9.
 */
@WeexComponent(names = {"weex-amap-marker"})
public class WXMapMarkerComponent extends AbstractMapWidgetComponent<Marker> {

  public WXMapMarkerComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);

  }

  private static boolean isGif(String file) {
    FileInputStream imgFile = null;
    try {
      imgFile = new FileInputStream(file);
      byte[] header = new byte[3];
      int length = imgFile.read(header);
      return length == 3 && header[0] == (byte) 'G' && header[1] == (byte) 'I' && header[2] == (byte) 'F';
    } catch (Exception e) {
      // ignore
    } finally {
      if (imgFile != null) {
        try {
          imgFile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return false;
  }

  @Override
  protected View initComponentHostView(@NonNull Context context) {
    if (getParent() != null && getParent() instanceof WXMapViewComponent) {
      String title = (String) getDomObject().getAttrs().get(Constant.Name.TITLE);
      String icon = (String) getDomObject().getAttrs().get(Constant.Name.ICON);
      String position = getDomObject().getAttrs().get(Constant.Name.POSITION).toString();
      initMarker(title, position, icon);
    }
    // FixMe： 只是为了绕过updateProperties中的逻辑检查
    return new ViewStub(context);
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
  public void setTitle(final String title) {
    execAfterWidgetReady("setTitle", new Runnable() {
      @Override
      public void run() {
        setMarkerTitle(getWidget(), title);
      }
    });
  }

  @WXComponentProp(name = Constant.Name.ICON)
  public void setIcon(final String icon) {
    execAfterWidgetReady("setIcon", new Runnable() {
      @Override
      public void run() {
        setMarkerIcon(getWidget(), icon);
      }
    });
  }

  @WXComponentProp(name = Constant.Name.HIDE_CALL_OUT)
  public void setHideCallOut(final Boolean hide) {
    execAfterWidgetReady("setHideCallOut", new Runnable() {
      @Override
      public void run() {
        setMarkerHideCallOut(getWidget(), hide);
      }
    });
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

  @Override
  public void destroy() {
    super.destroy();
    if (getWidget() != null) {
      getWidget().remove();
    }
  }

  public Marker getMarker() {
    return getWidget();
  }

  public void onClick() {
    getInstance().fireEvent(getRef(), Constants.Event.CLICK);
  }

  private void initMarker(final String title, final String position, final String icon) {
    postMapOperationTask((WXMapViewComponent) getParent(), new WXMapViewComponent.MapOperationTask() {
      @Override
      public void execute(TextureMapView mapView) {
        final MarkerOptions markerOptions = new MarkerOptions();
        //设置Marker可拖动
        markerOptions.draggable(true);
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOptions.setFlat(true);
        Marker marker = mapView.getMap().addMarker(markerOptions);
        //setMarkerTitle(marker, title);
        //setMarkerPosition(marker, position);
        //setMarkerIcon(marker, icon);
        setWidget(marker);
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

  private void setMarkerIcon(@Nullable final Marker mMarker, final String icon) {

    if (TextUtils.isEmpty(icon) || mMarker == null) {
      return;
    }

    Uri rewrited = getInstance().rewriteUri(Uri.parse(icon), URIAdapter.IMAGE);
    if (Constants.Scheme.LOCAL.equals(rewrited.getScheme())) {
      Resources resources = getContext().getResources();
      List<String> segments = rewrited.getPathSegments();
      if (segments.size() == 1) {
        WXLogUtils.d(TAG, "Load marker icon from drawable: " + segments.get(0));
        int id = resources.getIdentifier(segments.get(0), "drawable", getContext().getPackageName());
        if (id != 0) {
          mMarker.setIcon(BitmapDescriptorFactory.fromResource(id));
          return;
        }
      }
    } else if ("path".equals(rewrited.getScheme())) {
      WXLogUtils.d(TAG, "Load marker icon from path: " + rewrited.getPath());
      mMarker.setIcon(BitmapDescriptorFactory.fromPath(rewrited.getPath()));
      return;
    }

    new AsyncTask<Void, String, Uri>() {

      @Override
      protected Uri doInBackground(Void... params) {
        try {
          return fetchIcon(icon, getContext().getExternalCacheDir());
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }

      @Override
      protected void onPostExecute(Uri result) {
        if (result != null && new File(result.getPath()).exists()) {
          if (isGif(result.getPath())) {
            GifDecoder gifDecoder = new GifDecoder();
            FileInputStream imgFile = null;
            try {

              imgFile = new FileInputStream(result.getPath());
              gifDecoder.read(imgFile);
              ArrayList<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();
              for (int i = 1; i < gifDecoder.getFrameCount(); i++) {
                Bitmap bitmap = gifDecoder.getFrame(i);
                if (bitmap != null && !bitmap.isRecycled()) {
                  bitmapDescriptors.add(BitmapDescriptorFactory.fromBitmap(bitmap));
                }
              }
              mMarker.setIcons(bitmapDescriptors);
              mMarker.setPeriod(2);

            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              if (imgFile != null) {
                try {
                  imgFile.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }

          } else {
            if (mMarker != null) {
              mMarker.setIcon(BitmapDescriptorFactory.fromPath(result.getPath()));
            }
          }

        }
      }
    }.execute();
  }

  private void setMarkerHideCallOut(@Nullable final Marker mMarker, Boolean hide) {
    if (mMarker != null) {
      if (hide) {
        mMarker.setClickable(!hide);
      }
    }
  }

  private void setMarkerPosition(@Nullable final Marker mMarker, String position) {
    try {
      JSONArray jsonArray = new JSONArray(position);
      LatLng latLng = new LatLng(jsonArray.optDouble(1), jsonArray.optDouble(0));
      if (mMarker != null) {
        MarkerOptions markerOptions = mMarker.getOptions();
        markerOptions.position(latLng);
        mMarker.setMarkerOptions(markerOptions);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void setMarkerTitle(@Nullable final Marker mMarker, String title) {
    if (mMarker != null) {
      MarkerOptions markerOptions = mMarker.getOptions();
      markerOptions.title(title);
      mMarker.setMarkerOptions(markerOptions);
    }
  }

  private Uri fetchIcon(String path, File cache) {
    String name = Uri.encode(path);
    File file = new File(cache, name);
    // 如果图片存在本地缓存目录，则不去服务器下载
    if (file.exists()) {
      return Uri.fromFile(file);
    } else {
      // 从网络上获取图片
      InputStream inputstream = null;
      FileOutputStream fileOutputStream = null;
      try {
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        if (conn.getResponseCode() == 200) {
          inputstream = conn.getInputStream();
          fileOutputStream = new FileOutputStream(file);
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = inputstream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
          }
          inputstream.close();
          fileOutputStream.close();
          // 返回一个URI对象
          return Uri.fromFile(file);
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (ProtocolException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (inputstream != null) {
          try {
            inputstream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (fileOutputStream != null) {
          try {
            fileOutputStream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

    }
    return null;
  }
}
