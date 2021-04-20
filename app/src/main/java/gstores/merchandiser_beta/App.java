package gstores.merchandiser_beta;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.printutil.SunmiPrintHelper;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        initPrinter();
    }

    private void initPrinter() {
        try {
            SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
        }catch (Exception ex){ ex.printStackTrace();}
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    AppLiterals.CHANNEL_ID,
                    AppLiterals.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
