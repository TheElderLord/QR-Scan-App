package com.example.finalcut.tasks;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.finalcut.classes.Ticket;
import com.example.finalcut.parsers.XmlUtils;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class BookingTicketTask  extends AsyncTask<String,String,String>   {
    StringBuilder str=new StringBuilder();
    private String QUEUE_ID;
    private String url;
    private String BRANCH_ID;
    private static ArrayList<Ticket> bookedList = new ArrayList<>();
    Ticket ticket;
    public BookingTicketTask(String url,String BRANCH_ID,String QUEUE_ID){
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
                    "         <cus:local>en</cus:local>\n" +
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
            String orderNum = a.get("cus:OrderNum");
            String ticketNo = a.get("cus:TicketNo");
            String serviceName = a.get("cus:ServiceName");
            String time = a.get("cus:ProposalTime");
            assert time != null;
            ticket = new Ticket(eventId,ticketNo,serviceName,Long.parseLong(time));
            bookedList.add(ticket);

        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println();
        System.out.println(ticket.getEventID()+" created");
//        if (!bookedList.isEmpty()){
//        bookedList.add(ticket);
//        networkResponseListener.BookedTickets(bookedList);
//        }

    }

    public static ArrayList<Ticket> getBookedList() {
        return bookedList;
    }

}
