package gstores.merchandiser_beta.components.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import gstores.merchandiser_beta.localdata.ICommandBase;
import gstores.merchandiser_beta.localdata.LocalDataHelper;

public class Model implements Parcelable {
    public static final String MODEL_ID = "model_id";
    private static final String DESCRIPTION = "description";
    private static final String TABLE_NAME = "model_master";
    public Item item;
    private double Quantity;
    private double Value;
    private Integer moldelID;
    private String descriptionString;

    public Model(int id, String desc) {
        setMoldelID(id);
        setDescriptionString(desc);
    }


    public static ArrayList<Model> getAPIModelList(User user, Brand brand,
                                                   Category category, Context context){
    return null;
    }

    public static ArrayList<Model> getModelList(User user, Brand brand,
                                                Category category, Context context) {
        ArrayList<Model> modelList = new ArrayList<Model>();
        Model model = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String command = ICommandBase.GET_MODEL;
        String[] parameter = null;
        if (brand == null & category == null) {
            command = ICommandBase.GET_MODEL;
            parameter = null;
        } else if (brand == null & category != null) {
            command = ICommandBase.GET_CATEGORY_MODEL;
            parameter = new String[]{category.getCategoryID().toString()};
        } else if (brand != null & category == null) {
            command = ICommandBase.GET_BRAND_MODEL;
            parameter = new String[]{brand.getBrandIDInteger().toString()};
        } else if (brand != null & category != null) {
            command = ICommandBase.GET_BRAND_CATEGORY_MODEL;
            parameter = new String[]{brand.getBrandIDInteger().toString(),
                    category.getCategoryID().toString()};
        }

        Cursor cursor = db.rawQuery(command, parameter);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                model = new Model(
                        cursor.getInt(cursor.getColumnIndex(MODEL_ID)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                modelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        databaseHelper.close();
        return modelList;
    }

    public static Model getModel(User user, Integer modelInteger,
                                 Context context) {
        Model model = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_MODEL_FOR_ID,
                new String[]{modelInteger.toString()});
        if (cursor != null && cursor.moveToFirst()) {
            model = new Model(cursor.getInt(cursor.getColumnIndex(MODEL_ID)),
                    cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            cursor.close();
        }
        db.close();
        databaseHelper.close();
        return model;
    }

    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return the moldelID
     */
    public Integer getMoldelID() {
        return moldelID;
    }

    /**
     * @param moldelID the moldelID to set
     */
    public void setMoldelID(Integer moldelID) {
        this.moldelID = moldelID;
    }

    /**
     * @return the descriptionString
     */
    public String getDescriptionString() {
        // if (descriptionString != null)
        return descriptionString;
        // else {
        // return "";
        // }
    }

    /**
     * @param descriptionString the descriptionString to set
     */
    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    public String getBrandID(Context context) {
        String result = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_MODEL_BRAND,
                new String[]{this.moldelID.toString()});
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Brand.BRAND_ID));
        }
        cursor.close();
        db.close();
        databaseHelper.close();
        return result;
    }

    public String getProductLine(Context context) {
        String result = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_MODEL_PRODUCT_LINE,
                new String[]{this.moldelID.toString()});
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor
                    .getColumnIndex(ProductLine.PRODUCT_LINE_ID));
        }
        cursor.close();
        db.close();
        databaseHelper.close();
        return result;
    }

    @Override
    public String toString() {
        return getDescriptionString();
    }

    public void insert(Context context) {
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MODEL_ID, getMoldelID());
        values.put(DESCRIPTION, getDescriptionString());
        db.beginTransaction();
        try {
            long result = db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        }
        catch (SQLiteConstraintException ex){
            ContentValues valuesUpdate = new ContentValues();
            valuesUpdate.put(DESCRIPTION, getDescriptionString());
            db.update(TABLE_NAME, valuesUpdate,MODEL_ID+"=?",
                    new String[]{
                    getMoldelID().toString()
                    });
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
    }

    public Integer getInventoryItemID(Context context) {
        Integer inventoryItemId = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(ICommandBase.GET_MODEL_INVENTORY_ID,
                    new String[]{getMoldelID().toString()});
            if (cursor != null && cursor.moveToFirst()) {
                inventoryItemId = cursor.getInt(cursor
                        .getColumnIndex(Item.INVENTORY_ITEM_ID));
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        databaseHelper.close();
        this.item = new Item();
        item.setItemIDInteger(inventoryItemId);
        return inventoryItemId;
    }

    public String getDescriptionString(Context context) {
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_MODEL_NAME,
                new String[]{getMoldelID().toString()});
        String description = null;
        if (cursor != null && cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
            cursor.close();
        }
        db.close();
        databaseHelper.close();
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.item, flags);
        dest.writeValue(this.moldelID);
        dest.writeString(this.descriptionString);
    }

    private Model(Parcel in) {
        this.item = in.readParcelable(Item.class.getClassLoader());
        this.moldelID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.descriptionString = in.readString();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        public Model createFromParcel(Parcel source) {
            return new Model(source);
        }

        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }
}
