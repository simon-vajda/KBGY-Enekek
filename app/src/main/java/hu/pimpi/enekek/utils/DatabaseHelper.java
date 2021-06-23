package hu.pimpi.enekek.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hu.pimpi.enekek.assethelper.SQLiteAssetHelper;
import hu.pimpi.enekek.songs.SongItem;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "songs_fts5.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    private boolean isNumber(String s) {
        int i = 0;

        if(s.charAt(0) == 'b' || s.charAt(0) == 'B') {
            if(s.length() == 1)
                return false;
            else
                i = 1;
        }

        for(; i < s.length(); ++i) {
            if(s.charAt(i) < '0' || '9' < s.charAt(i)) return false;
        }

        return true;
    }

    public List<SongItem> search(String st) {
        if(st == null)
            return null;

        String searchText = st.toLowerCase().replaceAll("[^a-z0-9á-ű ]|[ ]{2,}", "").trim();

        if(searchText.isEmpty())
            return null;

        String orderedQuery = "SELECT filename, snippet(songs, 0, '<b>', '</b>', '...', 20) as title_snippet, snippet(songs, 1, '<b>', '</b>', '...', 15) as lyrics_snippet " +
                "FROM songs " +
                "WHERE songs MATCH ? AND rank MATCH 'bm25(5.0, 1.0)' " +
                "ORDER BY rank";

        String unorderedQuery = "SELECT filename, snippet(songs, 0, '<b>', '</b>', '...', 20) as title_snippet, snippet(songs, 1, '<b>', '</b>', '...', 15) as lyrics_snippet " +
                "FROM songs " +
                "WHERE songs MATCH ?";

        StringBuilder matchCondition = new StringBuilder(searchText);
        matchCondition.append(" OR ");

        for (String s : searchText.split(" ")) {
            matchCondition.append(s + "* ");
        }

        Cursor cursor = getReadableDatabase().rawQuery(isNumber(searchText) ? unorderedQuery : orderedQuery, new String[]{matchCondition.toString()});
        List<SongItem> songs = new ArrayList<>();

        while (cursor.moveToNext()) {
            String filename = cursor.getString(0);
            String title = cursor.getString(1);
            String lyrics = cursor.getString(2);

            songs.add(new SongItem(filename, title, lyrics));
        }

        cursor.close();
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

        cursor.close();
        return songs;
    }

    public int getNumberOfSongs() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT title FROM songs", null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
