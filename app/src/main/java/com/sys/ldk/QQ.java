package com.sys.ldk;

import android.content.Context;

import static com.sys.ldk.accessibility.api.User.startAPP;

public class QQ {

    public static void openQQ(Context context) {
        startAPP(context, "com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity");
    }

}
