package com.example.finalcut;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalcut.fragments.ScanFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.Arrays;

public class MQTTTestingActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    MqttAndroidClient client;
    String clientId,topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtttesting);
        init();
    }
    public void init(){
        textView = findViewById(R.id.testtext);
        button = findViewById(R.id.testCon);
        clientId = "Mobile";
        topic = "test/ticket";
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                        clientId);

        button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 System.out.println("Clicked");
              connect();
             }
         });

    }
    public  void connect(){
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            IMqttToken token = client.connect(options);
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
    public void sub(){
        try {
            client.subscribe(topic,2);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("CLIENT CONNECTION LOST:","Not connected");

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String s = new String(message.getPayload());

                    Log.d("MQTT BROKER MESSAGE:",s);
                    textView.setText(s);


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



}