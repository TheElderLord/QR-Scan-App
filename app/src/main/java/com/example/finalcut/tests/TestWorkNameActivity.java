//package com.example.finalcut.tests;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.annotation.SuppressLint;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.example.finalcut.R;
//import com.example.finalcut.classes.Ticket;
////import com.example.finalcut.fragments.ScanFragment;
//import com.example.finalcut.fragments.TicketFragment;
//import com.example.finalcut.recycleview.MyAdapter;
//import com.example.finalcut.interfaces.RecycleViewInterface;
////import com.example.finalcut.tasks.WorkNameTask;
//import com.journeyapps.barcodescanner.ScanContract;
//import com.journeyapps.barcodescanner.ScanOptions;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//public class TestWorkNameActivity extends AppCompatActivity implements RecycleViewInterface {
//    private RecyclerView recyclerView;
//    private MyAdapter myAdapter;
//    private  ArrayList<Ticket> recycleList = new ArrayList<>();
//    private String url; //= "http://10.10.111.90:3857";
//    private String BRANCH_ID;// ="2111";
//    private Button scanQR;
//    private Button bookedTickets;
//    private String IP;
//    WindowManager manager;
//    Map<String, String> infoMap = new HashMap<String, String>();
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_work_name);
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
//                .detectLeakedClosableObjects()
//                .build());
//        StrictMode.enableDefaults();
//        recyclerView = findViewById(R.id.recyclerView);
//        scanQR = findViewById(R.id.scanQR);
//        bookedTickets = findViewById(R.id.booked);
//        myAdapter =new MyAdapter(this, recycleList, TestWorkNameActivity.this);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(myAdapter);
////        IP = "10.10.111.90";
////        BRANCH_ID = "2111";
//
////
////
////        WorkNameTask workNameTask = new WorkNameTask(TestWorkNameActivity.this,url,BRANCH_ID);
////        workNameTask.execute();
//
//
//
////        url = "http://10.10.111.90:3857";
////        BRANCH_ID="2111";
//
//
//
//        bookedTickets.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent a = new Intent(TestWorkNameActivity.this,BookedTicketList.class);
//                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//            }
//        });
//        scanQR.setOnClickListener(new View.OnClickListener() {
//            boolean received = true;
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onClick(View view) {
////                Intent a = new Intent(context,ScanQRActivity.class);
////                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(a);
////                Bundle extras = getIntent().getExtras();
////                if (extras != null) {
////                url ="http://"+ extras.getString("IP")+":3857";
////                BRANCH_ID = extras.getString("BRANCH");
////
////                 //The key argument here must match that used in the other activity
////                }
//                scanQR();
//                System.out.println(url +" Branch ID is "+BRANCH_ID);
//                System.out.println(url);
////                if (IP==null && BRANCH_ID==null ){
////                    received=false;
////                }
////                while (!received){
////                    try {
////                        wait();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
//////                notifyAll();
//
//
////                Runnable runnable1 = new Runnable() {
////                    @Override
////                    public void run() {
////                        try {
////                            runOnUiThread(new Runnable() {
////
////
////                                @Override
////                                public void run() {
////
////
////                                }
////                            });
////                            Thread.sleep(300);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                };
////                new Thread(runnable1).start();
//
//
//
//
////                else{
////                    ConstraintLayout constraintLayout = findViewById(R.id.testAct);
////                    TextView textView = new TextView(getApplicationContext());
////                    textView.setText("Scan QR please");
////                    textView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
////                    ((ConstraintLayout) constraintLayout).addView(textView);
////                }
//            }
//        });
//    }
//    private void scanQR(){
//
//        ScanOptions scanOptions = new ScanOptions();
//        scanOptions.createScanIntent(getApplicationContext());
//        scanOptions.setPrompt("A volume up to flash on");
//        scanOptions.setBeepEnabled(false);
//        scanOptions.setOrientationLocked(true);
//        scanOptions.setCaptureActivity(CaptureAct.class);
//        barlauncher.launch(scanOptions);
//
//    }
//    ActivityResultLauncher<ScanOptions> barlauncher= registerForActivityResult(new ScanContract(), result -> {
//
//        if (result!=null) {
//
//            String s = result.getContents();
//            try {
//                s= s.substring(1,s.length()-1);
//                s= s.replaceAll("'","").trim();
//                String[] pairs = s.split(",");
//                for (int i=0;i<pairs.length;i++) {
//                    String pair = pairs[i].trim();
//                    String[] keyValue = pair.split(":");
//                    infoMap.put(keyValue[0].trim(), keyValue[1].trim());
//                }
//                IP = infoMap.get("IP");
//                BRANCH_ID = infoMap.get("BRANCH_ID");
//
//                IP = IP.substring(1,IP.length()-1);
//                BRANCH_ID = BRANCH_ID.substring(1,BRANCH_ID.length()-1);
//                System.out.println(IP);
//                System.out.println(BRANCH_ID);
//                url = "http://"+IP+":3857";
//                WorkNameTask workNameTask = new WorkNameTask(url,BRANCH_ID);
//                workNameTask.execute();
//                System.out.println(url);
//
//            }catch (Exception e){
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Alert");
//                builder.setMessage("Invalid QR");
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.show();
//            }
//        }
//
//    });
//
//    @Override
//    public void onItemClick(int position) {
//        String a = recycleList.get(position).getQueueID();
//        TicketFragment.BookingTicketTask bok = new TicketFragment.BookingTicketTask(url,BRANCH_ID,a);
//        bok.execute();
//        Toast.makeText(this,"Ticket was booked",Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onRemove(int position) {
//
//    }
//
//    //    class ScanQRTask extends AsyncTask<Void,Void,Void>{
////
////
//////        private void scanQR(){
//////
//////            ScanOptions scanOptions = new ScanOptions();
//////            scanOptions.setPrompt("A volume up to flash on");
//////            scanOptions.setBeepEnabled(false);
//////            scanOptions.setOrientationLocked(true);
//////            scanOptions.setCaptureActivity(CaptureAct.class);
//////            barlauncher.launch(scanOptions);
//////
//////        }
//////        ActivityResultLauncher<ScanOptions> barlauncher= registerForActivityResult(new ScanContract(), result -> {
//////
//////            if (result!=null) {
//////
//////                String s = result.getContents();
//////                try {
//////                    s= s.substring(1,s.length()-1);
//////                    s= s.replaceAll("'","").trim();
//////                    String[] pairs = s.split(",");
//////                    for (int i=0;i<pairs.length;i++) {
//////                        String pair = pairs[i].trim();
//////                        String[] keyValue = pair.split(":");
//////                        infoMap.put(keyValue[0].trim(), keyValue[1].trim());
//////                    }
//////                    IP = infoMap.get("IP");
//////                    BRANCH_ID = infoMap.get("BRANCH_ID");
//////
//////                    IP = IP.substring(1,IP.length()-1);
//////                    BRANCH_ID = BRANCH_ID.substring(1,BRANCH_ID.length()-1);
//////                    System.out.println(IP);
//////                    System.out.println(BRANCH_ID);
//////                    url = "http://"+IP+":3857";
//////                    System.out.println(url);
////////                AlertDialog.Builder builder = new AlertDialog.Builder(context);
////////                builder.setTitle("Alert");
////////                builder.setMessage(url);
////////                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////////                    @Override
////////                    public void onClick(DialogInterface dialogInterface, int i) {
////////                        dialogInterface.dismiss();
////////                    }
////////                });
////////                builder.show();
//////
//////                }catch (Exception e){
//////                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//////                    builder.setTitle("Alert");
//////                    builder.setMessage("Invalid QR");
//////
//////                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//////                        @Override
//////                        public void onClick(DialogInterface dialogInterface, int i) {
//////                            dialogInterface.dismiss();
//////                        }
//////                    });
//////                    builder.show();
//////                }
//////            }
//////
//////        });
////
////        @Override
////        protected Void doInBackground(Void... voids) {
////            scanQR();
////            return null;
////        }
////
////        @Override
////        protected void onPostExecute(Void unused) {
////            super.onPostExecute(unused);
////            WorkNameTask workNameTask = new WorkNameTask(url,BRANCH_ID);
////            workNameTask.execute();
////
////        }
////    }
//static class WorkNameTask extends AsyncTask<String,Ticket,String> {
//        private String url ;
//        private StringBuilder str=new StringBuilder();
//        private String EN="";
//        private String RU="";
//        private String KZ="";
//        private String[] cut;
//        private ArrayList<Ticket> tickets = new ArrayList<>();
//        private boolean isEN,isRU,isKZ=false;
//        private String BRANCH_ID;
//
//        public WorkNameTask(String url,String Branch){
//            this.url=url;
//            this.BRANCH_ID=Branch;
//        }
//
//    @Override
//    protected void onProgressUpdate(Ticket... values) {
//        super.onProgressUpdate(values);
//
//    }
//
//    @Override
//        protected String doInBackground(String... strings) {
//            sendRequest();
//            parseXML();
//
//
//            return null;
//        }
//        public void sendRequest(){
//            try {
////                TrafficStats.setThreadStatsTag((int) Thread.currentThread().getId());
//                URL obj = new URL(url);
//                System.out.println(url);
//                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//                con.setRequestMethod("POST");
//                con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
//                String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://nomad.org/CustomUI\">\n" +
//                        "   <soapenv:Header/>\n" +
//                        "   <soapenv:Body>\n" +
//                        "      <cus:NomadTerminalMenuList_Input>\n" +
//                        "         <cus:ParentQueueIdTerminal>?</cus:ParentQueueIdTerminal>\n" +
//                        "         <cus:BranchQueueId>"+BRANCH_ID+"</cus:BranchQueueId>\n" +
//                        "      </cus:NomadTerminalMenuList_Input>\n" +
//                        "   </soapenv:Body>\n" +
//                        "</soapenv:Envelope>";
//                con.setDoOutput(true);
//                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                wr.writeBytes(xml);
//                wr.flush();
//                wr.close();
//                System.out.println(con.getResponseMessage());
//                System.out.println("SOAP RESPONSE "+str);
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String inputLine;
//
//                while ((inputLine = in.readLine()) != null) {
//                    str.append(inputLine);
//                }
//                in.close();
//                con.disconnect();
//
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//
//        //Parsing XML to get usefull data
//        public  void parseXML(){
//            tickets.clear();
//            isEN=true;
//            try {
//                Document doc = loadXMLFromString(String.valueOf(str));
//                doc.getDocumentElement().normalize();
//                NodeList nodeList = doc.getElementsByTagName("xsd:element");
//                for (int i = 0; i < nodeList.getLength(); ++i) {
//                    Node node = nodeList.item(i);
//                    String workName = String.valueOf(node.getAttributes().getNamedItem("workName").getNodeValue());
//                    String queueId = getStringFromXML(node,"queueId");
//                    String parentId = getStringFromXML(node,"parentId");
//                    cut = workName.split(";");
//
//                    for (String str:cut) {
//                        if (str.contains("EN")) EN=removeTrash(str);
//                        else if (str.contains("KZ")) KZ=removeTrash(str);
//                        else RU = removeTrash(str);
//                    }
//                    Ticket ticket=null;
//                    if (isEN){
//                        ticket= new Ticket(EN,queueId,parentId);
//                        System.out.println(EN);
//                    }
//                    else if (isKZ){
//                        ticket = new Ticket(KZ,queueId,parentId);
//                    }
//                    else ticket=new Ticket(RU,queueId,parentId);
//                    tickets.add(ticket);
//                }
//            }
//            catch (Exception e) {
//                System.out.println(e);
//            }
//
//            System.out.println("Is EN:"+isEN+"\tisKZ:"+isKZ+"\tisRu:"+isRU);
//            System.out.println(tickets.size());
//
//        }
//
//
//        public Document loadXMLFromString(String xml) throws Exception {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            InputSource is = new InputSource(new StringReader(xml));
//            return builder.parse(is);
//        }
//
//        //This method is only for removing useless strings from workName
//        //deletes first 2 symbols which gives Language
//        public String removeTrash(String s){
//            String beg = s.substring(0,2);
//            return s.replaceAll(beg+"=","");
//        }
//
//
//
//
//        //get XML string and get necessary fields and removes useless symbols like = and "
//        public String getStringFromXML(Node node, String string){
//            String str = String.valueOf(node.getAttributes().getNamedItem(string).getNodeValue()).replaceAll(string+"=","");
//            str = str.replaceAll("\"","");
//            return str;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
////            myAdapter.setNewList(tickets);
//        }
//    }
//}