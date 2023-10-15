package com.example.finalcut.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalcut.R;
import com.example.finalcut.classes.Ticket;

import com.example.finalcut.recycleview.MyAdapter;
import com.example.finalcut.interfaces.RecycleViewInterface;
import com.example.finalcut.tests.CaptureAct;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ScanFragment extends Fragment implements RecycleViewInterface {
    private RecyclerView recyclerView;
    private  MyAdapter myAdapter;
    private  ArrayList<Ticket> recycleList = new ArrayList<>();
    private  String url; //= "http://10.10.111.90:3857";
    public static   String BRANCH_ID;// ="2111";
    private ImageView scanQR;
    public static String IP;
    Map<String, String> infoMap = new HashMap<String, String>();
    private TextView empty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);

        scanQR = view.findViewById(R.id.scanQR);
        empty = view.findViewById(R.id.empty);
        myAdapter =new MyAdapter(getContext(), recycleList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
//        recycleList.add(new Ticket("Taxes","489784","80456"));
        if (recycleList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }

        scanQR.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getContext(),QRScan.class);
//                startActivity(i);
                recycleList.clear();
                scanQR();
                recyclerView.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                System.out.println(url +" Branch ID is "+BRANCH_ID);
                System.out.println(url);

            }
        });

    }
    private void scanQR(){
//        WorkNameTask task = new WorkNameTask(this,"http://89.218.32.7:3857","2420");

//        WorkNameTask task = new WorkNameTask(this,"http://10.10.111.90:3857","2111");
//        task.execute();
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("A volume up to flash on");
        scanOptions.setBeepEnabled(false);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureAct.class);
        barlauncher.launch(scanOptions);

    }
    ActivityResultLauncher<ScanOptions> barlauncher= registerForActivityResult(new ScanContract(), result -> {

        if (result!=null) {

            String s = result.getContents();
            try {
                s= s.substring(1,s.length()-1);
                s= s.replaceAll("'","").trim();
                String[] pairs = s.split(",");
                for (int i=0;i<pairs.length;i++) {
                    String pair = pairs[i].trim();
                    String[] keyValue = pair.split(":");
                    infoMap.put(keyValue[0].trim(), keyValue[1].trim());
                }
                IP = infoMap.get("IP");
                BRANCH_ID = infoMap.get("BRANCH_ID");

                IP = IP.substring(1,IP.length()-1);
                BRANCH_ID = BRANCH_ID.substring(1,BRANCH_ID.length()-1);
                url = "http://"+IP+":3857";
                System.out.println(url+"\n"+BRANCH_ID);
                WorkNameTask task = new WorkNameTask(this,url,BRANCH_ID);
                task.execute();
            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Alert");
                builder.setMessage("Invalid QR");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        }

    });

    @Override
    public void onItemClick(int position) {
        String a = recycleList.get(position).getQueueID();
        String maxServ = recycleList.get(position).getMaxServime();
        boolean booked = false;

        if (maxServ.isEmpty() || maxServ ==null){
            SubService task = new SubService(this,url,BRANCH_ID,a);
            task.execute();
        }
        else {
            TicketFragment.BookingTicketTask bok = new TicketFragment.BookingTicketTask(url,BRANCH_ID,a);
            bok.execute();
            Toast.makeText(getContext(),"Ticket was booked",Toast.LENGTH_SHORT).show();
            booked=true;

        }
        System.out.println("Max Serv TIme "+maxServ);
        System.out.println("Queue ID "+a);

        System.out.println(url+" "+BRANCH_ID+" "+a);
        if (booked) {
            WorkNameTask task = new WorkNameTask(this,url,BRANCH_ID);
            task.execute();
        }


    }

    @Override
    public void onRemove(int position) {

    }

    static class WorkNameTask extends AsyncTask<String,Ticket,String> {
        private WeakReference<ScanFragment> weakReference;
        private String url ;
        private StringBuilder str=new StringBuilder();
        private String EN="";
        private String RU="";
        private String KZ="";
        private String[] cut;
        private String queueID;
        private boolean isEN,isRU,isKZ=false;
        private String BRANCH_ID;
        static HttpURLConnection con;

        public WorkNameTask(ScanFragment scanFragment,String url,String Branch){
            weakReference = new WeakReference<ScanFragment>(scanFragment);
            this.url=url;
            this.BRANCH_ID=Branch;
        }




        @Override
        protected String doInBackground(String... strings) {
            sendRequest();
            parseXML();



            return null;
        }
        public void sendRequest(){
            try {
                URL obj = new URL(url);
                System.out.println(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
                String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://nomad.org/CustomUI\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <cus:NomadTerminalMenuList_Input>\n" +
                        "         <cus:ParentQueueIdTerminal>?</cus:ParentQueueIdTerminal>\n" +
                        "         <cus:BranchQueueId>"+BRANCH_ID+"</cus:BranchQueueId>\n" +
                        "      </cus:NomadTerminalMenuList_Input>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(xml);
                wr.flush();
                wr.close();
                System.out.println(con.getResponseMessage());
                System.out.println("SOAP RESPONSE "+str);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    str.append(inputLine);
                }
                in.close();
                con.disconnect();



            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //Parsing XML to get usefull data
        @SuppressLint("SuspiciousIndentation")
        public  void parseXML(){
            ScanFragment fragment = weakReference.get();
            fragment.recycleList.clear();
            if (fragment==null || fragment.isDetached()){
                return;
            }
            if (str!=null)
            isRU=true;
            try {
                Document doc = loadXMLFromString(String.valueOf(str));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("xsd:element");
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Node node = nodeList.item(i);
                    String workName = String.valueOf(node.getAttributes().getNamedItem("workName").getNodeValue());
                    String queueId = getStringFromXML(node,"queueId");
                    String parentId = getStringFromXML(node,"parentId");
                    String maxServTime = getStringFromXML(node,"maxServTime");
                    cut = workName.split(";");
                    System.out.println(workName);
                    for (String str:cut) {
                        if (str.contains("EN")) EN=removeTrash(str);
                        else if (str.contains("KZ")) KZ=removeTrash(str);
                        else if (str.contains("RU"))RU = removeTrash(str);
                    }
                    Ticket ticket=null;
                    if (isEN){
                        ticket= new Ticket(EN,queueId,parentId,maxServTime);
                        System.out.println(EN);
                    }
                    else if (isKZ){
                        ticket = new Ticket(KZ,queueId,parentId,maxServTime);
                    }
                    else if (isRU){
                        System.out.println(RU);
                        ticket=new Ticket(RU,queueId,parentId,maxServTime);
                    }


                    fragment.recycleList.add(ticket);
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }

            System.out.println("Is EN:"+isEN+"\tisKZ:"+isKZ+"\tisRu:"+isRU);
            System.out.println( fragment.recycleList.size());

        }


        public Document loadXMLFromString(String xml) throws Exception {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        }

        //This method is only for removing useless strings from workName
        //deletes first 2 symbols which gives Language
        public String removeTrash(String s){
            String beg = s.substring(0,2);
            return s.replaceAll(beg+"=","");
        }




        //get XML string and get necessary fields and removes useless symbols like = and "
        public String getStringFromXML(Node node, String string){
            String str = String.valueOf(node.getAttributes().getNamedItem(string).getNodeValue()).replaceAll(string+"=","");
            str = str.replaceAll("\"","");
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (!tickets.isEmpty()){
//                recycleList.clear();
//            }
            ScanFragment fragment = weakReference.get();


            if (fragment==null || fragment.isDetached()){
                return;
            }

            fragment.myAdapter.notifyDataSetChanged();
//            myAdapter.setNewList(tickets);
//            ticketSaver.saveTickets(tickets);
        }
    }


    static class SubService extends AsyncTask<String,Ticket,String> {
        private WeakReference<ScanFragment> weakReference;
        private String url ;
        private StringBuilder str=new StringBuilder();
        private String EN="";
        private String RU="";
        private String KZ="";
        private String[] cut;
        private String queueID;
        private boolean isEN,isRU,isKZ=false;
        private String BRANCH_ID;
        static HttpURLConnection con;


        public SubService(ScanFragment scanFragment,String url,String Branch,String queueID){
            weakReference = new WeakReference<ScanFragment>(scanFragment);
            this.url=url;
            this.BRANCH_ID=Branch;
            this.queueID=queueID;
        }


        @Override
        protected String doInBackground(String... strings) {
            sendRequest();
            parseXML();



            return null;
        }
        public void sendRequest(){
            try {
                URL obj = new URL(url);
                System.out.println(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
                String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://nomad.org/CustomUI\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <cus:NomadTerminalMenuList_Input>\n" +
                        "         <cus:ParentQueueIdTerminal>"+this.queueID+"</cus:ParentQueueIdTerminal>\n" +
                        "         <cus:BranchQueueId>"+this.BRANCH_ID+"</cus:BranchQueueId>\n" +
                        "      </cus:NomadTerminalMenuList_Input>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(xml);
                wr.flush();
                wr.close();
                System.out.println(con.getResponseMessage());
                System.out.println("SOAP RESPONSE "+str);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    str.append(inputLine);
                }
                in.close();
                con.disconnect();



            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        //Parsing XML to get usefull data
        @SuppressLint("SuspiciousIndentation")
        public  void parseXML(){
            ScanFragment fragment = weakReference.get();
            fragment.recycleList.clear();
            if (fragment==null || fragment.isDetached()){
                return;
            }
            if (str!=null)
                isRU=true;
            try {
                Document doc = loadXMLFromString(String.valueOf(str));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("xsd:element");
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Node node = nodeList.item(i);
                    String workName = String.valueOf(node.getAttributes().getNamedItem("workName").getNodeValue());
                    String queueId = getStringFromXML(node,"queueId");
                    String parentId = getStringFromXML(node,"parentId");
                    String maxServTime = getStringFromXML(node,"maxServTime");
                    cut = workName.split(";");
                    System.out.println(workName);
                    for (String str:cut) {
                        if (str.contains("EN")) EN=removeTrash(str);
                        else if (str.contains("KZ")) KZ=removeTrash(str);
                        else if (str.contains("RU")) RU = removeTrash(str);
                    }
                    Ticket ticket=null;
                    if (isEN){
                        ticket= new Ticket(EN,queueId,parentId,maxServTime);
                        System.out.println(EN);
                    }
                    else if (isKZ){
                        ticket = new Ticket(KZ,queueId,parentId,maxServTime);
                    }
                    else if (isRU)ticket=new Ticket(RU,queueId,parentId,maxServTime);


                    fragment.recycleList.add(ticket);
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }

            System.out.println("Is EN:"+isEN+"\tisKZ:"+isKZ+"\tisRu:"+isRU);
            System.out.println( fragment.recycleList.size());

        }


        public Document loadXMLFromString(String xml) throws Exception {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        }

        //This method is only for removing useless strings from workName
        //deletes first 2 symbols which gives Language
        public String removeTrash(String s){
            String beg = s.substring(0,2);
            return s.replaceAll(beg+"=","");
        }




        //get XML string and get necessary fields and removes useless symbols like = and "
        public String getStringFromXML(Node node, String string){
            String str = String.valueOf(node.getAttributes().getNamedItem(string).getNodeValue()).replaceAll(string+"=","");
            str = str.replaceAll("\"","");
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (!tickets.isEmpty()){
//                recycleList.clear();
//            }

            ScanFragment fragment = weakReference.get();
            if (fragment==null || fragment.isDetached()){
                return;
            }

            fragment.myAdapter.notifyDataSetChanged();
//            myAdapter.setNewList(tickets);
//            ticketSaver.saveTickets(tickets);
        }
    }

    public String getBranchId() {
        return BRANCH_ID;
    }
}