package com.sys.ldk.app;

import android.content.Context;

import com.sys.ldk.FloatingWindow;
import com.sys.ldk.MainActivity;
import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.serverset.MessengerService;
import com.sys.ldk.serverset.MyNotificationType;
import com.sys.ldk.xxqg.Autoanswer;
import com.sys.ldk.xxqg.AutoRead;
import com.sys.ldk.xxqg.AutoVideo;
import com.sys.ldk.xxqg.XXQG;
import com.sys.ldk.accessibility.api.User;

/**
 * @author: Nine_Dollar
 * @date: 2021/1/21
 * @description 启动app
 */
public class AppThreas {
    private MyThread myThread;
    private boolean threadflag = false;
    private Context myContext;

    void AppThreas() {
        myContext = MainActivity.getMcontext();
    }

    public void run(String apptype) {
        if (!threadflag) {
            myThread = new MyThread();
            myThread.setApptype(apptype);
            myThread.isrun = true;
            threadflag = true;
            myThread.start();
        }
    }

    public void zhanting() {
        if (threadflag) {
            myThread.pauseThread();
        }
    }

    public void huifu() {
        if (threadflag) {
            myThread.resumeThread();
        }
    }

    public void tingzhi() {
            myThread.stopThread();
    }

    private class MyThread extends Thread {
        private final Object lock = new Object();
        private boolean pause = false;
        private volatile boolean isrun = false;
        private String mess;
        private String apptype = null;

        /**
         * 调用这个方法实现暂停线程
         */
        void pauseThread() {
            pause = true;
        }

        /**
         * 调用这个方法实现恢复线程的运行
         */
        void resumeThread() {
            pause = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /**
         * 调用这个方法实现停止线程的运行
         */
        void stopThread() {
            LogUtil.D("------停止");
            myThread.interrupt();
            threadflag = false;
            isrun = false;
        }

        /**
         * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
         */
        void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void setApptype(String apptype) {
            this.apptype = apptype;
        }

        @Override
        public void run() {
            super.run();
            int i = 0;
            try {
                while (isrun||!isInterrupted()) {
                    // 让线程处于暂停等待状态
                    while (pause) {
                        onPause();
                    }
                    try {
                        Thread.sleep(50);
                        switch (apptype) {
                            case function.readandvideo:
                                readandvideo();
                                break;
                            case function.dati:
                                dati();
                                break;
                        }
                    } catch (InterruptedException e) {
                        //捕获到异常之后，执行break跳出循环
                        e.printStackTrace();
                        LogUtil.E("中断---------");
                        break;
                    }

                }
            } catch (NullPointerException e) {
                Thread.currentThread().interrupt();
                LogUtil.E("中断---------");
                e.printStackTrace();
            }
        }

        void readandvideo() {
            LogUtil.D(function.readandvideo);
            if (!XXQG.startLearning_power(myContext, "cn.xuexi.android", "com.alibaba.android.rimet.biz.SplashActivity")) {
                LogUtil.E("打开学习强国失败");
                return;
            }
            if (!XXQG.isLearning_power()) {
                LogUtil.E("不在学习强国页面");
                return;
            }
            AutoRead.auto_read();
            ThreadSleepTime.sleep3();
            AutoVideo.auto_video();
        }

        void dati() {
            LogUtil.D(function.dati);
            Autoanswer.startanswer();
        }
    }
}
