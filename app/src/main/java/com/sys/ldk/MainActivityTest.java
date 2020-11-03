package com.sys.ldk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.xxqg.AutoRead;
import com.sys.ldk.xxqg.AutoVideo;
import com.sys.ldk.xxqg.Autoanswer;
import com.sys.ldk.xxqg.XXQG;

import static com.sys.ldk.jrxy.JRXY.openjrxy;
import static com.sys.ldk.serverset.Keyguard.context;
import static com.sys.ldk.xxqg.XXQG.openxxqj;

public class MainActivityTest extends AppCompatActivity implements View.OnClickListener {

    private Button btn_read;
    private Button btn_answer;
    private Button btn_vido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        initView();

        //        打开学习强国
        findViewById(R.id.xxqg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApiUtil.isAccessibilityServiceOn(MainActivityTest.this, MainAccessService.class)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            XXQG.openxxqj(context);
                        }
                    }).start();
                } else {
                    Toast.makeText(MainActivityTest.this, "请开启辅助功能", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        打开今日校园
        findViewById(R.id.jrxy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApiUtil.isAccessibilityServiceOn(MainActivityTest.this, MainAccessService.class)) {
                    Thread jiri = new jiriThread();
                    jiri.start();
                    try {
                        jiri.join();
                        LogUtil.D("今日校园线程等待结束");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivityTest.this, "请开启辅助功能", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_QQ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        QQ qq = new QQ();
                        qq.openQQ(getApplicationContext());
                    }
                }).start();

            }
        });
    }

    private void initView() {
        btn_read = (Button) findViewById(R.id.btn_read);
        btn_answer = (Button) findViewById(R.id.btn_answer);
        btn_vido = (Button) findViewById(R.id.btn_vido);

        btn_read.setOnClickListener(this);
        btn_answer.setOnClickListener(this);
        btn_vido.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        XXQG xxqg = new XXQG();
        switch (v.getId()) {
            case R.id.btn_read:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.D("打开学习强国");
                        UiApi.backToDesk();
                        if (!xxqg.startLearning_power(context, "cn.xuexi.android", "com.alibaba.android.rimet.biz.SplashActivity")) {
                            LogUtil.E("打开学习强国失败");
                            return;
                        }
                        if (!xxqg.isLearning_power()) {
                            LogUtil.E("不在学习强国页面");
                            return;
                        }
                        AutoRead.auto_read();
                    }
                }).start();
                break;
            case R.id.btn_answer:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.D("打开学习强国");
                        UiApi.backToDesk();
                        if (!xxqg.startLearning_power(context, "cn.xuexi.android", "com.alibaba.android.rimet.biz.SplashActivity")) {
                            LogUtil.E("打开学习强国失败");
                            return;
                        }
                        if (!xxqg.isLearning_power()) {
                            LogUtil.E("不在学习强国页面");
                            return;
                        }
                        Autoanswer.doactivity();
                    }
                }).start();
                break;
            case R.id.btn_vido:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!xxqg.startLearning_power(context, "cn.xuexi.android", "com.alibaba.android.rimet.biz.SplashActivity")) {
                            LogUtil.E("打开学习强国失败");
                            return;
                        }
                        if (!xxqg.isLearning_power()) {
                            LogUtil.E("不在学习强国页面");
                            return;
                        }
                        AutoVideo.auto_video();
                    }
                }).start();
                break;
        }
    }

    //    学习强国线程
    public class xuexiThread extends Thread {
        @Override
        public void run() {
            openxxqj(MainActivityTest.this);
        }
    }

    public class jiriThread extends Thread {
        @Override
        public void run() {
            openjrxy(MainActivityTest.this);
        }
    }

}
