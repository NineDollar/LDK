package com.sys.ldk.serverset;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sys.ldk.MainActivity;
import com.sys.ldk.MyApplication;
import com.sys.ldk.MyBroadcastReceiver;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.shellService.Main;

import java.util.ArrayList;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static com.sys.ldk.FloatingWindow.mcontext;

/**
 * @description 服务端
 * @author: Nine_Dollar
 * @date: 2020/11/3
 */
public class MessengerService extends Service {
    private String string;
    private NotificationCompat.Builder builder;
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
    public static String text = "未接收到消息";
    public static String title = "";

    /**
     * Handler of incoming messages from clients.
     */
    @SuppressLint("HandlerLeak")
    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.I("收到消息");
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
                    title = msg.getData().getString(MyNotificationType.keytitle1);
                    text = msg.getData().getString(MyNotificationType.keytext1);
                    LogUtil.V("title: " + title);
                    LogUtil.V("text: " + text);
//                    showNotification(title, text);
                    createNotificationChannel(title, text);
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
    /*private void showNotification(String title, String text) {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.enableLights(false);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        sendMessage(title, text);
    }

    private void sendMessage(String title, String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getContext(), 0,
                new Intent(this, MainActivity.class), 0);
        // Set the info for the views that show in the notification panel.
        String ticker = getResources().getString(R.string.ticker);
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.user_ic_launcher_round)  // the status icon
                .setTicker(ticker)  // the status text
                .setContentTitle(title)  // the label of the entry
//                .setContentText(text)  // the contents of the entry
                .setStyle(new Notification.BigTextStyle().bigText(text))
//                .setBadgeIconType(R.drawable.add)
//                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
//                .setOnlyAlertOnce(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.tubiao))
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setChannelId(CHANNEL_ID)
                .build();
        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
//        mNM.notify(R.string.remote_service_started, notification);

        startForeground(notificationId, notification);
    }*/
    private void createNotificationChannel(String title, String text) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(MyNotificationType.getChannel(), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        showNotification(title, text);
    }

    private void showNotification(String title, String text) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
        snoozeIntent.setAction("MessengerService");
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyNotificationType.getChannel())
                .setSmallIcon(R.mipmap.user_ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.tubiao))
                .setContentTitle(title)
                .setContentText(text)
                .setShowWhen(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(false)
                .addAction(R.mipmap.user_ic_launcher_round, getString(R.string.dakaixuanfuchaung),
                        snoozePendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mcontext);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(MyNotificationType.getNotificationId(), builder.build());
    }
}
