package com.sys.ldk.weixin;

import android.view.accessibility.AccessibilityNodeInfo;


import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.List;

import static com.sys.ldk.serverset.Keyguard.context;


public class DGSX {

    public boolean startdgsx() {
//        打开微信
        User.Threadsleep(1);
        if (!openweixin()) {
            return false;
        }

//        判断是否在微信页面
        User.Threadsleep(2);
        if (!isPage()) {
            LogUtil.E("不在微信界面");
            return false;
        }else{

        }

//        进入打卡页面
        User.Threadsleep500();
        if (!clickdgsx()) {
            return false;
        }

//      填写
        User.Threadsleep500();
        if (!tianxiety(5000,"上班")) {
            return false;
        }

//        提交
//        User.Threadsleep500();
//        UiApi.clickNodeByTextWithTimeOut(5000, "提交");

//        返回桌面
        User.Threadsleep500();
        UiApi.backToDesk();

//        熄屏
        User.Threadsleep500();
        AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
        return true;
    }

    private boolean openweixin() {
        if (!User.startAPP(context, "com.tencent.mm", "com.tencent.mm.ui.LauncherUI")) {
            LogUtil.E("打开微信失败");
            return false;
        }
        LogUtil.D("微信启动成功");
        return true;
    }

    public boolean isPage() {
        String pageStr = "{maxMustMills:5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':[],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':['com.tencent.mm:id/cns'],'desc':[]}"
                + "}";

        return UiApi.isMyNeedPage(pageStr);
    }

    public boolean clickdgsx() {
        String temp1 = "{"
                + "'maxWClickMSec':5000,"
                + "'click':{'id':'com.tencent.mm:id/b4r'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':[],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}";

        String temp2 = "{"
                + "'maxWClickMSec':5000,"
                + "'click':{'text':'教学服务'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':[],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}";

        String temp3 = "{"
                + "'maxWClickMSec':5000,"
                + "'click':{'text':'顶岗实习'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':[],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}";

        return UiApi.jumpToNeedPage(new String[]{temp1, temp2, temp3});
    }

    //    查找输入框并输入，maxMills超时时间
    public boolean tianxiety(long maxMills, String inputtext) {

        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = 500;
        }
        while (true) {
            List<AccessibilityNodeInfo> accessibilityNodeInfoList = User.getEditableInfo();
            LogUtil.I(accessibilityNodeInfoList.size() + "");
            if (accessibilityNodeInfoList.size() == 1) {
                AcessibilityApi.inputTextByNode(accessibilityNodeInfoList.get(0), inputtext);
                LogUtil.D("输入成功");
                return true;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    ApiUtil.sleepTime(500);
                    LogUtil.D("未找到输入框");
                } else {
                    LogUtil.E("查找超时");
                    break;
                }
            }
        }
        return false;
    }
}

