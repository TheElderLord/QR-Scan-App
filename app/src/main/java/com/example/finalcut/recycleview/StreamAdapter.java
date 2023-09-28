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

public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.TicketHolder> {
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    static ArrayList<Ticket> list;



    public StreamAdapter(Context context, ArrayList<Ticket> list, RecycleViewInterface recycleViewInterface) {
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
        View v = LayoutInflater.from(context).inflate(R.layout.stream_item, parent, false);

//        v.setOnClickListener(view -> Toast.makeText(context,list.get(viewType).getServiceName() , Toast.LENGTH_SHORT).show());


        return new TicketHolder(v, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {

        Ticket ticket = list.get(position);


        holder.streamTicket.setText(ticket.getTicketNumber());
        holder.streamWindow.setText(ticket.getWindow());




    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TicketHolder extends RecyclerView.ViewHolder {
        TextView streamTicket;
        TextView streamWindow;

        public TicketHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            streamTicket = itemView.findViewById(R.id.streamTicket);
            streamWindow = itemView.findViewById(R.id.streamWindow);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recycleViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }


    }










    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TicketHolder holder) {
        super.onViewDetachedFromWindow(holder);

    }

}
