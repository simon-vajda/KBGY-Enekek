package hu.pimpi.enekek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import hu.pimpi.enekek.utils.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHelper db;

    long lastClick = 0;
    int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);
        int songCount = db.getNumberOfSongs();

        String infoMessage = getString(R.string.info) + "\n" +
                getString(R.string.num_of_songs) + ": " + songCount + "\n" +
                getString(R.string.application) + ": v" + BuildConfig.VERSION_NAME;

        TextView versionText = findViewById(R.id.settings_version);
        versionText.setText(infoMessage);
    }

    public void onInfoClick(View view) {
        if(System.currentTimeMillis() - lastClick < 500) {
            if(++clickCount == 2) {
                Toast.makeText(this, R.string.easter_egg, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LyricsActivity.class);
                intent.putExtra("filename", "Béke vár rám ().xml");
                startActivity(intent);
                clickCount = 0;
            }
        } else {
            clickCount = 0;
        }

        lastClick = System.currentTimeMillis();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private String theme;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            ListPreference themePreference = findPreference("theme_preference");
            themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    theme = newValue.toString();
                    setTheme();
                    getActivity().recreate();
                    return true;
                }
            });
        }

        private void setTheme() {
            if(theme.equals("1")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if(theme.equals("2")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        }
    }
}