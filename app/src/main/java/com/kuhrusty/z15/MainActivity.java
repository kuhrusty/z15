package com.kuhrusty.z15;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView sl = findViewById(R.id.scenarios);
        //  "use this setting to improve performance if you know that changes
        //  in content do not change the layout size of the RecyclerView"
        //  (leaving this out because some list elements may be bigger than
        //  others, if the scenario name wraps onto multiple lines)
        //sl.setHasFixedSize(true);
        sl.setLayoutManager(new LinearLayoutManager(this));
        ScenarioListAdapter sla = new ScenarioListAdapter(this, sl);
        sl.setAdapter(sla);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(sla);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        } else if (id == R.id.action_about) {
            openAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openAbout() {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra(HelpActivity.INTENT_URL,
                "file:///android_asset/" + getString(R.string.assets_subdir) + "/about.html");
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    public void startScenario(String scenarioID) {
        Intent intent = new Intent(this, ScenarioActivity.class);
        intent.putExtra(ScenarioActivity.INTENT_SCENARIO_ID, scenarioID);
        startActivity(intent);
    }
}
