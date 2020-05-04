package gstores.merchandiser_beta.localdata;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gstores.merchandiser_beta.localdata.contracts.SyncContract;
import gstores.merchandiser_beta.localdata.contracts.UserReaderContract;

public class LocalDataHelper extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 3;

    public LocalDataHelper(Context context) {
        super(context.getApplicationContext(), ICommandBase.NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        intiDatabase(db);
    }

    private Boolean intiDatabase(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.beginTransaction();
        Boolean success = true;
        for (String cmd : ICommandBase.SCRIPT_VERSION_1) {
            try {
                //Log.v(ApplicationConstants.DEVELOPER, cmd);
                db.execSQL(cmd);
            } catch (SQLException ex) {
                ex.printStackTrace();
                success = false;
            } finally {
            }
        }
        for (String cmd : ICommandBase.SCRIPT_VERSION_2) {
            try {
                db.execSQL(cmd);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
            }
        }
        for (String cmd : ICommandBase.SCRIPT_VERSION_3) {
            try {
                db.execSQL(cmd);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
            }
        }
        for (String cmd : ICommandBase.SCRIPT_VERSION_3) {
            try {
                db.execSQL(cmd);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
            }
        }
        db.execSQL(UserReaderContract.SQL_CREATE_USER);
        db.execSQL(SyncContract.SQL_CREATE_SYNCH);
        //Log.v(ApplicationConstants.DEVELOPER, "Transaction Successfully Completed.");
        db.setTransactionSuccessful();
        db.endTransaction();
        return success;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
            case  2:
                for (String cmd : ICommandBase.SCRIPT_VERSION_2) {
                    try {
                        db.execSQL(cmd);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                    }
                }
            case 3:
                for (String cmd : ICommandBase.SCRIPT_VERSION_3) {
                    try {
                        db.execSQL(cmd);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                    }
                }
            case 4:

                for (String cmd : ICommandBase.SCRIPT_VERSION_3) {
                    try {
                        db.execSQL(cmd);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                    }
                }
            case 5:

                for (String cmd : ICommandBase.SCRIPT_VERSION_4) {
                    try {
                        db.execSQL(cmd);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                    }
                }
        }
    }
}
