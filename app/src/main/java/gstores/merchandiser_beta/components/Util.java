package gstores.merchandiser_beta.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import gstores.merchandiser_beta.R;
import gstores.merchandiser_beta.components.helpers.PreferenceHelpers;
import gstores.merchandiser_beta.localdata.dataproviders.sync;
import gstores.merchandiser_beta.localdata.logEnabled;
import gstores.merchandiser_beta.localdata.models.LoginToken;
import gstores.merchandiser_beta.localdata.models.UserDetail;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import static gstores.merchandiser_beta.components.Util.SETTINGS_TAG.BUSINESS_EXCEL;

public class Util {
    private static final String TAG = "Util";


    public static long getInterval() {
        long min = 60000;
        return min*1;
    }

    public static Snackbar SimpleSnackBar(View parentView, String messgage, int lengthLong) {
        return Snackbar.make(parentView, messgage, lengthLong);
    }

    public static String getSettings(String TAG) {
        switch (TAG) {
            case BUSINESS_EXCEL:
                return "http://businessexcel.gsmobileapps.com";
        }
        return null;
    }

    public static List<String>   getProfiles() {
        List<String> ProfileItems = new ArrayList<>();
        ProfileItems.add("Brands");
        return ProfileItems;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * R.string.pref_head_office_key
     * @return A new instance of fragment DeliveryJobsFragment.
     */
    public static String getSettingById(Context context,int id){
        String defaultValue;
        switch (id){
            case R.string.pref_head_office_key:
                defaultValue = context.getResources().getString(
                        R.string.pref_head_office_default
                );
                break;
            default:
                defaultValue = "";
        }

        String settings = PreferenceHelpers.getPreference(
                PreferenceManager.getDefaultSharedPreferences(context),
                context.getResources().getString(id),
                defaultValue);

        return settings;
    }

    public static LoginToken getToken(Context context) {
        LoginToken user = UserDetail.getUserToken(context);
        return  user;
    }

    public static String getJsonValue(String jSON,String root,String key){
        String value = null;
        try {
            JSONObject reader = new JSONObject(jSON);
            JSONObject main = reader.getJSONObject(root);
            value = main.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return  value;
    }

    public static void Logoff(Context applicationContext) {
        UserDetail.Logoff(applicationContext);
    }

    /***
     * Get current date as integer string
     * @return
     */
    public static String getTodayDate() {
        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:",String.valueOf(todaysDate));
        return(String.valueOf(todaysDate));
    }

    /***
     * Get current time as integer string
     * @return
     */
    public static String getCurrentTimeString() {

        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:",String.valueOf(currentTime));
        return(String.valueOf(currentTime));

    }

    /***
     * Get current Date
     * @return
     */
    public static Date getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    /**
     * Add to syncable items to sync for later
     * */
    public  static <T> void addToSynch(T value, String updateRemark) {
        try {
            ((logEnabled)value).setRemarkForLog(updateRemark);
            sync<T> sync = new sync( Class.forName(value.getClass().getName()));
            sync.setValueBase(value);
            sync.setSynced(false);
            sync.save(getApplicationContext());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public final static  byte[] BitmapEncode(Bitmap bitmap)
    {
        final int lnth=bitmap.getByteCount();
        ByteBuffer dst= ByteBuffer.allocate(lnth);
        bitmap.copyPixelsToBuffer( dst);
        byte[] barray=dst.array();
        return  barray;
    }

    public final static  Bitmap BtmapDecode(byte[] byte_array) {
        Bitmap bitmap = new BitmapFactory().decodeByteArray(byte_array, 0/* starting index*/, byte_array.length/*no of byte to read*/);
        return bitmap;
    }

    public final static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public final static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public final static class SETTINGS_TAG {
        public final static String BUSINESS_EXCEL = "businessexcel";
    }
}
