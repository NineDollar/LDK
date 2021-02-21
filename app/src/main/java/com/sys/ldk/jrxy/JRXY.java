package com.sys.ldk.jrxy;

import android.content.Context;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

public class JRXY {
    public static boolean openjrxy(Context context) {
        LogUtil.I("打开今日校园");
//        返回桌面
        UiApi.backToDesk();
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
//      打开今日校园
        startLearning_power(context, "com.wisedu.cpdaily", "com.wisorg.wisedu.home.ui.HomeActivity");
//        判断是否在当前页面
        if (!isxxqj_power()) {
            return false;
        }

//       进入打卡页面
        if (!into_page()) {
            return false;
        }
        return true;
    }

    public static boolean into_page() {
        int n;
        if(ThreadSleepTime.sleep2()){
        return true;
    }
        AcessibilityApi.clickTextViewByText("我的大学");

//        查找信息收集并点击
        if(ThreadSleepTime.sleep2()){
        return true;
    }
        if(AcessibilityApi.clickTextViewByText("信息收集")){

        }

        ThreadSleepTime.sleep3();
//        判断是否发布
        if (User.findtext("暂无信息收集")) {
            LogUtil.E("暂无信息收集");
            return false;
        }

//      点击报送表单
        ThreadSleepTime.sleep3();
        n = 0;
        while (!User.clik_text_Info("未填写 >")) {
            n += 1;
            if (n > 3) {
                LogUtil.D("点击 未填写 > 失败");
                return false;
            }
        }

//      开始填写
        if(ThreadSleepTime.sleep2()){
        return true;
    }
        TianXie.tianxie();
        return true;
    }

    private static boolean isxxqj_power() {
//        判断页面次数
        int frequency = 3;
//        判断是否进入软件
        while (frequency > 0) {
            LogUtil.I("" + frequency);
            if (ThreadSleepTime.sleep1()) {
            return false;
        }
            if (isPage()) {
                LogUtil.I("已进入今日校园");
                return true;
            } else {
                LogUtil.E("当前不在今日校园界面,暂停操作");
            }
            frequency--;
        }
        LogUtil.D("进入今日校园失败");
        return false;
    }

    private static boolean isPage() {
        String pageStr = "{maxMustMills:5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':['我的大学'],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}";

        return UiApi.isMyNeedPage(pageStr);
    }

    private static boolean startLearning_power(Context context, String pkg, String cls) {
        try {
            if (User.startAPP(context, pkg, cls)) {
                LogUtil.D("APP启动成功");
                return true;
            } else {
                LogUtil.E("包名不正确或未安装");
                return false;
            }
        } catch (Exception e) {
            LogUtil.E(e.toString());
        }
        return false;
    }

}
