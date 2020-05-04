package gstores.merchandiser_beta.components.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.localdata.ICommandBase;
import gstores.merchandiser_beta.localdata.LocalDataHelper;

public class Group implements Parcelable {
    public static final String GROUP_ID = "group_id";
    public static final Creator<Group> CREATOR = new Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
    private static final String GROUP_NAME = "group_name";
    private Integer groupIDInteger;
    private String nameString;

    public Group(Parcel in) {
        Integer tempInteger = in.readInt();
        groupIDInteger = tempInteger == -1 ? null : tempInteger;
        String tempString = in.readString();
        nameString = tempString == "" ? null : tempString;
    }

    public Group(Integer id, String name) {
        this.groupIDInteger = id;
        this.nameString = name;
    }

    public static Group getGroup(int groupID, Context context) {
        Group group = null;
        final String LOGIN = "1";
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICommandBase.GET_GROUP,
                new String[]{LOGIN});
        if (cursor != null && cursor.moveToFirst()) {
            group = new Group(groupID, cursor.getString(cursor
                    .getColumnIndex(GROUP_NAME)));
        }
        cursor.close();
        db.close();
        databaseHelper.close();
        return group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeInt(groupIDInteger);
        } catch (NullPointerException e) {
            dest.writeInt(AppLiterals.NULL_INTEGER);
            e.printStackTrace();
        }
        try {
            dest.writeString(nameString);
        } catch (Exception e) {
            dest.writeString(AppLiterals.NULL_STRING);
            e.printStackTrace();
        }
    }

}
