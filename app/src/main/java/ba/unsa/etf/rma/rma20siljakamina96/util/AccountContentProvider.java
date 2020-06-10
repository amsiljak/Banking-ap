package ba.unsa.etf.rma.rma20siljakamina96.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AccountContentProvider extends ContentProvider {
    private static final int ALLROWS =1;
    private static final int ONEROW = 2;
    private static final UriMatcher uM;
    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.account","elements",ALLROWS);
        uM.addURI("rma.provider.account","elements/#",ONEROW);
    }
    AccountDBOpenHelper accountDBOpenHelper;
    @Override
    public boolean onCreate() {
        accountDBOpenHelper = new AccountDBOpenHelper(getContext(),
                AccountDBOpenHelper.DATABASE_NAME,null,
                AccountDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try{
            database=accountDBOpenHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=accountDBOpenHelper.getReadableDatabase();
        }
        String groupby=null;
        String having=null;
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();

        switch (uM.match(uri)){
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                squery.appendWhere(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID+"="+idRow);
            default:break;
        }
        squery.setTables(TransactionDBOpenHelper.TRANSACTION_TABLE);
        Cursor cursor = squery.query(database,projection,selection,selectionArgs,groupby,having,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uM.match(uri)){
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            case ONEROW:
                return "vnd.android.cursor.item/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: "+uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database;
        try{
            database=accountDBOpenHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=accountDBOpenHelper.getReadableDatabase();
        }
        long id = database.insert(TransactionDBOpenHelper.TRANSACTION_TABLE, null, values);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
