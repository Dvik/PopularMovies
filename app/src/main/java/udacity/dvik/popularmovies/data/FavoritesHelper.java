package udacity.dvik.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Divya on 10/4/2016.
 */

public class FavoritesHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FavoritesHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "(" + FavoritesContract.FavoritesEntry.COLUMN_ID +
                " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_LANG + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_FAVORITED +
                " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_FAVORITES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "'");

        onCreate(sqLiteDatabase);
    }
}

