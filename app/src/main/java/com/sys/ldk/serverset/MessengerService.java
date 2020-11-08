package com.sys.ldk.serverset;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sys.ldk.MainActivity;
import com.sys.ldk.MyApplication;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;

/**
 * @description 服务端
 * @author: Nine_Dollar
 * @date: 2020/11/3
 */
public class MessengerService extends Service {
    private String string;
    private static final String CHANNEL_ID = "10";
    public static CharSequence textTitle = "服务检查";
    public static CharSequence textContent = "检查失败";
    public int notificationId = 123;

    /**
     * For showing and hiding our notification.
     */
    NotificationManager mNM;
    /**
     * Keeps track of all current registered clients.
     */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    /**
     * Holds last value set by a client.
     */
    int mValue = 0;

    /**
     * Command to the service to register a client, receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client where callbacks should be sent.
     */
    public static final int MSG_REGISTER_CLIENT = 1;

    /**
     * Command to the service to unregister a client, ot stop receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client as previously given with MSG_REGISTER_CLIENT.
     */
    static final int MSG_UNREGISTER_CLIENT = 2;

    /**
     * Command to service to set a new value.  This can be sent to the
     * service to supply a new value, and will be sent by the service to
     * any registered clients with the new value.
     */
    public static final int MSG_SET_VALUE = 3;
    public static String mytext = "未接收到消息";

    /**
     * Handler of incoming messages from clients.
     */
    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("Messeng", "服务端接收到客户端消息");
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                    mValue = msg.arg1;
                    for (int i = mClients.size() - 1; i >= 0; i--) {
                        try {
                            mClients.get(i).send(Message.obtain(null,
                                    MSG_SET_VALUE, mValue, 0));
                        } catch (RemoteException e) {
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                    break;
                case MyNotificationType.case1:
                    mytext = msg.getData().getString(MyNotificationType.key1);
                    Log.e("mess: ", mytext);
                    showNotification(mytext);
                    break;
                case MyNotificationType.case2 :
                    mytext = msg.getData().getString(MyNotificationType.key2);
                    Log.e("mess: ", mytext);
                    showNotification(mytext);
                    break;
                    case MyNotificationType.case3 :
                    mytext = msg.getData().getString(MyNotificationType.key3);
                    Log.e("mess: ", mytext);
                    showNotification(mytext);
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

    @Override
    public void onCreate() {

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.remote_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String text) {

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        sendMessage(text);
    }

    private void sendMessage(String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getContext(), 0,
                new Intent(this, MainActivity.class), 0);
        CharSequence titext = getText(R.string.remote_service_started);
        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.user_ic_launcher_round)  // the status icon
                .setTicker(titext)  // the status text
                .setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();
        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
//        mNM.notify(R.string.remote_service_started, notification);
        startForeground(notificationId, notification);
    }


}
