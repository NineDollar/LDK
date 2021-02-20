package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sys.ldk.accessibility.api.User.Threadsleep;

public class AutoVideo {
    //    最大观看时间5分钟
    private static int MAXshijina = 5 * 2;
    private static int times = 10;
    private static int count = 0;

    public static boolean auto_video() {
        AcessibilityApi.clickTextViewByText("电视台");
        User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText("第一频道");
        User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText("学习视频");
        User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText("学习视频");
        /*User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText("看理论");
        User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText("看理论");*/
        User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText("第一频道");
        User.Threadsleep(2);
        if (!startvido()) {
            return false;
        }
        if (count < times) {
            LogUtil.D("观看第二次");
            User.Threadsleep(2);
            List<AccessibilityNodeInfo> List = User.getScrollNodeInfo();
            AcessibilityApi.ScrollNode(List.get(4));
            User.Threadsleep(2);
            startvido();
        }
        return true;
    }
    private static boolean startvido(){
        List<String> stringList = User.getallInfottext(false);
        List<String> list = new ArrayList<>();

        for (String s : stringList
        ) {
            if (AutoVideo.Data(s)) {
                list.add(s);
            }
        }
        list.remove(0);
        count = list.size();
        if (count > times) {
            count = times;
        }
        LogUtil.D("视频数量： " + count);
        for (String s : list
        ) {
            LogUtil.E(s);
        }
        for (String s : list
        ) {
            User.Threadsleep(1);
            AcessibilityApi.clickTextViewByText(s);
//                判断观看是否结束
            int shijian = 0;
            while (true) {
                shijian += 1;
                if (watch_end() || shijian >= MAXshijina) {
                    if (!XXQG.XXQGBACK("欢迎发表你的观点")) {
                        return false;
                    }
                    break;
                }
            }
        }
        LogUtil.D("视频观看完毕");
        return true;
    }

    //    观看是否结束
    private static boolean watch_end() {
        if (AcessibilityApi.findViewByText("重新播放") != null) {
            LogUtil.E("观看完毕");
            return true;
        } else {
            Threadsleep(1);
        }
        return false;
    }

    public static Boolean Data(String str) {
        String regex = "[0-9]{2}:[0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        boolean dateFlag = m.matches();
        if (!dateFlag) {
            return false;
        }
        return true;
    }
}
