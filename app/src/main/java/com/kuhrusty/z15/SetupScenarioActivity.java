package com.kuhrusty.z15;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kuhrusty.z15.model.Map;
import com.kuhrusty.z15.model.RepositoryFactory;
import com.kuhrusty.z15.model.Scenario;
import com.kuhrusty.z15.model.Tile;
import com.kuhrusty.z15.ui.MapView;
import com.kuhrusty.z15.ui.UIUtil;

import java.util.ArrayList;
import java.util.Collections;

import static com.kuhrusty.z15.ScenarioActivity.INTENT_SCENARIO_ID;

/**
 * This displays the information you need for setting up a scenario.
 */
public class SetupScenarioActivity extends AppCompatActivity {
    private static final String LOGBIT = "SetupScenarioActivity";

    private static final String BUNDLE_SCENARIO_ID = "scenarioID";
    private static final String BUNDLE_HIGHLIGHT_STEP = "highlightStep";

    private MapView mapView;
    private View noTilesButton;
    private View prevTileButton;
    private View nextTileButton;
    private View allTilesButton;

    private Scenario scenario;
    private ArrayList<String> tilesInOrder = new ArrayList<>();
    private int highlightStep = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_scenario);
        mapView = findViewById(R.id.mapView);
        noTilesButton = findViewById(R.id.noTilesButton);
        prevTileButton = findViewById(R.id.prevTileButton);
        nextTileButton = findViewById(R.id.nextTileButton);
        allTilesButton = findViewById(R.id.allTilesButton);

        String scenarioID = null;
        if (savedInstanceState != null) {
            scenarioID = savedInstanceState.getString(BUNDLE_SCENARIO_ID);
            Log.d(LOGBIT, "onCreate() savedInstanceState, scenarioID " +
                    scenarioID);
            highlightStep = savedInstanceState.getInt(BUNDLE_HIGHLIGHT_STEP);
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                scenarioID = intent.getStringExtra(INTENT_SCENARIO_ID);
            }
            Log.d(LOGBIT, "onCreate() intent, scenarioID " + scenarioID);
        }

        if (scenarioID != null) {
            scenario = RepositoryFactory.getScenarioRepository(this).getScenario(scenarioID);
            if (scenario == null) {
                Toast.makeText(this, "gackk, life is bad, no scenario " +
                        scenarioID, Toast.LENGTH_LONG).show();
            } else {
                UIUtil.formatScenarioTitle(
                        (TextView)(findViewById(R.id.scenarioName)), scenario);
                Map map = scenario.getMap();
                if (map != null) {
                    mapView.setMap(map);
                    for (int row = 0; row < map.getHeight(); ++row) {
                        for (int col = 0; col < map.getWidth(); ++col) {
                            Tile tt = map.getTile(col, row);
                            if (tt != null) tilesInOrder.add(tt.getID());
                        }
                    }
                    Collections.sort(tilesInOrder, Tile.IDStringComparator);
                }
            }
        }

        if (highlightStep == -1) {
            onShowNoTilesClicked(null);
        } else if (highlightStep == tilesInOrder.size()) {
            onShowAllTilesClicked(null);
        } else {
            int target = highlightStep;
            highlightStep = -1;
            for (int ii = 0; ii <= target; ++ii) {
                onShowNextTileClicked(null);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (scenario != null) savedInstanceState.putString(BUNDLE_SCENARIO_ID, scenario.getID());
        savedInstanceState.putInt(BUNDLE_HIGHLIGHT_STEP, highlightStep);
        //  Note that this doesn't exactly save their selected tile; if they've
        //  tapped on tile A-1, so it's currently magnified, we're losing that
        //  fact here.  "Sorry."
    }

    public void onShowNoTilesClicked(View view) {
        mapView.dimAll();
        highlightStep = -1;
        noTilesButton.setEnabled(false);
        prevTileButton.setEnabled(false);
        nextTileButton.setEnabled(true);
        allTilesButton.setEnabled(true);
    }
    public void onShowPrevTileClicked(View view) {
        if (highlightStep > -1) --highlightStep;
        mapView.undimAndSelectTile((highlightStep >= 0) ?
                        tilesInOrder.get(highlightStep) : null,
                highlightStep + 1 < tilesInOrder.size() ?
                        tilesInOrder.get(highlightStep + 1) : null);
        noTilesButton.setEnabled(highlightStep >= 0);
        prevTileButton.setEnabled(highlightStep >= 0);
        nextTileButton.setEnabled(true);
        allTilesButton.setEnabled(true);
    }
    public void onShowNextTileClicked(View view) {
        if (highlightStep < tilesInOrder.size()) ++highlightStep;
        mapView.undimAndSelectTile((highlightStep < tilesInOrder.size()) ?
                tilesInOrder.get(highlightStep) : null, null);
        noTilesButton.setEnabled(true);
        prevTileButton.setEnabled(true);
        nextTileButton.setEnabled(highlightStep < tilesInOrder.size());
        allTilesButton.setEnabled(highlightStep < tilesInOrder.size());
    }
    public void onShowAllTilesClicked(View view) {
        mapView.undimAll();
        highlightStep = tilesInOrder.size();
        noTilesButton.setEnabled(true);
        prevTileButton.setEnabled(true);
        nextTileButton.setEnabled(false);
        allTilesButton.setEnabled(false);
    }

    /**
     * The user is done setting up & wants to play the scenario!
     */
    public void onStartScenarioClicked(View view) {
        Intent intent = new Intent(this, ScenarioActivity.class);
        intent.putExtra(INTENT_SCENARIO_ID, scenario.getID());
        startActivity(intent);
    }
}
