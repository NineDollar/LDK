package com.sys.ldk.serverset;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sys.ldk.MainAccessService;
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

public class Permission extends AppCompatActivity implements View.OnClickListener {
    private Context mcontext;
    private TextView textview1;
    private Button fuzhu;
    private Button xuanfu;
    private TextView xuanfutext;
    private TextView textview2;
    private Button ziqidong;
    private TextView houtaitext;
    private Button dianliang;
    private TextView dianliangtext;
    private Button soding;
    private TextView soudingtxst;
    private TextView textview3;
    private TextView yinchang;
    private Switch switchyinchang;
    private TextView fuzhutext;
    private Keyguard keyguard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_server_set);
        mcontext = MyApplication.getContext();
        initView();
        initflag();
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        textview1 = (TextView) findViewById(R.id.textview1);
        fuzhu = (Button) findViewById(R.id.fuzhu);
        xuanfu = (Button) findViewById(R.id.xuanfu);
        xuanfutext = (TextView) findViewById(R.id.xuanfutext);
        textview2 = (TextView) findViewById(R.id.textview2);
        ziqidong = (Button) findViewById(R.id.ziqidong);
        houtaitext = (TextView) findViewById(R.id.houtaitext);
        dianliang = (Button) findViewById(R.id.dianliang);
        dianliangtext = (TextView) findViewById(R.id.dianliangtext);
        soding = (Button) findViewById(R.id.soding);
        soudingtxst = (TextView) findViewById(R.id.soudingtxst);
        textview3 = (TextView) findViewById(R.id.textview3);
        yinchang = (TextView) findViewById(R.id.yinchang);
        switchyinchang = (Switch) findViewById(R.id.switchyinchang);
        fuzhutext = (TextView) findViewById(R.id.fuzhutext);

        fuzhu.setOnClickListener(this);
        xuanfu.setOnClickListener(this);
        ziqidong.setOnClickListener(this);
        dianliang.setOnClickListener(this);
        soding.setOnClickListener(this);
        fuzhutext.setOnClickListener(this);

        switchyinchang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
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
        if (isIgnoringBatteryOptimizations()) {
            dianliangtext.setText(R.string.ysq);
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

    @SuppressLint("ResourceAsColor")
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
                jumpStartInterface(mcontext);
                break;
            case R.id.dianliang:
                if (isIgnoringBatteryOptimizations()) {
                    Toast.makeText(mcontext, "已授权", Toast.LENGTH_SHORT).show();
                } else {
                    requestIgnoreBatteryOptimizations();
                }
                break;
            case R.id.soding:
                AcessibilityApi.performAction(AcessibilityApi.ActionType.RECENTS);
                break;
        }
    }

    //    申请加入白名单
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + mcontext.getPackageName()));
            mcontext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //   判断是否在白名单
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) mcontext.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(mcontext.getPackageName());
        }
        return isIgnoring;
    }

    //    后台自启动

    //        获取手机信号
    private String getMobileType() {
        return Build.MANUFACTURER;
    }

    /**
     * @description
     * @param context 上下文
     * @return
     * @author Nine_Dollar
     * @time 2020/11/3 1:48
     */
    public void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                LogUtil.D("进入三星启动页面");
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，系统更新之后，完全找不到了，心里默默Fuck！
                // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else {
                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                // 针对于其他设备，我们只能调整当前系统app查看详情界面
                // 在此根据用户手机当前版本跳转系统设置界面
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            LogUtil.E("异常，进入设置页面");
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }
}
