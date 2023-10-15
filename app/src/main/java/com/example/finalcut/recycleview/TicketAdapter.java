package com.example.finalcut.recycleview;

import static com.example.finalcut.R.drawable.remove_active;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalcut.MainActivity;
import com.example.finalcut.R;
import com.example.finalcut.classes.Ticket;
import com.example.finalcut.fragments.ScanFragment;
import com.example.finalcut.fragments.TicketFragment;
import com.example.finalcut.interfaces.RecycleViewInterface;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    static ArrayList<Ticket> list;
    MqttAndroidClient client;
    String topic,clientId,number;
    public static  String mqttmessage;

    public static boolean completed = false;


    public TicketAdapter(Context context, ArrayList<Ticket> list,RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.list = list;
        this.recycleViewInterface = recycleViewInterface;

    }



    public void setNewList(ArrayList<Ticket> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ticketitem,parent,false);

        v.setOnClickListener(view -> Toast.makeText(context,list.get(viewType).getServiceName() , Toast.LENGTH_SHORT).show());
//        Ticket ticket = list.get(viewType);
//        TextView time = v.findViewById(R.id.time);
//        TextView status = v.findViewById(R.id.wstatus);
//
//        ImageView remove = v.findViewById(R.id.removeItem);
//        final long[] mills = {ticket.getProposalTime()};
//
//        timer =  new CountDownTimer(mills[0],1000) {
//            @Override
//            public void onTick(long l) {
//
//                int minutes = (int) mills[0] /1000/60;
//                int seconds = (int) mills[0] /1000%60;
//                String formatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
//
//                time.setText(formatted);
//                mills[0] -=1000;
//                if (mills[0]<=300000){
//                    if (!ticket.isCompleted()){
//                        addNotification(ticket);
//                        ticket.setCompleted(true);}
//                }
//
//            }
//
//            @SuppressLint("SuspiciousIndentation")
//            @Override
//            public void onFinish() {
//                time.setText("00:00");
//
//
//            }
//        }.start();
//        if (ticket.isCompleted()) {
//            remove.setImageResource(remove_active);
//            status.setText("Завершено:Пожалуйста,поставьте оценку");
//            status.setTextColor(Color.GREEN);
//            time.setText("00:00");
////            notifyItemChanged(position);
////            synchronized(this){
////                notifyItemChanged(position);
////            }
//            timer.cancel();
//        }
//



        return new TicketHolder(v,recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {

        Ticket ticket = list.get(position);
        topic = "nomad/"+ticket.getEventID();
        clientId = "Mobile";
        number = ticket.getTicketNumber();
        client =null;
        try {
            client = new MqttAndroidClient(context, "tcp://"+ ScanFragment.IP +":1883",
                    clientId);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(number);
//        topic = "test/ticket";
//        client = new MqttAndroidClient(context, "tcp://broker.hivemq.com:1883", clientId);






        try{
            if (!ticket.isCompleted())
                connect(position);
        }catch (Exception e){
            System.out.println("Problem with MQTT broker or client");
        }



        holder.serviceName.setText(ticket.getServiceName());
        holder.ticketNo.setText(ticket.getTicketNumber());

        final long[] mills = {ticket.getProposalTime()};

        CountDownTimer time = new CountDownTimer(mills[0],1000) {
            @Override
            public void onTick(long l) {

                int minutes = (int) mills[0] /1000/60;
                int seconds = (int) mills[0] /1000%60;
                String formatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

                holder.time.setText(formatted);
                mills[0] -=1000;
                if (mills[0]<=300000){
                    if (!ticket.isCompleted()){
                        try {
                            addNotification(ticket);
                        }catch (Exception e){

                        }
                        ticket.setCompleted(true);}
                }

            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onFinish() {
                holder.time.setText("00:00");


            }
        }.start();
        try {


            if (ticket.isCompleted()) {
                holder.remove.setImageResource(remove_active);
                holder.status.setText("Завершено:Пожалуйста,поставьте оценку");
                holder.status.setTextColor(Color.GREEN);
                holder.time.setText("00:00");
//            notifyItemChanged(position);
//            synchronized(this){
//                notifyItemChanged(position);
//            }
                time.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void addNotification(Ticket ticket) {
        int NOTIFICATION_ID = 345;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.nomad)
                .setContentTitle(ticket.getTicketNumber())
                .setContentText(ticket.getServiceName());

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    private void addNotification(String s,int pos) {
        String[] arr = s.split(";");
        int NOTIFICATION_ID = 123;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.nomad)
                .setContentTitle(s)
                .setContentText("Поставьте оценку качества билета номер "+list.get(pos).getTicketNumber());

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void addNotification(String str,String ticket) {
        int NOTIFICATION_ID = 234;

        String[] messArray = str.split(";");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.nomad)
                .setContentTitle("")
                .setContentText("Билет "+ticket+" подойдите к "+messArray[1]+" окну вас обслужит "+messArray[2]);

        Intent resultIntent = new Intent(context, TicketFragment.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TicketHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView time;
        TextView ticketNo,status;
        ImageView remove;

        public TicketHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            time = itemView.findViewById(R.id.time);
            ticketNo = itemView.findViewById(R.id.ticketNo);
            remove = itemView.findViewById(R.id.removeItem);
            status = itemView.findViewById(R.id.wstatus);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        recycleViewInterface.onRemove(position);
                    }
                }
            });
//            if (!list.isEmpty()){
//            if (list.get(getAdapterPosition()).isCompleted()){
//                remove.setImageResource(remove_active);
//            }}



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewInterface!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            recycleViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }


    }
    public void connect(int pos){

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);


//            options.setKeepAliveInterval(100000000);
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("MQTT CLIENT CONNECTED:", "onSuccess");
                    sub(pos);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("Client not connected", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



    //Disconnect from MQTT broker
    public void disconnect(){

        try {
            IMqttToken unsubToken = client.disconnect();
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                    Log.d("Disconnected:","Disconnected from broker");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }



    }
    public void unsubscribe(String topic){
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("MQTT CLIENT:","UNSUBSCRIBED");
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



    //Subscribe to MQTT broker
    public void sub(int pos){
        final boolean[] connected = {true};
        try {
            client.subscribe(topic,2);
//            client.subscribe("#",2);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("CLIENT CONNECTION LOST:","Not connected");

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    mqttmessage = new String(message.getPayload());

                    Log.d("MQTT BROKER MESSAGE:", mqttmessage);
                    if (mqttmessage.equalsIgnoreCase("completed")) {
                        addNotification("Оцените качества обслуживания",pos);
                        unsubscribe(topic);
                        disconnect();
//                        client.close();
                        connected[0] = false;
                        list.get(pos).setCompleted(true);
                        completed=true;
                    }
                    else if (mqttmessage.equalsIgnoreCase("missed")){
                        unsubscribe(topic);
                        disconnect();
                        client.close();
                        connected[0] = false;
                        list.get(pos).setCompleted(true);
                        completed=true;
                    }


                    try {
                        addNotification(mqttmessage,number);
                    }catch (Exception e){

                    }

//                    if (list.isEmpty()) {
//                        unsubscribe(topic);
//                        disconnect();
////                    client.close();
//                    }

//                    final long[] seconds = {1800000};
//                    new CountDownTimer(seconds[0],1000) {
//                        @Override
//                        public void onTick(long l) {
//                            seconds[0] -=1000;
//
//                            }
//                            @Override
//                            public void onFinish() {
//                            if (connected[0])
//                                disconnect();
//                                list.get(pos).setCompleted(true);
//                            }
//                        }.start();

                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("DELIVERY COMPLETE:",token.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static String getMqttmessage() {
        return mqttmessage!=null && !mqttmessage.equalsIgnoreCase("missed") && !mqttmessage.equalsIgnoreCase("completed")  ? mqttmessage : null;
    }

//    @Override
//    public void onViewDetachedFromWindow(@NonNull TicketHolder holder) {
//        super.onViewDetachedFromWindow(holder);
////        try {
////            client.disconnect();
////        } catch (MqttException e) {
////            e.printStackTrace();
////        }
//    }
}