//package com.example.finalcut.tests;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.finalcut.R;
//import com.journeyapps.barcodescanner.CaptureActivity;
//import com.journeyapps.barcodescanner.ScanContract;
//import com.journeyapps.barcodescanner.ScanOptions;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ScanQRActivity extends AppCompatActivity {
//    TextView txt;
//    String IP;
//    String Branch;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_qractivity);
//        txt=findViewById(R.id.scanText);
//        scanQR();
//
//
//    }
//    private void scanQR(){
//        ScanOptions scanOptions = new ScanOptions();
//        scanOptions.setPrompt("A volume up to flash on");
//        scanOptions.setBeepEnabled(false);
//        scanOptions.setOrientationLocked(true);
//        scanOptions.setCaptureActivity(CaptureAct.class);
//        barlauncher.launch(scanOptions);
//
//    }
//    ActivityResultLauncher<ScanOptions> barlauncher= registerForActivityResult(new ScanContract(),result -> {
//
//        if (result!=null) {
//            Map<String, String> myMap = new HashMap<String, String>();
//            String s = result.getContents();
//            try {
//                s= s.substring(1,s.length()-1);
//                s= s.replaceAll("'","").trim();
//                String[] pairs = s.split(",");
//                for (int i=0;i<pairs.length;i++) {
//                    String pair = pairs[i].trim();
//                    String[] keyValue = pair.split(":");
//                    myMap.put(keyValue[0].trim(), keyValue[1].trim());
//                }
//                IP = myMap.get("IP");
//                Branch = myMap.get("BRANCH_ID");
//                IP = IP.substring(1,IP.length()-1);
//                Branch = Branch.substring(1,Branch.length()-1);
//                txt.setText(IP+" "+Branch);
////                String url = "http://"+IP+":3857";
////
//             TestWorkNameActivity.WorkNameTask workNameTask = new TestWorkNameActivity.WorkNameTask(IP,Branch);
//             workNameTask.execute();
//
//                System.out.println(IP+" "+Branch);
//                Intent i = new Intent(ScanQRActivity.this, TestWorkNameActivity.class);
//                i.putExtra("IP",IP);
//                i.putExtra("BRANCH",Branch);
//                startActivity(i);
//
//            }catch (Exception e){
//                AlertDialog.Builder builder = new AlertDialog.Builder(ScanQRActivity.this);
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
//
//
//        }
//    });
//}