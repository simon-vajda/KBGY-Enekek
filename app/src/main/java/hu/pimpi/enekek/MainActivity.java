package hu.pimpi.enekek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.l4digital.fastscroll.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.pimpi.enekek.songs.SongItem;
import hu.pimpi.enekek.songs.SongsAdapter;
import hu.pimpi.enekek.utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    FastScrollRecyclerView recyclerView;
    ArrayList<SongItem> songs;
    SongsAdapter adapter;
    SearchView searchView;
    TextView noResultView;

    DatabaseHelper databaseHelper;

    private CharSequence searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        songs = new ArrayList<>();
        if(savedInstanceState == null) {
            try {
                songs.addAll(databaseHelper.getSongs());
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

        noResultView = findViewById(R.id.no_result_main);
        checkIfEmpty();
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
            public boolean onQueryTextChange(String query) {
                List<SongItem> results = databaseHelper.search(query);

                if(results != null) {
                    adapter.showResults(results);
                    recyclerView.smoothScrollToPosition(0);
                } else {
                    adapter.resetList();
                }

                checkIfEmpty();

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

    private void checkIfEmpty() {
        noResultView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}