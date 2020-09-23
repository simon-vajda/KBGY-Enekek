package hu.pimpi.enekek;

import android.content.Context;
import android.database.Cursor;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<SongItem> search(String query) {
        List<SongItem> songs = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT filename, snippet(songs, '<b>', '</b>', '...', 2, 20) as title_snippet, snippet(songs, '<b>', '</b>', '...', 3, 8) as lyrics_snippet " +
                        "FROM songs " +
                        "WHERE songs MATCH '?*'", new String[]{query});

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

        return songs;
    }
}
