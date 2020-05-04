package gstores.merchandiser_beta.localdata.contracts;

import android.provider.BaseColumns;

public class SyncContract {
    private SyncContract(){}

    public   static final String SQL_CREATE_SYNCH =
            "CREATE TABLE " + Sync.TABLE_NAME + " (" +
                    Sync.COLUMN_NAME_TAG+ " TEXT"+
                    "," +
                    Sync.COLUMN_NAME_VALUE + " TEXT "+
                    "," +
                    Sync.COLUMN_NAME_SYNCHED + " INTEGER "+
                    ")";

    public static final String SQL_DROP_SYNCH =
            "DROP TABLE IF EXISTS " + Sync.TABLE_NAME;

    public static final String SQL_GET_SYNCH =
            "SELECT " + Sync.COLUMN_NAME_TAG+
                    ", "+
                    Sync.COLUMN_NAME_VALUE +
                    ", " +
                    Sync.COLUMN_NAME_SYNCHED +
                    " FROM "+ Sync.TABLE_NAME;
    public static  final  String[] COLUMS ={
            Sync.COLUMN_NAME_ROWID,
            Sync.COLUMN_NAME_TAG,
            Sync.COLUMN_NAME_VALUE ,
            Sync.COLUMN_NAME_SYNCHED
    };

    public static class Sync implements BaseColumns {
        public static final String TABLE_NAME = "sync";
        public static final String COLUMN_NAME_ROWID = "rowid";
        public static final String COLUMN_NAME_TAG = "tag";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_SYNCHED = "synched";
    }
}