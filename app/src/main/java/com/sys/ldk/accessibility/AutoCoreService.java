package com.sys.ldk.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.util.LogUtil;


public abstract class AutoCoreService extends AccessibilityService
{
    @Override
    public void onCreate() {
        super.onCreate();
        AcessibilityApi.setAccessibilityService(this);

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AcessibilityApi.setAccessibilityEvent(event);
        onAccessEvent(event);
    }

    @Override
    public void onInterrupt()
    {

    }

    public  abstract void  onAccessEvent(AccessibilityEvent event);

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
