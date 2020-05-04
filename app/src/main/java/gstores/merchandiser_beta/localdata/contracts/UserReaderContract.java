package gstores.merchandiser_beta.localdata.contracts;

import android.provider.BaseColumns;

public class UserReaderContract {
    private UserReaderContract(){}
    public   static final String SQL_CREATE_USER =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User.COLUMN_NAME_USER_NAME+ " TEXT PRIMARY KEY," +
                    User.COLUMN_NAME_FULL_NAME + " TEXT,"+
                    User.COLUMN_NAME_VEHICLE_CODE + " TEXT,"+
                    User.COLUMN_NAME_PROFILE + " TEXT"+
                    ")";

    public static final String SQL_DROP_USER =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    public static final String SQL_GET_USER =
            "SELECT " +User.COLUMN_NAME_USER_NAME+", "+
                    User.COLUMN_NAME_FULL_NAME+", "+
                    User.COLUMN_NAME_VEHICLE_CODE+", "+
                    User.COLUMN_NAME_PROFILE+
                    " FROM "+User.TABLE_NAME;

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_NAME= "user_name";
        public static final String COLUMN_NAME_FULL_NAME = "full_name";
        public static final String COLUMN_NAME_VEHICLE_CODE = "vehicle_code";
        public static final String COLUMN_NAME_PROFILE = "profile";
    }
}
