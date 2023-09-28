//package com.example.finalcut.tasks;
//
//import android.net.TrafficStats;
//import android.os.AsyncTask;
//import android.widget.TextView;
//
//import com.example.finalcut.classes.Ticket;
//
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
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//public class WorkNameTask extends AsyncTask<String,String,String> {
//    private String url ;
//    private StringBuilder str=new StringBuilder();
//    private String EN="";
//    private String RU="";
//    private String KZ="";
//    private String[] cut;
//    private static ArrayList<Ticket> tickets = new ArrayList<>();
//    private boolean isEN,isRU,isKZ=false;
//    private String BRANCH_ID;
//
//    public WorkNameTask(TextView txt, String url, String Branch){
//        this.url=url;
//        this.BRANCH_ID=Branch;
//    }
//    @Override
//    protected String doInBackground(String... strings) {
//        sendRequest();
//        parseXML();
//        return null;
//    }
//    public void sendRequest(){
//        try {
//            TrafficStats.setThreadStatsTag((int) Thread.currentThread().getId());
//            URL obj = new URL(url);
//            System.out.println(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
//            String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://nomad.org/CustomUI\">\n" +
//                    "   <soapenv:Header/>\n" +
//                    "   <soapenv:Body>\n" +
//                    "      <cus:NomadTerminalMenuList_Input>\n" +
//                    "         <cus:ParentQueueIdTerminal>?</cus:ParentQueueIdTerminal>\n" +
//                    "         <cus:BranchQueueId>"+BRANCH_ID+"</cus:BranchQueueId>\n" +
//                    "      </cus:NomadTerminalMenuList_Input>\n" +
//                    "   </soapenv:Body>\n" +
//                    "</soapenv:Envelope>";
//            con.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(xml);
//            wr.flush();
//            wr.close();
//            System.out.println(con.getResponseMessage());
//            System.out.println("SOAP RESPONSE "+str);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//
//            while ((inputLine = in.readLine()) != null) {
//                str.append(inputLine);
//            }
//            in.close();
//            con.disconnect();
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    //Parsing XML to get usefull data
//    public  void parseXML(){
//        tickets.clear();
//        isEN=true;
//        try {
//            Document doc = loadXMLFromString(String.valueOf(str));
//            doc.getDocumentElement().normalize();
//            NodeList nodeList = doc.getElementsByTagName("xsd:element");
//            for (int i = 0; i < nodeList.getLength(); ++i) {
//                Node node = nodeList.item(i);
//                String workName = String.valueOf(node.getAttributes().getNamedItem("workName").getNodeValue());
//                String queueId = getStringFromXML(node,"queueId");
//                String parentId = getStringFromXML(node,"parentId");
//                cut = workName.split(";");
//
//                for (String str:cut) {
//                    if (str.contains("EN")) EN=removeTrash(str);
//                    else if (str.contains("KZ")) KZ=removeTrash(str);
//                    else RU = removeTrash(str);
//                }
//                Ticket ticket=null;
//                if (isEN){
//                    ticket= new Ticket(EN,queueId,parentId);
//                    System.out.println(EN);
//                }
//                else if (isKZ){
//                    ticket = new Ticket(KZ,queueId,parentId);
//                }
//                else ticket=new Ticket(RU,queueId,parentId);
//                tickets.add(ticket);
//            }
//        }
//        catch (Exception e) {
//            System.out.println(e);
//        }
//
//        System.out.println("Is EN:"+isEN+"\tisKZ:"+isKZ+"\tisRu:"+isRU);
//        System.out.println(tickets.size());
//
//    }
//
//
//    public Document loadXMLFromString(String xml) throws Exception {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        InputSource is = new InputSource(new StringReader(xml));
//        return builder.parse(is);
//    }
//
//    //This method is only for removing useless strings from workName
//    //deletes first 2 symbols which gives Language
//    public String removeTrash(String s){
//        String beg = s.substring(0,2);
//        return s.replaceAll(beg+"=","");
//    }
//
//
//
//
//    //get XML string and get necessary fields and removes useless symbols like = and "
//    public String getStringFromXML(Node node, String string){
//        String str = String.valueOf(node.getAttributes().getNamedItem(string).getNodeValue()).replaceAll(string+"=","");
//        str = str.replaceAll("\"","");
//        return str;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        System.out.println(str);
//        if (str!=null){
//
//        }
//    }
//}
