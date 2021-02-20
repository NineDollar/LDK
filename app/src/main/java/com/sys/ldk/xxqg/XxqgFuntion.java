package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>大国共用方法</p>
 *
 * @author: Nine_Dollar
 * @date: 2021/2/20
 */
public class XxqgFuntion {


    /**
     * 用于点击listview
     * @param id listview每条数据都是一样的id
     * @param text 通过text确定info
     * @return List<AccessibilityNodeInfo>
     */
    public static List<AccessibilityNodeInfo> listinfo(String id, String text) {
        int j = 0;
        List<AccessibilityNodeInfo> Idlist = new ArrayList<>();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        if (accessibilityNodeInfoList.isEmpty()) {
            LogUtil.W("未找到info");
            return null;
        }
        LogUtil.V("总共：" + accessibilityNodeInfoList.size());
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            j++;
            if (id.equals(a.getViewIdResourceName() + "")) {
                if (text.equals(accessibilityNodeInfoList.get(j).getText() + "")) {
                    LogUtil.V("text：" + a.getText());
                    Idlist.add(a);
                }
            }
        }
        if (Idlist.isEmpty()) {
            LogUtil.W("未找到");
            return null;
        }
        LogUtil.V("新闻总共：" + Idlist.size());
        return Idlist;
    }
}
