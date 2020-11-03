package com.sys.ldk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.appcompat.app.AlertDialog;

import com.sys.ldk.R;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.interfaces.OnInvokeView;
import com.sys.ldk.easyfloat.permission.PermissionUtils;
import com.sys.ldk.serverset.Binding;
import com.sys.ldk.serverset.MessengerService;

import java.util.List;

public class  FloatingWindow {
    private Context mcontext;
    public void chekPermission() {
        mcontext = MyApplication.getContext();
        if (!PermissionUtils.checkPermission(mcontext)) {
            AlertDialog alertDialog = new AlertDialog.Builder(mcontext)
                    .setTitle("提示")
                    .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                    .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Floating_window();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            alertDialog.show();
        } else {
            Floating_window();
        }
    }

    //    悬浮窗
    private  void Floating_window() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, 0, 700)
                .setLayout(R.layout.activity_floatingwindow,
                        new OnInvokeView() {
                            @Override
                            public void invoke(View View) {
                                View.findViewById(R.id.btn_floatwindow).setOnClickListener(v1 -> test5());
                            }
                        })
                .show();
    }

    private void test5() {
//        UiApi.clickNodeByIdWithTimeOut(2000, "com.tencent.mm:id/b4r");
//        UiApi.clickNodeByTextWithTimeOut(2000, "教学服务");
//        UiApi.clickNodeByTextWithTimeOut(2000, "顶岗实习");
        /*List<AccessibilityNodeInfo> accessibilityNodeInfoList = User.getEditableInfo();
        LogUtil.I(accessibilityNodeInfoList.size()+"");
        if (accessibilityNodeInfoList.size() == 1) {
            AcessibilityApi.inputTextByNode(accessibilityNodeInfoList.get(0),"上班");
        }*/
        /*if(User.findtext("川信职院微教")){
            LogUtil.I("true");
        }*/
//        DGSX dgsx = new DGSX();
//        dgsx.startdgsx();

//        Autoanswer.auto_answer();
//        Autoanswer.doactivity();
//        Notification.textContent = "成功";
//        User.getallInfottext(true);
//        UiApi.clickNodeByIdWithTimeOut(1000, "cn.xuexi.android:id/home_bottom_tab_button_work");
//        UiApi.clickNodeByDesWithTimeOut(1000, "工作");
        /*List<AccessibilityNodeInfo> List = User.getScrollNodeInfo();
        AcessibilityApi.ScrollNode(List.get(4));*/
        LogUtil.I("点击");
        /*Message message = new Message();
        message.what = 123;
*/
        Binding binding = new Binding();
//        建立连接
        binding.doBindService();
       /* Binding.IncomingHandler bindingincominghandler = binding.new IncomingHandler();
//        bindingincominghandler.sendMessage(message);

        Message msg = Message.obtain(null, 123);
        msg.replyTo = new Messenger(bindingincominghandler);
        // We pass the value
        Bundle b = new Bundle();
        b.putString("123", "data");
        msg.setData(b);
        try {
            msg.replyTo.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }
}
