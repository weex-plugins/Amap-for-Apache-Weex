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

public abstract class AbstractMapWidgetComponent extends WXComponent<View> {

    protected static final String TAG = "WXMapViewComponent";
    private List<Runnable> mPaddingTasks = new ArrayList<>();
    private AtomicBoolean mIsMapLoaded = new AtomicBoolean(false);

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
                        execPaddingTasks();
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

    protected void execPaddingTasks() {
        for (Runnable task : mPaddingTasks) {
            task.run();
            WXLogUtils.d(TAG, "Exec padding widget task " + task.toString());
        }
        mPaddingTasks.clear();
    }

    protected void postTask(final String friendlyName, final Runnable task) {

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

        if (mIsMapLoaded.get()) {
            WXLogUtils.d(TAG, "Map loaded, exec task " + task.toString() + "immediately");
            wrapper.run();
        } else {
            mPaddingTasks.add(wrapper);
            WXLogUtils.d(TAG, "Map not ready, cache task " + task.toString());
        }
    }

    @Deprecated
    protected void postTask(final Runnable task) {
        postTask(task.toString(), task);
    }
}
