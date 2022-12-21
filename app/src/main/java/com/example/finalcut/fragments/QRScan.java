//package com.example.finalcut.fragments;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.WindowManager;
//
//import com.example.finalcut.MainActivity;
//import com.example.finalcut.R;
//import com.example.finalcut.classes.Ticket;
//import com.example.finalcut.tests.CaptureAct;
//import com.example.finalcut.tests.ScanQRActivity;
//import com.example.finalcut.tests.TestWorkNameActivity;
//import com.journeyapps.barcodescanner.ScanContract;
//import com.journeyapps.barcodescanner.ScanOptions;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class QRScan extends AppCompatActivity {
//    private String IP;
//    ArrayList<Ticket> tickets = new ArrayList<>();
//    WindowManager manager;
//    Map<String, String> infoMap = new HashMap<String, String>();
//    private String url; //= "http://10.10.111.90:3857";
//    private String BRANCH_ID;// ="2111";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qrscan);
//        scanQR();
//    }
//    private void scanQR(){
//
//        ScanOptions scanOptions = new ScanOptions();
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
//                url = "http://"+IP+":3857";
////                ScanFragment.WorkNameTask task = new ScanFragment.WorkNameTask(ScanFragment.WorkNameTask.class,url,BRANCH_ID);
////                task.execute();
//                Intent i = new Intent(this, MainActivity.class);
//                startActivity(i);
//            }catch (Exception e){
//                Intent i = new Intent(this, MainActivity.class);
//                startActivity(i);
////                AlertDialog.Builder builder = new AlertDialog.Builder(this);
////                builder.setTitle("Alert");
////                builder.setMessage("Invalid QR");
////
////                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        dialogInterface.dismiss();
////                    }
////                });
////                builder.show();
//            }
//        }
//
//    });
//}