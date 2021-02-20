package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sys.ldk.xxqg.ReturnType.CLIK;
import static com.sys.ldk.xxqg.ReturnType.FAILURE;
import static com.sys.ldk.xxqg.ReturnType.OVER;
import static com.sys.ldk.xxqg.ReturnType.SUCCESS;

public class IntoAnswer {
    public static int intoanswer() {
        ThreadSleepTime.sleepshort();
        int ReFlag;
        LogUtil.D("开始自动答题");
//        关闭键盘
        AcessibilityApi.closeKeyBoard();
        String questiontype = getquestiontype();
        String questiontypeflag = questiontype.length() > 3 ? questiontype.substring(0, 3) : questiontype;

        switch (questiontypeflag) {
            case "多选题":
                ReFlag = Doxt.doxt(questiontype);
                switch (ReFlag) {
                    case CLIK:
                        LogUtil.D("多选题跳过");
                    case SUCCESS:
                        LogUtil.D("多选题成功");
                        break;
                    case FAILURE:
                        LogUtil.E("多选题失败");
                        break;
                    case OVER:
                        LogUtil.D("多选题答题完成");
                        break;
                }
                break;
            case "填空题":
                ReFlag = Tikot.tikot(questiontype);
                switch (ReFlag) {
                    case CLIK:
                        LogUtil.D("跳过填空题");
                        break;
                    case SUCCESS:
                        LogUtil.D("填空题成功");
                        break;
                    case FAILURE:
                        LogUtil.E("填空题失败");
                        break;
                    case OVER:
                        LogUtil.D("填空题答题完成");
                        break;
                }
                break;
            case "单选题":
                ReFlag = Dxt.dxt(questiontype);
                switch (ReFlag) {
                    case CLIK:
                        LogUtil.D("单选题跳过");
                        break;
                    case SUCCESS:
                        LogUtil.D("单选题成功");
                        break;
                    case FAILURE:
                        LogUtil.E("单选题失败");
                        break;
                    case OVER:
                        LogUtil.D("单选题答题完成");
                        break;
                }
                break;
            default:
                LogUtil.E("未找到题型");
                return FAILURE;
        }
//eg:
//        点击答案
     /*   abcdInfoandtext(null, null, null);
        HashMap<String, AccessibilityNodeInfo> hashMaptextInfo = new HashMap<>();
        HashMap<String, AccessibilityNodeInfo> hashMapabcdInfo = new HashMap<>();*/

//     点击查看提示
/*        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("查看提示", 0);
        AccessibilityNodeInfo a = getallInfotextandafterInfo(hashMap).get("查看提示");
        AcessibilityApi.performViewClick(a);*/

//     点击确定
/*        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("确定", 0);
        AccessibilityNodeInfo a = getallInfotextandafterInfo(hashMap).get("确定");
        AcessibilityApi.performViewClick(a);*/

        return ReFlag;
    }

    //    获取ABCD的text和Info
    public static boolean abcdInfoandtext(HashMap<String, AccessibilityNodeInfo> hashMaptextInfo, HashMap<String, AccessibilityNodeInfo> hashMapabcdInfo, List<String> listabcdtext) {
        int n = 0;
        if (hashMaptextInfo == null) {
            hashMaptextInfo = new HashMap<>();
        }
        if (hashMapabcdInfo == null) {
            hashMapabcdInfo = new HashMap<>();
        }
        if (listabcdtext == null) {
            listabcdtext = new ArrayList<>();
        }

        List<AccessibilityNodeInfo> accessibilityNodeInfoList = new ArrayList<>();
        AcessibilityApi.getAllNode(null, accessibilityNodeInfoList);
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            String abcdInfotext = a.getText() + "";
            if (abcdInfotext.equals("A.") || abcdInfotext.equals("B.") || abcdInfotext.equals("C.") || abcdInfotext.equals("D.")) {
                String abcdtext = accessibilityNodeInfoList.get(n + 1).getText() + "";
                AccessibilityNodeInfo abcdclikInfo = accessibilityNodeInfoList.get(n);

                hashMaptextInfo.put(abcdtext, abcdclikInfo);
                hashMapabcdInfo.put(abcdInfotext, abcdclikInfo);
                listabcdtext.add(abcdtext);
            }
            n += 1;
        }

//        输出
        for (HashMap.Entry e : hashMaptextInfo.entrySet()
        ) {
            LogUtil.D("hashMaptextInfo: text: " + e.getKey() + " click: " + hashMaptextInfo.get(e.getKey()).isClickable());
        }
        int q = 0;
        for (HashMap.Entry e : hashMapabcdInfo.entrySet()
        ) {
            LogUtil.D("hashMapabcdInfo: text: " + e.getKey() + " click: " + hashMapabcdInfo.get(e.getKey()).isClickable());
            q += 1;
        }
        for (String s : listabcdtext
        ) {
            LogUtil.D("listabcdtext: " + s);
        }
        return true;
    }


    //    获取题型z
    public static String getquestiontype() {
        List<String> listtext = User.getallInfottext(false);
        String text = "未找到题型";
        for (int i = 0; i < listtext.size(); i++) {
            String textflag = listtext.get(i);
            if (textflag.equals("单选题") || textflag.equals("多选题") || textflag.equals("填空题") || textflag.equals("单选题 (10分)") || textflag.equals("多选题 (10分)") || textflag.equals("填空题 (10分)")) {
                text = textflag;
                break;
            }
        }
        LogUtil.D("题型为：" + text);
        return text;
    }

    // 获取text后的n个Info,numberofsearches查找个数
    public static HashMap<String, AccessibilityNodeInfo> getallInfotextandafterInfo(HashMap<String, Integer> getInfoBytextaftermap) {
        HashMap<String, AccessibilityNodeInfo> InfoList = new HashMap<>();
        List<AccessibilityNodeInfo> listInfo = AcessibilityApi.getAllNode(null, null);
        int m = 0;
        for (AccessibilityNodeInfo s : listInfo
        ) {
            String Infotext = s.getText() + "";
            for (HashMap.Entry e : getInfoBytextaftermap.entrySet()
            ) {
                String key = e.getKey() + "";
                int vaule = (int) e.getValue();
                if (Infotext.equals(key)) {
                    AccessibilityNodeInfo Info = listInfo.get(m + vaule);
                    InfoList.put(key, Info);
                    LogUtil.D("找到：" + key + ", InfoisClickable: " + Info.isClickable() + ", InfoisEditable: " + Info.isEditable());
                }
            }
            m += 1;
        }
        return InfoList;
    }


/*    public static List<String> getallInfottext() {
        List<AccessibilityNodeInfo> listInfo = AcessibilityApi.getAllNode(null, null);
        List<String> stringListtext = new ArrayList<>();
//        get all text
        for (int i = 0; i < listInfo.size(); i++) {
            String text = listInfo.get(i).getText() + "";
//            LogUtil.D("Info " + i + " text: " + text);
            stringListtext.add(text);
        }
        return stringListtext;
    }*/

    //    获取题目
    public static List<String> gettimu(String questiontype) {
        List<String> timuallList = new ArrayList<>();
        List<String> timuList = new ArrayList<>();
        timuallList = User.getallInfottext(false);
//        获取题目开始位置
        int n = 0;
        for (int i = 0; i < timuallList.size(); i++) {
            String temp = timuallList.get(i);
            if (temp.equals(questiontype)) {
                n = i;
                break;
            }
        }
        n += 3;
        LogUtil.D("获取题目起始位置： " + n);

        for (int i = n; i < timuallList.size(); i++) {
            String tempstr = timuallList.get(i);
            if (tempstr.equals("")) {
                continue;
            }
            if (tempstr.equals("") || tempstr.equals("查看提示") || tempstr.indexOf("推荐") != -1 || tempstr.equals("A.") || tempstr.indexOf("出题") != -1) {
                return timuList;
            }
            timuList.add(tempstr);
        }
        return timuList;
    }

    //    获取题目横线前后字符串
    public static HashMap<Integer, String[]> getwentideginAndend(List<String> timu) {
        String wt_begin = null;
        String wt_end;
//        匹配问题前后字符串个数
        int wtchs = 3;
        int m = 0;
        List<String> wt_begin_end_list = new ArrayList<>();
        HashMap<Integer, String[]> hashMap = new HashMap<>();
        String[] wtstrings = new String[]{};

        for (String s : timu
        ) {
            m += 1;
            LogUtil.D("问题 " + m + " 为： " + s);
            if (s.length() < 3) {
                wt_begin = s;
                wt_end = s;
            } else {
                wt_begin = s.substring(0, wtchs);
                wt_end = s.substring(s.length() - wtchs);
            }

            wt_begin_end_list.add(wt_begin);
            wt_begin_end_list.add(wt_end);

        }
        wt_begin_end_list.remove(0);
        wt_begin_end_list.remove(wt_begin_end_list.size() - 1);
        int keymap = 0;
        for (int i = 0; i < wt_begin_end_list.size(); i++) {
//            横线开头
            if (i % 2 == 0) {
                wt_begin = wt_begin_end_list.get(i);
            }
//            横线末尾
            if (i % 2 != 0) {
                wt_end = wt_begin_end_list.get(i);
                wtstrings = new String[]{wt_begin, wt_end,};
                hashMap.put(keymap, wtstrings);
                keymap += 1;
            }
        }

        for (HashMap.Entry<Integer, String[]> entry : hashMap.entrySet()
        ) {
            LogUtil.D("key:" + entry.getKey() + "，vaule:" + entry.getValue()[0] + "," + entry.getValue()[1]);
        }
        return hashMap;
    }

    public static List<AccessibilityNodeInfo> getinputInfo() {
        List<AccessibilityNodeInfo> allInfo = AcessibilityApi.getAllNode(null, null);
        List<AccessibilityNodeInfo> inputInfo = new ArrayList<>();
        for (AccessibilityNodeInfo a : allInfo
        ) {
            if (a.isEditable()) {
                inputInfo.add(a);
            }
        }
        return inputInfo;
    }

    public static int cliknext(String cliknextorqueding) {
        int CReFlag = 0;
        LogUtil.D("点击下一题按钮");
        if (cliknextorqueding == null) {
            List<String> list = User.getallInfottext(false);
            for (String s : list
            ) {
                if (s.equals("确定") || s.equals("下一题") || s.equals("完成")) {
                    cliknextorqueding = s;
                    LogUtil.D("找到：" + cliknextorqueding);
                    break;
                }
            }
            if (cliknextorqueding != null) {
                jixuanswer(cliknextorqueding);
            } else {
                LogUtil.E("点击下一题失败");
                return FAILURE;
            }
            ThreadSleepTime.sleepshort();
            CReFlag = overanswer();
            switch (CReFlag) {
                case OVER:
                    LogUtil.D("答题完成");
                    Autoanswer.back();
                    break;
                case SUCCESS:
                    LogUtil.D("继续答题");
                    break;
                case CLIK:
                    LogUtil.E("答题失败，再点击一次下一题");
//                    递归存在多个返回值
                    cliknext(null);
                    break;
                default:
                    LogUtil.E("出错");
                    return FAILURE;
            }
        }
        LogUtil.D("---------CReFlag:" + CReFlag);
        return CReFlag;
    }

    public static boolean jixuanswer(String cliknextorqueding) {
        if (!User.clik_text_Info(cliknextorqueding)) {
            return false;
        }
        LogUtil.D("以点击下一题");
        return true;
    }

    public static int overanswer() {
        LogUtil.D("检查是否答题完成");
        List<String> list = User.getallInfottext(false);
        for (String s : list
        ) {
            if (s.equals("答案解析")) {
                return CLIK;
            } else if (s.equals("再来一组") || s.equals("本次作答分数") || s.equals("本次答对题目数") || s.equals("再练一组") || s.equals("再练一次")) {
                return OVER;
            }
        }
        return SUCCESS;
    }

    //    判字符串出现的次数
    public static int countStr(String str, String sToFind) {
        int num = 0;
        while (str.contains(sToFind)) {
            str = str.substring(str.indexOf(sToFind) + sToFind.length());
            num++;
        }
        LogUtil.D("sToFind: " + sToFind + " 出现次数：" + num);
        return num;
    }
}
