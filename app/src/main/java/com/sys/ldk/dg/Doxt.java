package com.sys.ldk.dg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sys.ldk.dg.ReturnType.FAILURE;
import static com.sys.ldk.dg.ReturnType.my_stop;

public class Doxt {
    /**
     * 多选题
     * @param questiontype
     * @return
     */
    public static int doxt(String questiontype) {
        List<String> alltextlistbefore = User.getallInfottext(false);
        List<String> timu = IntoAnswer.gettimu(questiontype);
        for (String s : timu
        ) {
            LogUtil.D("题目：" + s + ", length：" + s.length());
        }

//       拿到答案选项
        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }
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
        if (tishistr.isEmpty()) {
            tishistr = alltextlistafter.get(alltextlistbefore.size() - 2);
        }
        LogUtil.D("提示： " + tishistr);
        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);

//       开始选择
//        判断是否全选
        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }

        if (quanxuan(timu.get(0), listabcdtext)) {
            LogUtil.D("全选");
            for (HashMap.Entry e : hashMapabcdInfo.entrySet()
            ) {
                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
                AcessibilityApi.performViewClick(hashMapabcdInfo.get(e.getKey()));
                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
            }
            return IntoAnswer.cliknext(null);
        } else {
            LogUtil.D("不能全选");
            List<String> dn = noquanxuan(listabcdtext, tishistr);
            if (dn.size() > 0) {
                for (String s : dn
                ) {
                    for (HashMap.Entry e : hashMaptextInfo.entrySet()
                    ) {
                        LogUtil.D("e.getKey():" + e.getKey());
                        if (s.equals(e.getKey())) {
                            if (ThreadSleepTime.sleep0D5()) {
                                return my_stop;
                            }
                            AcessibilityApi.performViewClick(hashMaptextInfo.get(e.getKey()));
                        }
                    }
                }
                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
                return IntoAnswer.cliknext(null);
            } else {
                LogUtil.D("答案为空");
            }
        }
        return FAILURE;
    }

    private static List<String> noquanxuan(List<String> listabcdtext, String tishistr) {
        List<String> dn = new ArrayList<>();
        for (String s : listabcdtext
        ) {
            if (tishistr.contains(s)) {
                LogUtil.D("不能全选答案：" + s);
                dn.add(s);
            }
        }
        return dn;
    }

    private static boolean quanxuan(String timu, List<String> listabcdtext) {
//      获取横线个数
        int henxian = IntoAnswer.countStr(timu, " ");
        LogUtil.D("横线出现次数： " + henxian);
//      获取答案个数
        int abcd = listabcdtext.size();
        LogUtil.D("答案个数： " + abcd);
//        判断是否全选
        return henxian == abcd;
    }

}
