package com.example.finalcut.fragments;

import static android.content.ContentValues.TAG;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.finalcut.R;
import com.example.finalcut.TicketSaver;
import com.example.finalcut.classes.Ticket;
import com.example.finalcut.parsers.XmlUtils;
import com.example.finalcut.interfaces.RecycleViewInterface;
import com.example.finalcut.recycleview.TicketAdapter;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;


public class TicketFragment extends Fragment implements RecycleViewInterface {
    private RecyclerView recyclerView;
    private  static TicketAdapter myAdapter;
    private static ArrayList<Ticket> ticketList = new ArrayList<>();
    private static TicketSaver ticketSaver;
    ArrayList<Ticket> savedTickets;
    public TicketFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        return view;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ticketSaver = new TicketSaver(getContext(),"bookedTickets");
        savedTickets = ticketSaver.getTickets();
        if (!savedTickets.isEmpty())
            ticketList = savedTickets;
        recyclerView = view.findViewById(R.id.bookedRecycler);
        myAdapter =new TicketAdapter(getContext(), ticketList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
//        ticketList.add(new Ticket("564856415156Esafs","89","Payment and loans",6000));
//        ticketList.add(new Ticket("564856415156Esafs","89","Payment and loans",3000));

    }


    @Override
    public void onItemClick(int position) {
        Ticket ticket = ticketList.get(position);
        String ticketNo=ticket.getTicketNumber();
        String serviceName = ticket.getServiceName();
        Intent i = new Intent(getContext(), TicketInfoActivity.class);

        i.putExtra("number",ticketNo);
        i.putExtra("name",serviceName);
//        i.putExtra("window",arr[1]);
//        i.putExtra("operator",arr[2]);

        i.putExtra("eventID",ticket.getEventID());
        startActivity(i);

    }

    @Override
    public void onRemove(int position) {
        Ticket ticket = ticketList.get(position);
        System.out.println(ticket.isCompleted());
//        if (ticket.isCompleted()){
        ticketList.remove(position);
        myAdapter.notifyItemRemoved(position);
//        }
//        else
//            Toast.makeText(getContext(),"Ждите окончания обслуживания",Toast.LENGTH_LONG).show();


    }

    public static class BookingTicketTask  extends AsyncTask<String,String,String> {
        WeakReference<TicketFragment> weakReference;
        StringBuilder str=new StringBuilder();
        private String QUEUE_ID;
        private String url;
        private String BRANCH_ID;
        Ticket ticket;

        public BookingTicketTask(String url,String BRANCH_ID,String QUEUE_ID){
//            weakReference = new WeakReference<>(fragment);
            this.url=url;
            this.BRANCH_ID = BRANCH_ID;
            this.QUEUE_ID = QUEUE_ID;

        }
        @Override
        protected String doInBackground(String... strings) {
            bookingTicket();
            getBookedTickets();

            return null;
        }
        private void bookingTicket(){
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
                String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://nomad.org/CustomUI\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <cus:NomadTerminalEvent_Now_Input>\n" +
                        "         <cus:QueueId_Now>"+QUEUE_ID+"</cus:QueueId_Now>\n" +
                        "         <cus:IIN>?</cus:IIN>\n" +
                        "         <cus:BranchId>"+BRANCH_ID+"</cus:BranchId>\n" +
                        "         <cus:channel>terminal</cus:channel>\n" +
                        "         <cus:local>ru</cus:local>\n" +
                        "      </cus:NomadTerminalEvent_Now_Input>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
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
                System.out.println(str);
                in.close();
                con.disconnect();


            } catch (Exception e) {
                System.out.println(e);
            }
        }
        public void getBookedTickets(){
            try {
                Map<String,String> a = XmlUtils.parseResponce(String.valueOf(str));
                String eventId = a.get("cus:eventId");
//                String orderNum = a.get("cus:OrderNum");
                String ticketNo = a.get("cus:TicketNo");
                String serviceName = a.get("cus:ServiceName");
                String time = a.get("cus:ProposalTime");
                assert time != null;
                ticket = new Ticket(eventId,ticketNo,serviceName,Long.parseLong(time));
                Log.d(TAG,eventId);
                Log.d(TAG,time);
                ticketList.add(ticket);
//                tickets.add(ticket);
            }catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println();
//            System.out.println(ticket.getEventID()+" created");
            ticketSaver.clearPreferences();
            myAdapter.notifyDataSetChanged();
//            myAdapter.setNewList(tickets);
            ticketSaver.saveTickets(ticketList);
//        if (!bookedList.isEmpty()){
//        bookedList.add(ticket);
//        }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ticketSaver.saveTickets(ticketList);

    }
}