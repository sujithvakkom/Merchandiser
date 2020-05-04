package gstores.merchandiser_beta;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.Iterator;
import java.util.List;

import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.localdata.models.UserDetail;

import static gstores.merchandiser_beta.components.AppLiterals.REQUEST_FORGROUND_SERVICE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDetail token = Util.getToken(getApplicationContext());
        Intent activityIntent;
        if (token != null) {
            activityIntent = new Intent(this, HomeActivity.class);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {}
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.FOREGROUND_SERVICE},
                                REQUEST_FORGROUND_SERVICE);
                    }
                }
            }else {
                if (!isServiceRunning(GSMerchantService.class.getName()))
                    startServices();
            }
        }else {
            activityIntent = new Intent(this, LoginActivity.class);
        }
        startActivity(activityIntent);
        finish();
    }

    private void startServices() {
        Intent intentGSMerchantService = new Intent(this,GSMerchantService.class);
        //startService(intentGSMerchantService);
        ContextCompat.startForegroundService(this,intentGSMerchantService);
    }

    private boolean isServiceRunning(String serviceName){
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if(runningServiceInfo.service.getClassName().equals(serviceName)){
                serviceRunning = true;

                if(runningServiceInfo.foreground)
                {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }
}
