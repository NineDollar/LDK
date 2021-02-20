package com.sys.ldk;

import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;


/**
 * <p></p>
 *
 * @author: Nine_Dollar
 * @date: 2021/2/1
 */
public  class TaskTheat {
    private MyThread myThread;

    public TaskTheat() {
        myThread = new MyThread();
    }

    void start() {
        LogUtil.D("请求启动");
        myThread.mystart();
    }

    void zhanting() {
        LogUtil.D("请求暂停");
        myThread.pauseThread();
    }

    void huifu() {
        LogUtil.D("请求恢复");
        myThread.resumeThread();
    }

    void tingzhi() {
        LogUtil.D("请求停止");
        myThread.mystop();
    }

    void test() {
        LogUtil.D(myThread.getState() + "");
    }

    public class MyThread extends Thread {
        public final Object lock = new Object();
        public boolean pause = false;
        public volatile boolean isrun = true;//真--允许运行

        public void mystart() {
            if (myThread == null) {
                LogUtil.E("线程未就绪");
                return;
            }
            if (myThread.getState().equals(Thread.State.TERMINATED)) {  //线程完成标志：TERMINATED
                myThread = new MyThread();
                myThread.isrun = true;
            }
            if (myThread.getState().equals(Thread.State.NEW)) {
                myThread.start();
            }
        }

        /**
         * 调用这个方法实现暂停线程
         */
        public void pauseThread() {
            myThread.interrupt();
            pause = true;
        }

        /**
         * 调用这个方法实现恢复线程的运行
         */
        public void resumeThread() {
            pause = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /**
         * 调用这个方法停止线程
         */
        public void mystop() {
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
        public void onPause() {
            synchronized (lock) {
                try {
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
            try {
                int index = 0;
                while (true) {
                    LogUtil.D(index + "   " + getId());

                    if (User.Threadsleep(myThread,1000)) {
                        break;
                    }

                    ++index;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            LogUtil.D("线程停止");
        }

        public boolean mythreadsleep() {
            if (ApiUtil.sleepTime(5)) {
                // 让线程处于暂停等待状态
                LogUtil.D("线程中断");
                if (!isrun) {
                    isrun = true;
                    LogUtil.D("线程退出");
                    return true;
                }
                while (pause) {
                    LogUtil.D("线程暂停");
                    onPause();
                }
            }
            return false;
        }
    }
}
