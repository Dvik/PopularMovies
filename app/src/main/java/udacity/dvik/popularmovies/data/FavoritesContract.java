package udacity.dvik.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Divya on 10/4/2016.
 */

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "udacity.dvik.popularmovies.data.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoritesEntry implements BaseColumns {

        public static final String TABLE_FAVORITES = "favorites";


        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_ORIGINAL_LANG = "originalLanguage";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_FAVORITED = "favorited";

        public static final String[] MOVIE_PROJECTION = {COLUMN_ID,COLUMN_POSTER_PATH,COLUMN_OVERVIEW,
                COLUMN_RELEASE_DATE,COLUMN_ORIGINAL_TITLE,
                COLUMN_ORIGINAL_LANG,COLUMN_TITLE,COLUMN_BACKDROP_PATH,
                COLUMN_VOTE_AVERAGE,COLUMN_FAVORITED };
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVORITES).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;

        public static Uri buildFavoritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
