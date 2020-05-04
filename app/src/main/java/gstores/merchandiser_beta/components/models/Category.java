package gstores.merchandiser_beta.components.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

import gstores.merchandiser_beta.localdata.ICommandBase;
import gstores.merchandiser_beta.localdata.LocalDataHelper;

//import android.util.Log;

public class Category implements Parcelable {
    public final static String CATEGORY_ID = "category_id";
    public final static String DESCRIPTION = "description";
    public final static String TABLE_NAME = "catogery_master";
    private Integer categoryID;
    private String description;

    public Category(int categoryId, String description) {
        this.categoryID = categoryId;
        this.description = description;
    }

    public static ArrayList<Category> getCategoryList(Context context) {
        ArrayList<Category> categoryList = new ArrayList<Category>();
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_CATEGORY_LIST, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                categoryList.add(new Category(cursor.getInt(cursor
                        .getColumnIndex(CATEGORY_ID)), cursor.getString(cursor
                        .getColumnIndex(DESCRIPTION))));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        databaseHelper.close();

        return categoryList;
    }

    public static Category getCategory(Integer categoryID, Context context) {
        Category category = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_CATEGORY_WITH_ID,
                new String[]{categoryID.toString()});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                category = new Category(cursor.getInt(cursor
                        .getColumnIndex(CATEGORY_ID)), cursor.getString(cursor
                        .getColumnIndex(DESCRIPTION)));
            } while (cursor.moveToNext());
        }
        db.close();
        databaseHelper.close();
        return category;
    }

    /**
     * @return the categoryID
     */
    public Integer getCategoryID() {
        return categoryID;
    }

    /**
     * @param categoryID the categoryID to set
     */
    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public void insert(Context context) {
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_ID, getCategoryID());
        values.put(DESCRIPTION, getDescription());
        db.beginTransaction();
        try {
            db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return getDescription();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.categoryID);
        dest.writeString(this.description);
    }

    private Category(Parcel in) {
        this.categoryID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
