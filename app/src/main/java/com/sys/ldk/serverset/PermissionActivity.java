package com.sys.ldk.serverset;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sys.ldk.MainAccessService;
import com.sys.ldk.MainActivity;
import com.sys.ldk.MyApplication;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;

import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.interfaces.OnInvokeView;
import com.sys.ldk.easyfloat.permission.PermissionUtils;

import java.util.List;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mcontext;
    private static PowerManager packageManager;
    private TextView xuanfutext;
    private TextView dianliangtext;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchyinchang;
    private TextView fuzhutext;
    private Keyguard keyguard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_server_set);
        mcontext = MainActivity.getMycontext();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mcontext = MyApplication.getContext();
        packageManager = (PowerManager) mcontext.getSystemService(Context.POWER_SERVICE);

        initView();
        initflag();
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApiUtil.isAccessibilityServiceOn(mcontext, MainAccessService.class)) {
            fuzhutext.setText(R.string.ysq);
        } else {
            fuzhutext.setText(R.string.wsq);
        }
        if (PermissionUtils.checkPermission(mcontext)) {
            xuanfutext.setText(R.string.ysq);
//            mcontext.stopService(new Intent(this, MainService.class));
            Intent intent = new Intent(this, MainService.class);
            startService(intent);
        } else {
            xuanfutext.setText(R.string.wsq);
        }
        if (packageManager.isIgnoringBatteryOptimizations(mcontext.getPackageName())) {
            dianliangtext.setText(R.string.ysq);
        } else {
            dianliangtext.setText(R.string.wsq);
        }
    }

    private void initView() {
        TextView textview1 = (TextView) findViewById(R.id.textview1);
        Button fuzhu = (Button) findViewById(R.id.fuzhu);
        Button xuanfu = (Button) findViewById(R.id.xuanfu);
        xuanfutext = (TextView) findViewById(R.id.xuanfutext);
        TextView textview2 = (TextView) findViewById(R.id.textview2);
        Button ziqidong = (Button) findViewById(R.id.ziqidong);
        TextView houtaitext = (TextView) findViewById(R.id.houtaitext);
        Button dianliang = (Button) findViewById(R.id.dianliang);
        dianliangtext = (TextView) findViewById(R.id.dianliangtext);
        Button soding = (Button) findViewById(R.id.soding);
        TextView soudingtxst = (TextView) findViewById(R.id.soudingtxst);
        TextView textview3 = (TextView) findViewById(R.id.textview3);
        TextView yinchang = (TextView) findViewById(R.id.yinchang);
        switchyinchang = (Switch) findViewById(R.id.switchyinchang);
        fuzhutext = (TextView) findViewById(R.id.fuzhutext);

        fuzhu.setOnClickListener(this);
        xuanfu.setOnClickListener(this);
        ziqidong.setOnClickListener(this);
        dianliang.setOnClickListener(this);
        soding.setOnClickListener(this);
        fuzhutext.setOnClickListener(this);

        switchyinchang.setChecked(true);
        switchyinchang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/*
                Intent intent = new Intent(mcontext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mcontext.startActivity(intent);*/

                /*val intent = Intent(context, SettingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                context.startActivity(intent)*/
            }
        });
    }

    private void initflag() {
        if (ApiUtil.isAccessibilityServiceOn(this, MainAccessService.class)) {
            fuzhutext.setText(R.string.ysq);
        }
        if (PermissionUtils.checkPermission(this)) {
            xuanfutext.setText(R.string.ysq);
        }

//                   判断键盘是否锁定
         /*  LogUtil.V("键盘是否锁定："+keyguardManager.isKeyguardLocked());
//           判断是否设置了图案或者PIN密码
           LogUtil.V("判断是否设置了图案或者PIN密码："+keyguardManager.isDeviceSecure());
//           判断是否由图案或者PIN锁定
           LogUtil.V("判断是否由图案或者PIN锁定："+keyguardManager.isKeyguardSecure());
//           判断当前设备是否需要图案或者PIN输入
           LogUtil.V("判断当前设备是否需要图案或者PIN输入："+keyguardManager.isDeviceLocked());*/
    }

    @SuppressLint({"ResourceAsColor", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fuzhu:
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                break;
            case R.id.xuanfu:
                EasyFloat.Builder easyFloat = new EasyFloat.Builder(this);
                easyFloat.requestPermission1();
                break;
            case R.id.ziqidong:
//                jumpStartInterface(mcontext);
                KeepLiveUtils.goKeepLiveSetting(mcontext);
                break;
            case R.id.dianliang:
                addWhite(this);
                break;
            case R.id.soding:
                if (!ApiUtil.isAccessibilityServiceOn(mcontext, MainAccessService.class)) {
                    Toast.makeText(mcontext, "请开启辅助服务", Toast.LENGTH_SHORT).show();
                    return;
                }
                AcessibilityApi.performAction(AcessibilityApi.ActionType.RECENTS);
                break;
        }
    }

    public static void addWhite(Activity activity) {
        //应用是否在 白名单中
        if (!packageManager.isIgnoringBatteryOptimizations(activity.getPackageName())) {
            @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "以加入电池优化白名单", Toast.LENGTH_SHORT).show();
        }
    }

    //    后台自启动

    //        获取手机信号
    private String getMobileType() {
        return Build.MANUFACTURER;
    }

    /**
     * @param context 上下文
     * @return
     * @description
     * @author Nine_Dollar
     * @time 2020/11/3 1:48
     */
    public void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LogUtil.D("当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                LogUtil.D("进入三星启动页面");
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity");
            }/* else if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                LogUtil.D("进入小米启动页面");
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                LogUtil.D("进入华为启动页面");
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                LogUtil.D("进入vivo启动页面");
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                LogUtil.D("进入OPPO启动页面");
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            }*/
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            LogUtil.W("异常，进入设置页面");
            intent = new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
