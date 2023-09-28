package com.example.finalcut.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalcut.R;
import com.example.finalcut.classes.Ticket;
import com.example.finalcut.interfaces.RecycleViewInterface;
import com.example.finalcut.recycleview.StreamAdapter;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;


public class StreamFragment extends Fragment implements RecycleViewInterface {
    private RecyclerView recyclerView;
    private StreamAdapter streamAdapter;
    private ArrayList<Ticket> streamTicket;
    MqttAndroidClient client;
    String topic, clientId, number;
    public static String mqttmessage;
    //        client = new MqttAndroidClient(context, "tcp://broker.hivemq.com:1883", clientId);





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerstream);
        streamTicket = new ArrayList<>();
        streamAdapter = new StreamAdapter(getContext(),streamTicket,this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(streamAdapter);



        client = new MqttAndroidClient(getContext(), "tcp://"+ ScanFragment.IP +":1883",clientId);
        clientId = "Mobile";
//        client = new MqttAndroidClient(getContext(), "tcp://broker.hivemq.com:1883", clientId);


        connect();

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onRemove(int position) {

    }
    public void connect() {

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
                    sub();

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
    //Subscribe to MQTT broker
    public void sub() {
        try {
            client.subscribe(topic, 2);
            client.subscribe("nomad/#",2);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("CLIENT CONNECTION LOST:", "Not connected");

                }

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    mqttmessage = new String(message.getPayload());
                    String[] arr = mqttmessage.split(";");
                    streamTicket.add(0,new Ticket(arr[0],arr[1]));
                    streamAdapter.notifyDataSetChanged();
                    Log.d("MQTT BROKER MESSAGE:", mqttmessage);


                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("DELIVERY COMPLETE:", token.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(String topic) {
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("MQTT CLIENT:", "UNSUBSCRIBED");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}