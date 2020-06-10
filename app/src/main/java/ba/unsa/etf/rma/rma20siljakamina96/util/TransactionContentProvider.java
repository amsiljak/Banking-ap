package ba.unsa.etf.rma.rma20siljakamina96.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

import static ba.unsa.etf.rma.rma20siljakamina96.util.TransactionDBOpenHelper.TRANSACTION_TABLE;

public class TransactionContentProvider extends ContentProvider {
    private static final int ALLROWS =1;
    private static final int ONEROW = 2;
    private static final UriMatcher uM;
    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.transactions","elements",ALLROWS);
        uM.addURI("rma.provider.transactions","elements/#",ONEROW);
    }

    TransactionDBOpenHelper transactionDBOpenHelper;
    @Override
    public boolean onCreate() {
        transactionDBOpenHelper = new TransactionDBOpenHelper(getContext(),
                TransactionDBOpenHelper.DATABASE_NAME,null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try{
            database=transactionDBOpenHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=transactionDBOpenHelper.getReadableDatabase();
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
        squery.setTables(TRANSACTION_TABLE);
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
            database=transactionDBOpenHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=transactionDBOpenHelper.getReadableDatabase();
        }
        long id = database.insert(TRANSACTION_TABLE, null, values);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db;
        try{
            db = transactionDBOpenHelper.getWritableDatabase();
        }catch (SQLiteException e){
            db = transactionDBOpenHelper.getReadableDatabase();
        }
        String groupBy = null;
        String having = null;
        int ret = 0;
        switch (uM.match(uri)){
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                String where = TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + "=" + idRow;
                ret = db.delete(TransactionDBOpenHelper.TRANSACTION_TABLE, where, selectionArgs);
                break;
            case ALLROWS:
                ret = db.delete(TransactionDBOpenHelper.TRANSACTION_TABLE, selection, selectionArgs);
                break;
        }
        return ret;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db;
        try{
            db = transactionDBOpenHelper.getWritableDatabase();
        }catch (SQLiteException e){
            db = transactionDBOpenHelper.getReadableDatabase();
        }

        int ret = 0;

        int typeOfURI = uM.match(uri);

        switch (typeOfURI) {
            case ALLROWS:
                ret = db.update(transactionDBOpenHelper.TRANSACTION_TABLE, values, "id=" + uri.getLastPathSegment(), null);
                break;
            case ONEROW:
                if(TextUtils.isEmpty(selection))
                    ret = db.update(transactionDBOpenHelper.TRANSACTION_TABLE, values, "id=" + uri.getLastPathSegment(), null);
                else
                    ret = db.update(transactionDBOpenHelper.TRANSACTION_TABLE, values, "internalId=" + selection, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }
}
