package com.sys.ldk.serverset;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sys.ldk.MyApplication;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.util.LogUtil;

import java.time.LocalDate;

/**
 * @author: Nine_Dollar
 * @date: 2020/11/3
 */
public class Binding extends Activity {
    private Context mcontext;
    private MyConn mconn;
    public Binding() {
        this.mcontext = MyApplication.getContext();
    }

    /**
     * Messenger for communicating with service.
     */
    Messenger mService = null;
    /**
     * Flag indicating whether we have called bind on the service.
     */
    boolean mIsBound;
    /**
     * Some text view we are using to show state information.
     */
    TextView mCallbackText;
    int flag = 0;

    /**
     * Handler of incoming messages from service.
     * 接收处理程序
     */
    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    LogUtil.D("客户端接收到消息1: " + msg.getData().getString("123"));
                    mCallbackText.setText("Received from service: " + msg.arg1);
                    break;
                case 123:
//                    mCallbackText.setText("Received from service: ");
                    LogUtil.D("客户端接收到消息2: " + msg.getData().getString("123"));
                    flag = 123;
                    break;
                case 1234:
//                    mCallbackText.setText("Received from service: ");
                    LogUtil.D("客户端接收到消息3: " + msg.getData().getString("123"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Class for interacting with the main interface of the service.
     */
    public ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);
            mCallbackText.setText("Attached.");

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
//                获取IBinder实例,以便我们可以将消息发送到我们的服务
//                Message msg = Message.obtain(null, MessengerService.MSG_REGISTER_CLIENT);
                Message msg = Message.obtain(null, 1234);
                msg.replyTo = mMessenger;
                mService.send(msg);

                // Give it some value as an example.
                msg = Message.obtain(null, MessengerService.MSG_SET_VALUE, this.hashCode(), 0);
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }

            // As part of the sample, tell the user what happened.
            Toast.makeText(mcontext, R.string.remote_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mCallbackText.setText("Disconnected.");

            // As part of the sample, tell the user what happened.
            Toast.makeText(MyApplication.getContext(), R.string.remote_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * @description 测试
     * @param 
     * @return 
     * @author Nine_Dollar
     * @time 2020/11/3 20:36
     */
    class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mCallbackText.setText("Attached.");
            try {
                Message msg = Message.obtain(null, 1234);
                msg.replyTo = mMessenger;
                mService.send(msg);
                msg = Message.obtain(null, MessengerService.MSG_SET_VALUE, this.hashCode(), 0);
                mService.send(msg);
            } catch (RemoteException e) {
                LogUtil.E(e.toString());
            }
            Toast.makeText(mcontext, R.string.remote_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /**
     * @param
     * @return
     * @description 绑定到服务，指定服务类和回调接口
     * @author Nine_Dollar
     * @time 2020/11/3 12:55
     */
    public void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because there is no reason to be able to let other
        // applications replace our component.
        Intent intent = new Intent(Binding.this, MessengerService.class);
//        BIND_AUTO_CREATE: 绑定服务并创建
        mconn = new MyConn();
        bindService(intent, mconn, BIND_AUTO_CREATE);
        mIsBound = true;
        mCallbackText.setText("Binding.");
    }

    public void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            MessengerService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }

            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            mCallbackText.setText("Unbinding.");
        }
    }
}
