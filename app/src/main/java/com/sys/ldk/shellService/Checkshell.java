package com.sys.ldk.shellService;

import android.app.Activity;
import android.widget.Toast;

import com.sys.ldk.MainActivity;
import com.sys.ldk.SocketClient;

/**
 * @author: Nine_Dollar
 * @date: 2020/11/4
 * @description 检测是否提权
 */
public class Checkshell {
    private static Activity activity;
    public static void show_root(Activity activity) {
        Checkshell.activity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SocketClient("###AreYouOK", new SocketClient.onServiceSend() {
                    @Override
                    public void getSend(String result) {
                        showTextOnTextView(result);
                    }
                });
            }
        }).start();
    }

    private static void showTextOnTextView(String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (text.equals("###IamOK#")) {
                    Toast.makeText(MainActivity.getMcontext(), "以提权", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.getMcontext(), "未提权", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
