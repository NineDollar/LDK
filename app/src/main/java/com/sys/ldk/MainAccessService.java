package com.sys.ldk;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.AutoCoreService;

public class MainAccessService extends AutoCoreService {

    @Override
    public void onAccessEvent(AccessibilityEvent event) {
        //如果需要通过监听除模拟点击之外的其他事情,再此写具体逻辑.否者无需任何操作
    }
}
