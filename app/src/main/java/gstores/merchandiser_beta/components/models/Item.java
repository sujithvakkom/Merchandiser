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

public class Item implements Parcelable {
    public static final String INVENTORY_ITEM_ID = "inventory_item_id";
    private static final String DESCRIPTION = "description";
    private static final String ITEM_CODE = "item_code";
    private static final String TABLE_NAME = "item_master";
    private Integer itemIDInteger;
    private ProductLine productLine;
    private Brand brand;
    private String descriptionString;
    private String itemCode;
    private Model model;
    private Category category;
    private int categoryId;
    private int modelId;
    private int brandId;

    public Item(int inventoryItemId, int categoryId, int modelId, int brandId,
                String itemCode, String itemName) {
        setItemIDInteger(inventoryItemId);
        this.categoryId = categoryId;
        this.modelId = modelId;
        this.brandId = brandId;
        setDescriptionString(itemName);
        setItemCode(itemCode);
    }

    public Item(int inventoryItem_id, Category category, Model model,
                Brand brand, String itemCode, String description) {
        this.setItemIDInteger(inventoryItem_id);
        this.setBrand(brand);
        this.setDescriptionString(description);
        this.setItemCode(itemCode);
        this.setModel(model);
        this.setCategory(category);

    }

    public Item() {
    }

    public static Brand GetBrand(Context context, Integer itemIDInteger) {
        Brand brand = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(ICommandBase.GET_ITEM_BRAND,
                    new String[]{itemIDInteger.toString()});
            if (cursor != null && cursor.moveToFirst()) {
                int brandId = cursor.getInt(cursor
                        .getColumnIndex(Brand.BRAND_ID));
                String description = cursor.getString(cursor
                        .getColumnIndex(DESCRIPTION));
                brand = new Brand(brandId, description);
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        databaseHelper.close();
        return brand;
    }

    public static ArrayList<Item> getItemList(Brand brand, Category category,
                                              Model model, Context context) {
        ArrayList<Item> itemList = new ArrayList<Item>();
        String brandCondition = brand == null ? "%" : brand.getBrandIDInteger()
                + "%";
        String categoryCondition = category == null ? "%" : category
                .getCategoryID() + "%";
        String modelCondition = model == null ? "%" : model.getMoldelID() + "%";
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_ITEM_MASTER, new String[]{
                brandCondition, categoryCondition, modelCondition});
        if (cursor != null && cursor.moveToFirst()) {
            do {

                itemList.add(new Item(
                        cursor.getInt(cursor.getColumnIndex(INVENTORY_ITEM_ID)),
                        Category.getCategory(cursor.getInt(cursor
                                .getColumnIndex(Category.CATEGORY_ID)), context),
                        Model.getModel(null, cursor.getInt(cursor
                                .getColumnIndex(Model.MODEL_ID)), context),
                        Brand.getBrand(cursor.getInt(cursor
                                .getColumnIndex(Brand.BRAND_ID)), context),
                        cursor.getString(cursor.getColumnIndex(ITEM_CODE)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION))));

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        databaseHelper.close();
        return itemList;
    }

    /**
     * @return the itemIDInteger
     */
    public Integer getItemIDInteger() {
        return itemIDInteger;
    }

    /**
     * @param itemIDInteger the itemIDInteger to set
     */
    public void setItemIDInteger(Integer itemIDInteger) {
        this.itemIDInteger = itemIDInteger;
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
     * @return the brand
     */
    public Brand getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(Brand brand) {
        this.brand = brand;
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

    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    public void insert(Context context) {
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INVENTORY_ITEM_ID, getItemIDInteger());
        values.put(ITEM_CODE, getItemCode());
        values.put(Brand.BRAND_ID, brandId);
        values.put(Category.CATEGORY_ID, categoryId);
        values.put(Model.MODEL_ID, modelId);
        values.put(DESCRIPTION, getDescriptionString());
        db.beginTransaction();
        try {
            long result = db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        }
        catch (SQLiteConstraintException ex){
            ContentValues valuesUpdate = new ContentValues();
            valuesUpdate.put(ITEM_CODE, getItemCode());
            valuesUpdate.put(Brand.BRAND_ID, brandId);
            valuesUpdate.put(Category.CATEGORY_ID, categoryId);
            valuesUpdate.put(Model.MODEL_ID, modelId);
            valuesUpdate.put(DESCRIPTION, getDescriptionString());
            long result = db.update(TABLE_NAME, valuesUpdate,INVENTORY_ITEM_ID+"=?",
                    new String[]{
                            getItemIDInteger().toString()
                    });
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
    }

    @Override
    public String toString() {
        return getModel().getDescriptionString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.itemIDInteger);
        dest.writeParcelable(this.productLine, flags);
        dest.writeParcelable(this.brand, flags);
        dest.writeString(this.descriptionString);
        dest.writeString(this.itemCode);
        dest.writeParcelable(this.model, 0);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(this.categoryId);
        dest.writeInt(this.modelId);
        dest.writeInt(this.brandId);
    }

    private Item(Parcel in) {
        this.itemIDInteger = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productLine = in.readParcelable(ProductLine.class.getClassLoader());
        this.brand = in.readParcelable(Brand.class.getClassLoader());
        this.descriptionString = in.readString();
        this.itemCode = in.readString();
        this.model = in.readParcelable(Model.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.categoryId = in.readInt();
        this.modelId = in.readInt();
        this.brandId = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
