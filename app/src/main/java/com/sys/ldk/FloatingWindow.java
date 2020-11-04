package com.sys.ldk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.interfaces.OnInvokeView;
import com.sys.ldk.easyfloat.permission.PermissionUtils;
import com.sys.ldk.serverset.Binding1;
import com.sys.ldk.serverset.MessengerService;

public class FloatingWindow {
    private static Context mcontext;

    public static void chekPermission(Context context) {
        mcontext = context;
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
    private static void Floating_window() {
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

    private static void test5() {
        LogUtil.I("点击");
        /*Message message = new Message();
        message.what = 123;
*/
//        Binding1 binding1 = new Binding1(mcontext);
//        建立连接
//        binding1.doBindService();
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

        Binding1 binding1 = new Binding1();
        binding1.doBindService();
    }

}
