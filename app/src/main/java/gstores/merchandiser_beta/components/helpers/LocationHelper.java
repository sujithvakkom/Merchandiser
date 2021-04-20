package gstores.merchandiser_beta.components.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
/*
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
*/
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper implements Runnable, OnSuccessListener<Location> {
    private final Context context;
    private final FusedLocationProviderClient mFusedLocationClient;
    private final LocationCallback mLocationCallback;
    private onLocationHelperSuccessListener onLocationHelperSuccess;

    public LocationHelper(Context context) {
        this.context = context;
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location mCurrentLocation = locationResult.getLastLocation();
                if (onLocationHelperSuccess != null)
                    onLocationHelperSuccess.lastKnownLocation(mCurrentLocation);
            }
        };
    }

    public boolean isEnabled() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    public void getLastKnownLocation(@Nullable onLocationHelperSuccessListener successListener) {
        if(successListener!=null)
        setOnLocationHelperSuccess(successListener);
        if (onLocationHelperSuccess != null) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this);
            }
        }
    }

    public void setOnLocationHelperSuccess(onLocationHelperSuccessListener onLocationHelperSuccess) {
        this.onLocationHelperSuccess = onLocationHelperSuccess;
    }

    @Override
    public void onSuccess(Location location) {
        // Got last known location. In some rare situations this can be null.
        if (location != null) {
            if (onLocationHelperSuccess != null)
                onLocationHelperSuccess.lastKnownLocation(location);
        } else {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();

                if (ContextCompat.checkSelfPermission(this.context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback,
                            null);
                }
            }
        }
    }

    @Override
    public void run() {
        getLastKnownLocation(this.onLocationHelperSuccess);
    }

    public interface onLocationHelperSuccessListener {
        void lastKnownLocation(Location location);
    }
}
