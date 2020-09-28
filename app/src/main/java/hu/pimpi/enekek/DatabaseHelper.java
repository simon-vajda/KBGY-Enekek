package hu.pimpi.enekek;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import hu.pimpi.enekek.assethelper.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "songs_fts5.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<SongItem> search(String query) {
        List<SongItem> songs = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT filename, snippet(songs, 2, '<b>', '</b>', '...', 20) as title_snippet, snippet(songs, 3, '<b>', '</b>', '...', 15) as lyrics_snippet " +
                        "FROM songs " +
                        "WHERE songs MATCH ? " +
                        "ORDER BY bm25(songs, 1.0, 1.0, 10.0, 5.0)"
        , new String[]{query+"*"});

        while (cursor.moveToNext()) {
            String filename = cursor.getString(0);
            String title = cursor.getString(1);
            String lyrics = cursor.getString(2);

            songs.add(new SongItem(filename, title, lyrics));
        }

        return songs;
    }

    public List<SongItem> getSongs() {
        List<SongItem> songs = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT filename, title FROM songs", null);

        while (cursor.moveToNext()) {
            String filename = cursor.getString(0);
            String title = cursor.getString(1);

            songs.add(new SongItem(filename, title, null));
        }

        //Collections.sort(songs, new NaturalOrderComparator());

        return songs;
    }
}
