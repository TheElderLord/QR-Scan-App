package com.example.finalcut.fragments;

import static android.content.ContentValues.TAG;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalcut.MainActivity;
import com.example.finalcut.R;
import com.example.finalcut.recycleview.TicketAdapter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketInfoActivity extends AppCompatActivity {
    TextView number,name,window,operator;
    String eventID;
    ImageView cancel;
    String tnumber,tname;
    AlertDialog.Builder popup;
    AlertDialog dialog;
    Button submit,serviceRate,delete;
    ImageView star1,star2,star3,star4,star5;

    static int rating;
    View view;


    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        serviceRate = findViewById(R.id.poprate);
        window =findViewById(R.id.window);
        operator=findViewById(R.id.operator);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tnumber = extras.getString("number");
            tname = extras.getString("name");
            eventID = extras.getString("eventID");

            //The key argument here must match that used in the other activity
        }
        ConstraintLayout c = new ConstraintLayout(this);
        double a=c.getX();


//        eventID="56d65as4e548aeawe-sdasew46841";
//        number.setText("80");
//        name.setText("Taxes and Loan");
//        if (!twindow.isEmpty()  && !toperator.isEmpty()){
//            window.setText(twindow);
//            operator.setText(toperator);
//        }
        if (TicketAdapter.getMqttmessage()!=null){
            String[] arr = TicketAdapter.getMqttmessage().split(";");
            window.setText(arr[1]);
            operator.setText(arr[2]);
        }


        number.setText(tnumber);
        name.setText(tname);
        serviceRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TicketAdapter.completed) showPopUp();
                else Toast.makeText(getApplicationContext(),"Ждите окончания обслуживания",Toast.LENGTH_LONG).show();
            }
        });
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences preferences = getSharedPreferences("bookedTickets", Context.MODE_PRIVATE);
//                preferences.edit().remove(eventID).commit();
//            }
//        });

    }
    @SuppressLint("MissingInflatedId")
    public void showPopUp(){

        popup = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.popupwindow,null);
        submit = view.findViewById(R.id.rate);
        cancel = view.findViewById(R.id.cancel);
        star1 = view.findViewById(R.id.star1);
        star2 = view.findViewById(R.id.star2);
        star3 = view.findViewById(R.id.star3);
        star4 = view.findViewById(R.id.star4);
        star5 = view.findViewById(R.id.star5);
        TextView rateText = view.findViewById(R.id.rateThx);
        popup.setView(view);
        dialog = popup.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=1;
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_gray);
                star3.setImageResource(R.drawable.star_gray);
                star4.setImageResource(R.drawable.star_gray);
                star5.setImageResource(R.drawable.star_gray);

            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=2;
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_gray);
                star4.setImageResource(R.drawable.star_gray);
                star5.setImageResource(R.drawable.star_gray);

            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=3;
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_yellow);
                star4.setImageResource(R.drawable.star_gray);
                star5.setImageResource(R.drawable.star_gray);

            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=4;
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_yellow);
                star4.setImageResource(R.drawable.star_yellow);
                star5.setImageResource(R.drawable.star_gray);

            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=5;
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_yellow);
                star4.setImageResource(R.drawable.star_yellow);
                star5.setImageResource(R.drawable.star_yellow);

            }
        });
         String url= "http://"+ ScanFragment.IP +":3857";
         String branch = ScanFragment.BRANCH_ID;
        System.out.println(url);
        System.out.println(branch);
         submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateText.setVisibility(View.VISIBLE);
                new RatingTask(url,branch,eventID).execute();

                Toast.makeText(view.getContext(),"Оценка "+rating,Toast.LENGTH_SHORT).show();
//                dialog.dismiss();

            }
        });
    }
    public class RatingTask extends AsyncTask<String,String,String> {
        StringBuilder str=new StringBuilder();
        private String eventID;
        private String url;
        private String BRANCH_ID;

        public RatingTask(String url, String BRANCH_ID, String eventID){
            this.url=url;
            this.BRANCH_ID = BRANCH_ID;
            this.eventID=eventID;

        }
        @Override
        protected String doInBackground(String... strings) {
            marking();

            return null;
        }
        private void marking(){
            try {
                Log.d(TAG,eventID);
                Log.d(TAG, String.valueOf(rating));
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//                HttpURLConnection con = ScanFragment.WorkNameTask.con;
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
                String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://nomad.org/CustomUI\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <cus:NomadTerminalTicketRating_Input>\n" +
                        "         <cus:EventRatingId>"+eventID+"</cus:EventRatingId>\n" +
                        "         <cus:Rating>"+ rating+"</cus:Rating>\n" +
                        "      </cus:NomadTerminalTicketRating_Input>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
                System.out.println(xml);
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(xml);
                wr.flush();
                wr.close();
                System.out.println(con.getResponseMessage());
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    str.append(inputLine);
                }
                in.close();
                con.disconnect();
                Log.d(TAG, String.valueOf(str));


            } catch (Exception e) {
                System.out.println(e);
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,eventID);
            Log.d(TAG, String.valueOf(rating));
            System.out.println(rating);
            Toast.makeText(view.getContext(),"Ticket "+tnumber+" was rated by "+rating,Toast.LENGTH_SHORT).show();
            System.out.println("Талон:"+tnumber+"\nОценка:"+rating);


        }


    }
}