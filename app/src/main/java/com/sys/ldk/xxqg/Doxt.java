package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.xxqg.IntoAnswer;
import com.sys.ldk.xxqg.ThreadSleepTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sys.ldk.xxqg.ReturnType.FAILURE;
import static com.sys.ldk.xxqg.ReturnType.SUCCESS;

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
        ThreadSleepTime.sleepshort();
        HashMap<String, AccessibilityNodeInfo> hashMaptextInfo = new HashMap<>();
        HashMap<String, AccessibilityNodeInfo> hashMapabcdInfo = new HashMap<>();
        List<String> listabcdtext = new ArrayList<>();
        IntoAnswer.abcdInfoandtext(hashMaptextInfo, hashMapabcdInfo, listabcdtext);

//      拿到提示
//        ThreadSleepTime.sleepshort();
        User.clik_text_Info("查看提示");
        ThreadSleepTime.sleepshort();
        List<String> alltextlistafter = User.getallInfottext(false);
        String tishistr = alltextlistafter.get(alltextlistbefore.size() - 3);
        if (tishistr.isEmpty()) {
            tishistr = alltextlistafter.get(alltextlistbefore.size() - 2);
        }
        LogUtil.D("提示： " + tishistr);
        ThreadSleepTime.sleepshort();
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);

//       开始选择
//        判断是否全选
//        User.Threadsleep500();
        ThreadSleepTime.sleepshort();
        if (quanxuan(timu.get(0), listabcdtext)) {
            LogUtil.D("全选");
            for (HashMap.Entry e : hashMapabcdInfo.entrySet()
            ) {
                ThreadSleepTime.sleepshort();
                AcessibilityApi.performViewClick(hashMapabcdInfo.get(e.getKey()));
            }
//            User.Threadsleep(1);
            ThreadSleepTime.sleepshorts();
            return IntoAnswer.cliknext(null);
        } else {
            LogUtil.D("不能全选");
            List<String> dn = noquanxuan(listabcdtext, tishistr);
            if(dn.size()>0){
                for (String s:dn
                     ) {
                    for (HashMap.Entry e : hashMaptextInfo.entrySet()
                    ) {
                        LogUtil.D("e.getKey():" + e.getKey());
                        if (s.equals(e.getKey())) {
                            ThreadSleepTime.sleepshort();
                            AcessibilityApi.performViewClick(hashMaptextInfo.get(e.getKey()));
                        }
                    }
                }
//                User.Threadsleep(1);
                ThreadSleepTime.sleepshorts();
                return IntoAnswer.cliknext(null);
            }else {
                LogUtil.D("答案为空");
            }
        }
        return FAILURE;
    }

    private static List<String> noquanxuan(List<String> listabcdtext, String tishistr) {
        List<String> dn = new ArrayList<>();
        for (String s : listabcdtext
        ) {
            if (tishistr.indexOf(s) != -1) {
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
        if (henxian == abcd) {
            return true;
        }
        return false;
    }

}
