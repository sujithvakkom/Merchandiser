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

public class Brand implements Parcelable {
    public static final String BRAND_ID = "brand_id";
    private static final String DESCRIPTION = "description";
    private static final String TABLE_NAME = "brand_master";
    private ProductLine productLine;
    private Integer brandIDInteger;
    private String descriptionString;

    public Brand(int id, String desc) {
        setBrandIDInteger(id);
        setDescriptionString(desc);
    }

    public static ArrayList<Brand> getBrandList(User user,
                                                ProductLine productLine, Context context) {
        ArrayList<Brand> brandList = new ArrayList<Brand>();
        Brand brand = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                productLine == null ? ICommandBase.GET_BRAND
                        : ICommandBase.GET_PRODUCT_LINE_BRAND,
                productLine == null ? null : new String[]{productLine
                        .getProductLineID().toString()});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                brand = new Brand(
                        cursor.getInt(cursor.getColumnIndex(BRAND_ID)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                brandList.add(brand);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        databaseHelper.close();
        return brandList;
    }

    public static Brand getBrand(Integer brandId, Context context) {
        Brand brand = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_BRAND_WITH_ID,
                new String[]{brandId.toString()});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                brand = new Brand(
                        cursor.getInt(cursor.getColumnIndex(BRAND_ID)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            } while (cursor.moveToNext());
        }
        db.close();
        databaseHelper.close();
        return brand;
    }

    /**
     * @return the productLine
     */
    public ProductLine getProductLine() {
        return productLine;
    }

    /**
     * @param productLine the productLine to set
     */
    public void setProductLine(ProductLine productLine) {
        this.productLine = productLine;
    }

    /**
     * @return the brandIDInteger
     */
    public Integer getBrandIDInteger() {
        return brandIDInteger;
    }

    /**
     * @param brandIDInteger the brandIDInteger to set
     */
    public void setBrandIDInteger(Integer brandIDInteger) {
        this.brandIDInteger = brandIDInteger;
    }

    /**
     * @return the descriptionString
     */
    public String getDescriptionString() {
        return descriptionString;
    }

    /**
     * @param descriptionString the descriptionString to set
     */
    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return getDescriptionString();
    }

    public void insert(Context context) {
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BRAND_ID, brandIDInteger);
        values.put(DESCRIPTION, descriptionString);
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productLine, 0);
        dest.writeValue(this.brandIDInteger);
        dest.writeString(this.descriptionString);
    }

    private Brand(Parcel in) {
        this.productLine = in.readParcelable(ProductLine.class.getClassLoader());
        this.brandIDInteger = (Integer) in.readValue(Integer.class.getClassLoader());
        this.descriptionString = in.readString();
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
}
