package gstores.merchandiser_beta.components.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.localdata.ICommandBase;
import gstores.merchandiser_beta.localdata.LocalDataHelper;

public class ProductLine implements Parcelable {
    public static final String PRODUCT_LINE_ID = "product_line_id";
    private static final String DESCRIPTION = "description";
    private Integer productLineID;
    private String description;

    public ProductLine(int productLineID, String description) {
        setProductLineID(productLineID);
        setDescription(description);
    }

    public static ArrayList<ProductLine> getProductLineList(User user,
                                                            Context context) {
        ArrayList<ProductLine> productLines = new ArrayList<ProductLine>();
        ProductLine line;

        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_PRODUCT_LINE, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                line = new ProductLine(cursor.getInt(cursor
                        .getColumnIndex(PRODUCT_LINE_ID)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                productLines.add(line);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        databaseHelper.close();
        return productLines;
    }

    /**
     * @return the productLineID
     */
    public Integer getProductLineID() {
        return productLineID;
    }

    /**
     * @param productLineID the productLineID to set
     */
    public void setProductLineID(Integer productLineID) {
        this.productLineID = productLineID;
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

    @Override
    public String toString() {
        String temp = getProductLineID() + AppLiterals.HYPHON
                + getDescription();
        return temp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.productLineID);
        dest.writeString(this.description);
    }

    private ProductLine(Parcel in) {
        this.productLineID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
    }

    public static final Creator<ProductLine> CREATOR = new Creator<ProductLine>() {
        public ProductLine createFromParcel(Parcel source) {
            return new ProductLine(source);
        }

        public ProductLine[] newArray(int size) {
            return new ProductLine[size];
        }
    };
}
