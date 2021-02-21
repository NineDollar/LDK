package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sys.ldk.xxqg.ReturnType.FAILURE;
import static com.sys.ldk.xxqg.ReturnType.SUCCESS;
import static com.sys.ldk.xxqg.ReturnType.my_stop;

public class Tikot {
    public static int tikot(String questiontype) {
//        填空类型
        int LX;
//        获取题目
        List<String> alltextlistbefore = User.getallInfottext(false);
        List<String> timu = IntoAnswer.gettimu(questiontype);
        for (String s : timu
        ) {
            LogUtil.D("题目：" + s + ", length：" + s.length());
        }

//       拿到答案选项
        HashMap<String, AccessibilityNodeInfo> hashMaptextInfo = new HashMap<>();
        HashMap<String, AccessibilityNodeInfo> hashMapabcdInfo = new HashMap<>();
        List<String> listabcdtext = new ArrayList<>();
        IntoAnswer.abcdInfoandtext(hashMaptextInfo, hashMapabcdInfo, listabcdtext);

//      拿到提示
        User.clik_text_Info("查看提示");

        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }
        List<String> alltextlistafter = User.getallInfottext(false);

        String tishistr = alltextlistafter.get(alltextlistbefore.size() - 3);
        for (String s : alltextlistafter
        ) {
            if (s.indexOf("正确答案：") > 0) {
                tishistr = s;
            }
        }
        if (tishistr.isEmpty()) {
            tishistr = alltextlistafter.get(alltextlistbefore.size() - 2);
        }
        if (tishistr.equals("null")) {
            LogUtil.E("提示信息： " + null + "， 返回");
            return FAILURE;
        }

        LogUtil.D("提示： " + tishistr);

        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }

        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        //        获得输入框Info
        List<AccessibilityNodeInfo> inputInfo = IntoAnswer.getinputInfo();

        if (tishistr.equals("请观看视频")) {
            inputdnfalse(inputInfo);

            if (ThreadSleepTime.sleep0D2()) {
                return my_stop;
            }
            return IntoAnswer.cliknext(null);
        }

        //      开始填空
//        获取横线开头和末尾字符
        HashMap<Integer, String[]> wtbeginAndendmap = getwtbeginAndendmap(timu);

        //        获得答案
        List<String> dnlist = getdnstr(wtbeginAndendmap, tishistr);
        if (dnlist.isEmpty()) {
            LX = 0;
        } else {
            LX = 1;
        }
//        填写答案
        switch (LX) {
            case 0:
                LogUtil.E("正则表达式未找到答案，跳过");
                inputdnfalse(inputInfo);
                break;
            case 1:
                LogUtil.D("找到答案");
                inputdn(inputInfo, dnlist);
                break;
            default:
                LogUtil.E("出错");
                return FAILURE;
        }
        if (ThreadSleepTime.sleep0D2()) {
            return my_stop;
        }
        int q = IntoAnswer.cliknext(null);
        LogUtil.W("q: " + q);
        return q;
    }

    //   随便输入答案
    private static void inputdnfalse(List<AccessibilityNodeInfo> inputInfo) {
        for (AccessibilityNodeInfo a : inputInfo
        ) {
            AcessibilityApi.inputTextByNode(a, "未找到");
            break;
        }
    }

    private static void inputdn(List<AccessibilityNodeInfo> inputInfo, List<String> dnlist) {
        int n = 0;
        for (AccessibilityNodeInfo a : inputInfo
        ) {
            AcessibilityApi.inputTextByNode(a, dnlist.get(n));
            n += 1;
        }
    }

    private static HashMap<Integer, String[]> getwtbeginAndendmap(List<String> timu) {
        HashMap<Integer, String[]> wentibeginAndendhashMap = IntoAnswer.getwentideginAndend(timu);
        return wentibeginAndendhashMap;
    }

    private static List<String> getdnstr(HashMap<Integer, String[]> getwtbeginAndendmap, String tishistr) {
        List<String> dnlist = new ArrayList<>();
        String wtbegin;
        String wtend;

        for (Map.Entry e : getwtbeginAndendmap.entrySet()
        ) {
            wtbegin = Objects.requireNonNull(getwtbeginAndendmap.get(e.getKey()))[0];
            wtend = Objects.requireNonNull(getwtbeginAndendmap.get(e.getKey()))[1];

//            匹配规则
            String regex = wtbegin + "(.*?)" + wtend;
            LogUtil.D("匹配规则： " + regex);
            Pattern pattern = Pattern.compile(regex);
            // 内容 与 匹配规则 的测试
            Matcher matcher = pattern.matcher(tishistr);
            if (matcher.find()) {
                // 不包含前后的两个字符
                String dn = matcher.group(1);
                LogUtil.D("匹配到答案： " + dn);
                dnlist.add(dn);
            } else {
                LogUtil.E("未匹配到答案");
            }
        }
        return dnlist;
    }

}
