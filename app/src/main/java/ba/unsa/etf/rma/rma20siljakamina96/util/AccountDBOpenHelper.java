package ba.unsa.etf.rma.rma20siljakamina96.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RMADataBaseAccount.db";
    public static final int DATABASE_VERSION = 1;

    public AccountDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AccountDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String ACCOUNT_TABLE = "account";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_INTERNAL_ID = "internalId";
    public static final String ACCOUNT_BUDGET = "budget";
    public static final String ACCOUNT_TOTAL_LIMIT = "totalLimit";
    public static final String ACCOUNT_MONTH_LIMIT = "monthLimit";

    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " ("  + ACCOUNT_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ACCOUNT_ID + " INTEGER UNIQUE, "
                    + ACCOUNT_BUDGET + " DOUBLE, "
                    + ACCOUNT_TOTAL_LIMIT + " DOUBLE, "
                    + ACCOUNT_MONTH_LIMIT + " DOUBLE);";

    private static final String ACCOUNT_DROP = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACCOUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ACCOUNT_DROP);
        onCreate(db);
    }
}
