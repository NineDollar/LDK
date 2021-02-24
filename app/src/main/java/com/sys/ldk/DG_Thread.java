package com.sys.ldk;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.dg.XXQG;
import com.sys.ldk.sdcard.SaveLog;

import static com.sys.ldk.FloatingWindow.mcontext;
import static com.sys.ldk.dg.ReturnType.my_stop;
import static java.sql.Types.NULL;

/**
 * <p>大国运行线程</p>
 *
 * @author: Nine_Dollar
 * @date: 2021/2/3
 */
public class DG_Thread {
    public final static String runing = "正在运行";
    public final static String zan_ting = "暂停";
    public final static String no_run = "停止";
    public static String mode_thread = "未就绪";
    public static MyThread myThread = new MyThread();
    private static boolean isAutoKeyBoard = true;
    public static String get_modeThread() {
        return MyThread.get_mode();
    }


    /**
     * 启动任务线程
     */
    public static void start() {
        LogUtil.D("请求启动");
        MyThread.mystart();
    }

    /**
     * 暂停
     */
    public static void zhanting() {
        LogUtil.D("请求暂停");
        MyThread.pauseThread();
    }

    /**
     * 暂停后继续
     */
    public static void huifu() {
        LogUtil.D("请求恢复");
        MyThread.resumeThread();
    }

    /**
     * 停止
     */
    public static void stop() {
        LogUtil.D("请求停止");
        MyThread.mystop();
    }

    public static void set_modeThread(String mode) {
        mode_thread = mode;
    }

    public static class MyThread extends Thread {
        public static final Object lock = new Object();
        public static volatile boolean pause = false;
        public static volatile boolean isrun = true;//真--允许运行

        public static boolean mystart() {
            if (myThread == null) {
                LogUtil.E("线程未就绪");
                return false;
            }
            if (myThread.getState().equals(State.TERMINATED)) {  //线程完成标志：TERMINATED
                myThread = new MyThread();
                isrun = true;
            }
            if (myThread.getState().equals(State.NEW)) {
                myThread.start();
            }
            return true;
        }

        /**
         * 获取线程运行状态
         *
         * @return
         */
        public static String get_mode() {
            return mode_thread;
        }

        /**
         * 调用这个方法实现暂停线程
         */
        public static void pauseThread() {
            myThread.interrupt();
            pause = true;
        }

        /**
         * 调用这个方法实现恢复线程的运行
         */
        public static void resumeThread() {
            pause = false;
            mode_thread = runing;
            xhui_fu();
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /**
         * 调用这个方法停止线程
         */
        public static void mystop() {
            pause = false;
            synchronized (lock) {
                lock.notifyAll();
            }
            myThread.interrupt();
            isrun = false;
        }

        /**
         * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
         */
        public static void onPause() {
            mode_thread = zan_ting;
            synchronized (lock) {
                try {
                    image_zant_ing();
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            LogUtil.D("线程启动");
//            保存日志
            savelog();
            try {
//                int index = 0;
                while (true) {
                    mode_thread = runing;
//                    旋转图标
                    ximage_run();

                   /* LogUtil.I(index + "   " + getId());
                    ++index;*/

                    /*if (ThreadSleepTime.sleep(10 * 1000)) {
                        break;
                    }*/
                    /*if (sleepmy(10000) == my_stop) {
                        break;
                    }*/
                    if(XXQG.openxxqj(mcontext)){
                        AcessibilityApi.performAction(AcessibilityApi.ActionType.HOME);
                        ThreadSleepTime.sleep1();
                        AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                    }
                    break;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            mode_thread = no_run;
//            停止图标
            imagestop();
            LogUtil.W("线程停止");
        }

        private void savelog() {
//            获取时间设置log文件名
            SaveLog.setSave_file_name(SaveLog.get_time_name());
        }

        /**
         * 毫秒，用于线程睡眠，可以正常暂停退出
         *
         * @param time
         * @return
         */
        public static int sleepmy(long time) {
            int sum = (int) time / 100;
            int i = 0;
            while (i++ < sum) {
                LogUtil.V("第" + i * (double) 100 / 1000 + "秒");
                if (mythreadsleep(100) == my_stop) {
                    return my_stop;
                }
            }
            return NULL;
        }

        public static int mythreadsleep(long time) {
            if (ThreadSleepTime.user_sleep(time)) {
                // 让线程处于暂停等待状态
                LogUtil.W("线程中断");
                if (!isrun) {
                    isrun = true;
                    LogUtil.W("线程退出");
                    return my_stop;
                }
                while (pause) {
                    LogUtil.W("线程暂停");
                    onPause();
                }
            }
            return NULL;
        }

        private void ximage_run() {
            FloatingWindow.image_run();
        }

        private static void image_zant_ing() {
            FloatingWindow.image_zan_ting();
//            恢复键盘
           if( AcessibilityApi.AutoKeyBoard()){
               isAutoKeyBoard = true;
           }
        }

        private static void xhui_fu() {
            if(isAutoKeyBoard){
                isAutoKeyBoard = false;
                AcessibilityApi.closeKeyBoard();
            }
            FloatingWindow.image_hui_fu();
        }

        private static void imagestop() {
            FloatingWindow.image_stop();
        }
    }
}
