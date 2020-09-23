package hu.pimpi.enekek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<SongItem> songs;
    SongsAdapter adapter;
    SearchView searchView;

    private CharSequence searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();
        setContentView(R.layout.activity_main);

        songs = new ArrayList<>();
        if(savedInstanceState == null) {
            try {
                loadFiles();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            songs = savedInstanceState.getParcelableArrayList("songs");
        }

        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongsAdapter(songs);
        adapter.setOnItemClickListener(new SongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), LyricsActivity.class);
                intent.putExtra("filename", songs.get(position).getFilename());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    void loadFiles() throws IOException, ParserConfigurationException, SAXException {
        for(String filename : getAssets().list("songs")) {
            InputStream is = getAssets().open("songs/" + filename);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            SongItem item = new SongItem(filename, ((Element) doc.getElementsByTagName("title").item(0)).getTextContent(), "");
            songs.add(item);
        }

        Collections.sort(songs, new NaturalOrderComparator());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("songs", songs);
        outState.putParcelableArrayList("fullList", adapter.getFullList());
        if(searchView != null)
            outState.putCharSequence("searchQuery", searchView.getQuery());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.setFullList(savedInstanceState.<SongItem>getParcelableArrayList("fullList"));
        searchQuery = savedInstanceState.getCharSequence("searchQuery");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        if(searchQuery != null && searchQuery.length() > 0) {
            searchItem.expandActionView();
            searchView.setQuery(searchQuery, false);
            searchView.clearFocus();
        }

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("theme_preference", "-1");

        if(theme.equals("1")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if(theme.equals("2")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}