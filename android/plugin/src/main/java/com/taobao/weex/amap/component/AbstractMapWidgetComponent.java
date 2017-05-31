package com.taobao.weex.amap.component;

import android.view.View;

import com.amap.api.maps.TextureMapView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by moxun on 17/5/17.
 */

public abstract class AbstractMapWidgetComponent<Widget> extends WXComponent<View> {

    protected static final String TAG = "WXMapViewComponent";
    private List<Runnable> mPaddingWidgetTasks = new ArrayList<>();
    private AtomicBoolean mIsMapLoaded = new AtomicBoolean(false);
    private Widget mWidget;

    public AbstractMapWidgetComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
    }

    protected void postMapOperationTask(WXMapViewComponent parent, final WXMapViewComponent.MapOperationTask task) {
        if (parent != null) {
            WXMapViewComponent.MapOperationTask wrapper = new WXMapViewComponent.MapOperationTask() {
                @Override
                public void execute(TextureMapView mapView) {
                    try {
                        setMapLoaded(true);
                        task.execute(mapView);
                    } catch (Throwable t) {
                        WXLogUtils.w(TAG, t);
                    }
                }
            };
            parent.postTask(wrapper);
        }
    }

    protected void setMapLoaded(boolean loaded) {
        mIsMapLoaded.set(loaded);
    }

    protected void execPaddingWidgetTasks() {
        for (Runnable task : mPaddingWidgetTasks) {
            task.run();
            WXLogUtils.d(TAG, "Exec padding widget task " + task.toString());
        }
        mPaddingWidgetTasks.clear();
    }

    protected void setWidget(Widget widget) {
        mWidget = widget;
        if (mWidget != null) {
            execPaddingWidgetTasks();
        }
    }

    protected void execAfterWidgetReady(final String friendlyName, final Runnable task) {
        Runnable wrapper = new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Throwable t) {
                    WXLogUtils.w(TAG, t);
                }
            }

            @Override
            public String toString() {
                return friendlyName;
            }
        };

        if (mWidget != null) {
            WXLogUtils.d(TAG, "Widget is ready, execute task " + friendlyName + " immediately");
            wrapper.run();
        } else {
            WXLogUtils.d(TAG, "Widget is not ready, cache task " + friendlyName);
            mPaddingWidgetTasks.add(wrapper);
        }
    }

    protected Widget getWidget() {
        if (mWidget == null) {
            WXLogUtils.w(TAG, new Throwable("Widget is null"));
        }
        return mWidget;
    }
}
