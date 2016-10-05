package udacity.dvik.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Divya on 10/4/2016.
 */

public class FavoritesProvider extends ContentProvider {

    private static final String LOG_TAG = FavoritesProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoritesHelper mOpenHelper;

    private static final int FAVORITES = 100;
    private static final int FAVORITES_WITH_ID = 200;

    private static UriMatcher buildUriMatcher(){
  
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoritesContract.FavoritesEntry.TABLE_FAVORITES, FAVORITES);
        matcher.addURI(authority, FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "/#", FAVORITES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate(){
        mOpenHelper = new FavoritesHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FAVORITES:{
                return FavoritesContract.FavoritesEntry.CONTENT_DIR_TYPE;
            }
            case FAVORITES_WITH_ID:{
                return FavoritesContract.FavoritesEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case FAVORITES:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case FAVORITES_WITH_ID:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        projection,
                        FavoritesContract.FavoritesEntry.COLUMN_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FAVORITES: {
                long _id = db.insert(FavoritesContract.FavoritesEntry.TABLE_FAVORITES, null, values);

                if (_id > 0) {
                    returnUri = FavoritesContract.FavoritesEntry.buildFavoritesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case FAVORITES:
                numDeleted = db.delete(
                        FavoritesContract.FavoritesEntry.TABLE_FAVORITES, selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "'");
                break;
            case FAVORITES_WITH_ID:
                numDeleted = db.delete(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        FavoritesContract.FavoritesEntry.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match){
            case FAVORITES:
                db.beginTransaction();

                int numInserted = 0;
                try{
                    for(ContentValues value : values){
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try{
                            _id = db.insertOrThrow(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                                    null, value);
                        }catch(SQLiteConstraintException e) {
                        }
                        if (_id != -1){
                            numInserted++;
                        }
                    }
                    if(numInserted > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case FAVORITES:{
                numUpdated = db.update(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITES_WITH_ID: {
                numUpdated = db.update(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        contentValues,
                        FavoritesContract.FavoritesEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

}
