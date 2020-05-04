package gstores.merchandiser_beta;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.helpers.LocationHelper;
import gstores.merchandiser_beta.components.helpers.PreferenceHelpers;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;
import gstores.merchandiser_beta.localdata.dataproviders.sync;
import gstores.merchandiser_beta.web.IWebClient;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static gstores.merchandiser_beta.components.AppLiterals.DELIVERY_CLASS_NAME;

public class GSMerchantService extends Service {
    private final static String TAG = "GSMerchantService";

    public GSMerchantService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                notificationIntent,
                0);

        Notification notification = new NotificationCompat.Builder(this,
                AppLiterals.CHANNEL_ID)
                .setContentTitle(AppLiterals.NOTIFICATION_TITLE)
                .setContentText("Service Started.")
                .setSmallIcon(R.drawable.ic_launcher_notification)
                .setContentIntent(pendingIntent).build();
        startForeground(1, notification);

        Thread thread = new Thread(new LocationBroadcaster());
        thread.start();

        return START_NOT_STICKY;//super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (Util.getInterval()),
                PendingIntent.getService(this,
                        0,
                        new Intent(this, GSMerchantService.class),
                        0)
        );
        */
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private class LocationBroadcaster implements Runnable {
        private final static String TAG = "LocationBroadcaster";

        @Override
        public void run() {
            do {
                try {
                    LocationHelper locationHelper = new LocationHelper(getApplicationContext());
                    locationHelper.getLastKnownLocation(new LocationHelper.onLocationHelperSuccessListener() {
                        @Override
                        public void lastKnownLocation(Location location) {
                            Double longitude = location.getLongitude();
                            Double lattitude = location.getLatitude();
                            Log.i(TAG,
                                    longitude.toString() + ", " + lattitude.toString()
                            );
                        }
                    });
                    Thread.sleep(Util.getInterval());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private class DataSync implements Runnable {
        private final static String TAG = "DataSync";

        @Override
        public void run() {

            try {
                do {
                    Thread.sleep(Util.getInterval());
                }
                while (true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private MultipartBody.Part prepareFilePart(String attachmentFile) {
            return null;
        }
    }
}