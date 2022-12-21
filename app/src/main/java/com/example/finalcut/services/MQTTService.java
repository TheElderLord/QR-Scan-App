package com.example.finalcut.services;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.finalcut.MainActivity;
import com.example.finalcut.R;
import com.example.finalcut.classes.Ticket;
import com.example.finalcut.fragments.TicketFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService extends Service {
    MqttAndroidClient client;
    String topic,clientId,number;
//    private Context context;
    private boolean connected;
//    public MQTTService(String topic,String number,String clientId) {
////        this.context = context;
//        this.topic=topic;
//        this.number=number;
//        this.clientId=clientId;
//        client = new MqttAndroidClient(getApplicationContext(), "tcp://10.10.111.100:1883",
//                clientId);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MQTTClientConnect();
        return super.onStartCommand(intent, flags, startId);

    }
    public void MQTTClientConnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (connected){
                    Log.d("Service","Service is running");
//                    connect();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//    public  void connect(){
//        try {
//            MqttConnectOptions options = new MqttConnectOptions();
//            options.setCleanSession(true);
//            IMqttToken token = client.connect(options);
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // We are connected
//                    Log.d("MQTT CLIENT CONNECTED:", "onSuccess");
//                    connected=true;
//                    sub();
//
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.d(TAG, "onFailure");
//
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//    public void sub(){
//        try {
//            client.subscribe(topic,2);
//            client.setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable cause) {
//                    Log.d("CLIENT CONNECTION LOST:","Not connected");
//                    System.out.println(cause.toString());
//                }
//
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                    String s = new String(message.getPayload());
//
//                    Log.d("MQTT BROKER MESSAGE:",s);
//
//                    if (s.toLowerCase().equals("completed")){
//                        final long[] seconds = {10000};
//                        new CountDownTimer(seconds[0],1000) {
//                            @Override
//                            public void onTick(long l) {
//                                seconds[0] -=1000;
//                                addNotification("Rate the ticket");
//                            }
//
//                            @Override
//                            public void onFinish() {
//
//                            }
//                        }.start();
//                        try {
//                            IMqttToken unsubToken = client.disconnect();
//                            unsubToken.setActionCallback(new IMqttActionListener() {
//                                @Override
//                                public void onSuccess(IMqttToken asyncActionToken) {
//                                    connected=false;
//                                    // The subscription could successfully be removed from the client
//                                    Log.d("Disconnected:","Disconnected from broker");
//                                }
//
//                                @Override
//                                public void onFailure(IMqttToken asyncActionToken,
//                                                      Throwable exception) {
//                                    // some error occurred, this is very unlikely as even if the client
//                                    // did not had a subscription to the topic the unsubscribe action
//                                    // will be successfully
//                                }
//                            });
//                        } catch (MqttException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    addNotification(s,number);
//
//
//
//
//                }
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken token) {
//                    Log.d("DELIVERY COMPLETE:",token.toString());
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//    private void addNotification(Ticket ticket) {
//        int NOTIFICATION_ID = 234;
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        String CHANNEL_ID = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            CHANNEL_ID = "my_channel_01";
//            CharSequence name = "my_channel";
//            String Description = "This is my channel";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mChannel.setDescription(Description);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mChannel.setShowBadge(false);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(ticket.getTicketNumber())
//                .setContentText(ticket.getServiceName());
//
//        Intent resultIntent = new Intent(context, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(resultPendingIntent);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
//    private void addNotification(String s) {
//        int NOTIFICATION_ID = 234;
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        String CHANNEL_ID = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            CHANNEL_ID = "my_channel_01";
//            CharSequence name = "my_channel";
//            String Description = "This is my channel";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mChannel.setDescription(Description);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mChannel.setShowBadge(false);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(s)
//                .setContentText("Rate the ticket");
//
//        Intent resultIntent = new Intent(context, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(resultPendingIntent);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
//
//    private void addNotification(String str,String ticket) {
//        int NOTIFICATION_ID = 234;
//
//        String[] messArray = str.split(";");
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        String CHANNEL_ID = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            CHANNEL_ID = "my_channel_01";
//            CharSequence name = "my_channel";
//            String Description = "This is my channel";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mChannel.setDescription(Description);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mChannel.setShowBadge(false);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Queue is come up")
//                .setContentText("Client with ticket "+ticket+" please go to "+messArray[0]+" window operator is "+messArray[1]);
//
//        Intent resultIntent = new Intent(context, TicketFragment.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(resultPendingIntent);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }

}
