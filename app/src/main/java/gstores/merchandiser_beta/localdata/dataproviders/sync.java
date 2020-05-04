package gstores.merchandiser_beta.localdata.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;

import gstores.merchandiser_beta.localdata.LocalDataHelper;
import gstores.merchandiser_beta.localdata.contracts.SyncContract;

public class sync<T> {
    final Class<T> typeParameterClass;
    public String tag;
    private T valueBase;
    public String value;
    public boolean synched;
    private Integer id;

    public sync(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
        this.tag = typeParameterClass.getName();
        this.id = null;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        if (value == null) {
            Gson gson = new Gson();
            value = gson.toJson(valueBase);
        }
        return value;
    }

    public T getValueBase() {
        if (valueBase != null) return valueBase;
        else if (value != null) {
            Gson gson = new Gson();
            valueBase =
                    gson.fromJson(value, typeParameterClass);
        } else valueBase = null;
        return valueBase;
    }

    public void setValueBase(T valueBase) {
        this.valueBase = valueBase;
    }

    public boolean getSynced() {
        return synched;
    }

    private void setTag(String tag) {
        this.tag = tag;
    }

    public void setSynced(boolean synced) {
        this.synched = synced;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public static ArrayList<sync> getSynchList(Context context, boolean isSynched) {
        ArrayList<sync> result = null;
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();

        Cursor cursor = db.query(SyncContract.Sync.TABLE_NAME,
                SyncContract.COLUMS,
                SyncContract.Sync.COLUMN_NAME_SYNCHED + "=? ",
                new String[]{(isSynched == true ? "1" : "0")},
                null,
                null,
                SyncContract.Sync.COLUMN_NAME_TAG);
        sync temp;
        if (cursor.moveToFirst()) {
            do {
                try {
                    String calssName = cursor.getString(cursor.getColumnIndex(SyncContract.Sync.COLUMN_NAME_TAG));
                    temp = new sync(Class.forName(calssName));
                    temp.setId(cursor.getInt(cursor.getColumnIndex(SyncContract.Sync.COLUMN_NAME_ROWID)));
                    temp.setValue(cursor.getString(cursor.getColumnIndex(SyncContract.Sync.COLUMN_NAME_VALUE)));
                    temp.setSynced(cursor.getInt(cursor.getColumnIndex(SyncContract.Sync.COLUMN_NAME_SYNCHED)) > 0);
                    result.add(temp);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        db.close();
        dataHelper.close();
        return null;
    }

    public boolean save(Context context) {
        long result = 0;
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(SyncContract.Sync.COLUMN_NAME_TAG, getTag());
            values.put(SyncContract.Sync.COLUMN_NAME_VALUE, getValue());
            values.put(SyncContract.Sync.COLUMN_NAME_SYNCHED, getSynced());

            db.beginTransaction();
            try {
                if (getId() == null)
                    result = db.insertOrThrow(SyncContract.Sync.TABLE_NAME,
                            null,
                            values);
                else if (getSynced())
                    db.delete(SyncContract.Sync.TABLE_NAME,
                            SyncContract.Sync.COLUMN_NAME_ROWID + "=?",
                            new String[]{this.getId().toString()});
                else
                    result = db.update(SyncContract.Sync.TABLE_NAME,
                            values,
                            SyncContract.Sync.COLUMN_NAME_ROWID + "=?",
                            new String[]{this.getId().toString()});
                db.setTransactionSuccessful();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            db.endTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        dataHelper.close();
        return result > 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
