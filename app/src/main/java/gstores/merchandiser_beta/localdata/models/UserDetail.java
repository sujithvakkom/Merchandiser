package gstores.merchandiser_beta.localdata.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.localdata.IDataClass;
import gstores.merchandiser_beta.localdata.LocalDataHelper;
import gstores.merchandiser_beta.localdata.contracts.UserReaderContract;
public class UserDetail implements Serializable , IDataClass {

    public String user_name;
    public String full_name;
    public String vehicle_code;
    public String profile;

    public UserDetail(String userName, String fullName,String vehicleCode,String profile) {
        this.user_name = userName;
        this.full_name = fullName;
        this.vehicle_code = vehicleCode;
        this.profile = profile;
    }

    public static UserDetail getUser(Context context){
        UserDetail userDetail = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(UserReaderContract.SQL_GET_USER,null);
            if (cursor != null && cursor.moveToFirst()) {
                String userName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_USER_NAME));
                String fullName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_FULL_NAME));
                String vehicleCode = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_VEHICLE_CODE));
                String profile = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_PROFILE));
                userDetail = new UserDetail(userName, fullName,vehicleCode,profile);
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        databaseHelper.close();
        return userDetail;

    }

    public static LoginToken getUserToken(Context context){
        LoginToken userDetail = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(UserReaderContract.SQL_GET_USER,null);
            if (cursor != null && cursor.moveToFirst()) {
                String userName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_USER_NAME));
                String fullName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_FULL_NAME));
                String vehicleCode = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_VEHICLE_CODE));
                String profile = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_PROFILE));
                userDetail = new LoginToken(userName, fullName,vehicleCode,profile);
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        databaseHelper.close();
        return userDetail;

    }

    public static boolean Logoff(Context context) {
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            db.delete(UserReaderContract.User.TABLE_NAME, null, null);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public long save(Context context){
        long result =-1;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserReaderContract.User.COLUMN_NAME_USER_NAME, user_name);
        values.put(UserReaderContract.User.COLUMN_NAME_FULL_NAME, full_name);
        values.put(UserReaderContract.User.COLUMN_NAME_VEHICLE_CODE, vehicle_code);
        values.put(UserReaderContract.User.COLUMN_NAME_PROFILE, profile);

        db.beginTransaction();
        try {
            result = db.insertOrThrow(UserReaderContract.User.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
        return  result;
    }

    public String getRole() {
        try {
            String role = Util.getJsonValue(this.profile, "root", "role");
            return role;
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
}
