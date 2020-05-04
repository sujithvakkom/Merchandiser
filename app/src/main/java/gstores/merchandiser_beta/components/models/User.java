package gstores.merchandiser_beta.components.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.localdata.ICommandBase;
import gstores.merchandiser_beta.localdata.LocalDataHelper;

//import android.util.Log;

/**
 * @author srkrishnan
 */
public class User implements Parcelable {
    public static final String MERCHANT = "Merchant";
    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    static final String USER_ID = "user_id";
    static final String EMAIL = "email";
    static final String GROUP_ID = "group_id";
    private static final String PASSWORD = "password";
    private static final String USER_NAME = "user_name";
    private static final String IS_LOGIN = "is_login";
    private static final String TABLE_NAME = "user_master";
    private static final String FIRST_NAME = "first_name";
    private static final String SECOND_NAME = "second_name";
    private Integer userIDInteger;
    private String userNameString;
    private String passwordString;
    private String firstNameString;
    private String secondNameString;
    private String email;
    private Group group;

    public User(String userName, String password) {
        userNameString = userName;
        passwordString = password;
    }

    public User(Parcel in) {
        Integer tempInteger = in.readInt();
        userIDInteger = (tempInteger == AppLiterals.NULL_INTEGER) ? null
                : tempInteger;
        String tempString = in.readString();
        userNameString = tempString == AppLiterals.NULL_STRING ? null
                : tempString;
        tempString = in.readString();
        passwordString = tempString == AppLiterals.NULL_STRING ? null
                : tempString;
        tempString = in.readString();
        firstNameString = tempString == AppLiterals.NULL_STRING ? null
                : tempString;
        tempString = in.readString();
        secondNameString = tempString == AppLiterals.NULL_STRING ? null
                : tempString;
        try {
            group = in.readParcelable(null);
        } catch (BadParcelableException e) {
            e.printStackTrace();
        }
    }

    public User() {
    }

    /**
     * Get Any active sessions.
     *
     * @return User null if no sessions
     */
    public static User getCurrentUser(Context context) {
        User user = null;
        final String LOGIN = "1";
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(ICommandBase.GET_CURRENT_USER,
                    new String[]{LOGIN});
            if (cursor != null && cursor.moveToFirst()) {
                user = new User(cursor.getString(cursor
                        .getColumnIndex(USER_NAME)), cursor.getString(cursor
                        .getColumnIndex(PASSWORD)));
                user.setFirstNameString(cursor.getString(cursor
                        .getColumnIndex(FIRST_NAME)));
                user.setSecondNameString(cursor.getString(cursor
                        .getColumnIndex(SECOND_NAME)));
                user.setUserIDInteger(cursor.getInt(cursor
                        .getColumnIndex(USER_ID)));
                user.setGroup(Group.getGroup(
                        cursor.getInt(cursor.getColumnIndex(Group.GROUP_ID)),
                        context));
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        databaseHelper.close();

        return user;
    }

    public static Boolean login(String userNameString, String passwordString,
                                Context context, Boolean remember) {
        Boolean result;
        Integer noRowsUpdated = 0;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        final String isLogin = remember ? "1" : "0";
        values.put(User.IS_LOGIN, isLogin);
        try {
            noRowsUpdated = db.update(User.TABLE_NAME, values,
                    ICommandBase.USER_LOGIN_WHERE, new String[]{
                            userNameString, passwordString});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
        result = (noRowsUpdated == 1) ? true : false;
        //if (!result && AppLiterals.isConnectedToInterNet(context)) {
        User user = new User();
                //    User user = DownLoadManager.login(userNameString, passwordString);
        //    if (user != null) {
        //        result = true;
                user.insert(context, isLogin);
        //    }
        //}
        return result;
    }

    public static User getUser(Integer userID, Context context) {
        User user = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_USER, new String[]{userID.toString()});
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            user = new User(cursor.getString(cursor
                    .getColumnIndex(USER_NAME)), cursor.getString(cursor
                    .getColumnIndex(PASSWORD)));
            user.setFirstNameString(cursor.getString(cursor
                    .getColumnIndex(FIRST_NAME)));
            user.setSecondNameString(cursor.getString(cursor
                    .getColumnIndex(SECOND_NAME)));
            user.setUserIDInteger(cursor.getInt(cursor
                    .getColumnIndex(USER_ID)));
            user.setGroup(Group.getGroup(
                    cursor.getInt(cursor.getColumnIndex(Group.GROUP_ID)),
                    context));
        }
        db.close();
        databaseHelper.close();
        return user;
    }

    /**
     * @return the userIDInteger
     */
    public Integer getUserIDInteger() {
        return userIDInteger;
    }

    /**
     * @param userIDInteger the userIDInteger to set
     */
    public void setUserIDInteger(Integer userIDInteger) {
        this.userIDInteger = userIDInteger;
    }

    /**
     * @return the userNameString
     */
    public String getUserNameString() {
        return userNameString;
    }

    /**
     * @param userNameString the userNameString to set
     */
    public void setUserNameString(String userNameString) {
        this.userNameString = userNameString;
    }

    /**
     * @return the passwordString
     */
    public String getPasswordString() {
        return passwordString;
    }

    /**
     * @param passwordString the passwordString to set
     */
    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    /**
     * @return the firstNameString
     */
    public String getFirstNameString() {
        return firstNameString;
    }

    /**
     * @param firstNameString the firstNameString to set
     */
    public void setFirstNameString(String firstNameString) {
        this.firstNameString = firstNameString;
    }

    /**
     * @return the secondNameString
     */
    public String getSecondNameString() {
        return secondNameString;
    }

    /**
     * @param secondNameString the secondNameString to set
     */
    public void setSecondNameString(String secondNameString) {
        this.secondNameString = secondNameString;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    public String getUserName() {
        return userNameString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeInt(userIDInteger);
        } catch (NullPointerException e) {
            dest.writeInt(AppLiterals.NULL_INTEGER);
            e.printStackTrace();
        }
        try {
            dest.writeString(userNameString);
        } catch (NullPointerException e) {
            dest.writeString(AppLiterals.NULL_STRING);
            e.printStackTrace();
        }
        try {
            dest.writeString(passwordString);
        } catch (NullPointerException e1) {
            dest.writeString(AppLiterals.NULL_STRING);
            e1.printStackTrace();
        }
        try {
            dest.writeString(firstNameString);
        } catch (NullPointerException e1) {
            dest.writeString(AppLiterals.NULL_STRING);
            e1.printStackTrace();
        }
        try {
            dest.writeString(secondNameString);
        } catch (NullPointerException e1) {
            dest.writeString(AppLiterals.NULL_STRING);
            e1.printStackTrace();
        }
        try {
            dest.writeParcelable(group, 0);
        } catch (NullPointerException e) {
            dest.writeString(AppLiterals.NULL_STRING);
            e.printStackTrace();
        }
    }

    private long insert(Context context, String isLogin) {
        long result = 0;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, getUserIDInteger());
        values.put(USER_NAME, getUserName());
        values.put(PASSWORD, getPasswordString());
        values.put(FIRST_NAME, getFirstNameString());
        values.put(SECOND_NAME, getSecondNameString());
        values.put(EMAIL, getEmail());
        values.put(IS_LOGIN, isLogin);
        values.put(GROUP_ID, AppLiterals.DEFAULT_USER_GROUP_ID);
        try {
            result = db.insert(TABLE_NAME, null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Boolean logoff(Context context) {
        Boolean result;
        int noRowsUpdated = 0;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        final String LOGOFF = "0";
        values.put(User.IS_LOGIN, LOGOFF);
        try {
            noRowsUpdated = db.update(User.TABLE_NAME, values,
                    ICommandBase.USER_LOGIN_WHERE, new String[]{
                            userNameString, passwordString});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
        result = (noRowsUpdated == 1) ? true : false;
        return result;
    }

    public CharSequence getFullName() {
        if (getFirstNameString() == null)
            return getUserName();
        else
            return getFirstNameString() + " " + getSecondNameString();
    }
}
