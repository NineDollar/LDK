package com.sys.ldk.serverset;

import android.app.Activity;
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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sys.ldk.MyApplication;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.util.LogUtil;

/**
 * @description 客户端
 * @author: Nine_Dollar
 * @date: 2020/11/3
 */
public class Binding extends Activity {
    private Context mcontext;
    private int mywhat;
    private String key;
    private String value;
    public Binding(int mywhat,String key, String value) {
        this.mywhat = mywhat;
        this.key = key;
        this.value = value;
        this.mcontext = MyApplication.getContext();
    }

    /**
     * Messenger for communicating with service.
     */
    Messenger mService = null;
    /**
     * 绑定服务标志
     */
    boolean mIsBound;
    /**
     * Some text view we are using to show state information.
     */
    String mCallbackText;
    int flag = 0;

    /**
     * Handler of incoming messages from service.
     * 接收处理程序
     */
    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("Binding", "客户端接收到消息");
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    Log.e("Bing: ", "回调");
                    mCallbackText = msg.arg1 + "";
                    break;
                case MyNotificationType.case1:
                    break;
                case MyNotificationType.case2:
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

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e("Binding", "连接成功");
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);
//            mCallbackText.setText("Attached.");
            try {
                Message msg = Message.obtain(null, mywhat);
                Bundle bundle = new Bundle();
                bundle.putString(key,value);
                msg.setData(bundle);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                LogUtil.E(e.toString());
            }
            // We want to monitor the service for as long as we are
            // connected to it.
            // As part of the sample, tell the user what happened.
            Toast.makeText(mcontext, R.string.remote_service_connected, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mCallbackText = "Disconnected.";

            // As part of the sample, tell the user what happened.
            Toast.makeText(mcontext, R.string.remote_service_disconnected, Toast.LENGTH_SHORT).show();
        }
    };

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
        Intent intent = new Intent(mcontext, MessengerService.class);
        mcontext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        mCallbackText = "Binding.";
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
            mCallbackText = "Unbinding.";
        }
    }
}
