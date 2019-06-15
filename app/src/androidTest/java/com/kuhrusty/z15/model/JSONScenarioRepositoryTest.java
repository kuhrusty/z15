package com.kuhrusty.z15.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kuhrusty.z15.R;
import com.kuhrusty.z15.SettingsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.kuhrusty.z15.model.Tile.Direction.East;
import static com.kuhrusty.z15.model.Tile.Direction.North;
import static com.kuhrusty.z15.model.Tile.Direction.West;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class JSONScenarioRepositoryTest {
    private boolean saved_SHOW_KS_SCENARIOS = false;

    @Before
    public void fiddlePrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                InstrumentationRegistry.getTargetContext());
        if (prefs != null) {
            saved_SHOW_KS_SCENARIOS = prefs.getBoolean(
                    SettingsActivity.PREF_SHOW_KS_SCENARIOS, saved_SHOW_KS_SCENARIOS);
            prefs.edit().putBoolean(SettingsActivity.PREF_SHOW_KS_SCENARIOS, true).commit();
        }
    }

    @After
    public void unfiddlePrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                InstrumentationRegistry.getTargetContext());
        if (prefs != null) {
            prefs.edit().putBoolean(SettingsActivity.PREF_SHOW_KS_SCENARIOS, saved_SHOW_KS_SCENARIOS).commit();
        }
    }

    @Test
    public void testRead() {
        ScenarioRepository sr = new JSONScenarioRepository(
                InstrumentationRegistry.getTargetContext());

        List<Scenario> scenarios = sr.getScenarios();
        //  Well, this is awkward.  Whether or not this test passes depends on
        //  whether the pref_show_ks_scenarios pref is set on the target device!
        assertEquals(20, scenarios.size());
        check(scenarios.get(1), "2", "The End of Innocence - Part 2",
                Scenario.Difficulty.Prologue, Shufflers.Scenario2,
                Soundtrack.Type.Growl60, 1);
        check(scenarios.get(12), "13", "Trapped",
                Scenario.Difficulty.Hero, Shufflers.StandardLettersEvents,
                Soundtrack.Type.Growl60, 2);
        check(scenarios.get(14), "15", "The Alpha",
                Scenario.Difficulty.Hero, Shufflers.StandardEvents,
                Soundtrack.Type.Growl40, 1);

        Scenario ts = sr.getScenario("13");
        check(ts, "13", "Trapped",
                Scenario.Difficulty.Hero, Shufflers.StandardLettersEvents,
                Soundtrack.Type.Growl60, 2);

        ts = sr.getScenario("10");
        Map map = ts.getMap();
        assertNotNull(map);
        assertEquals(7, map.getWidth());
        assertEquals(4, map.getHeight());
        assertNull(map.getTile(0, 0));
        check(map.getTile(0, 1), "31-A", R.drawable.tile07a, East);
        check(map.getTile(1, 1), "11-B", R.drawable.tile11b, West, North);
        assertNull(map.getTile(6, 0));
    }

    public static void check(Scenario got, String expectID, String expectName,
                             Scenario.Difficulty expectDifficulty,
                             ZombieDeckShuffler expectShuffler,
                             Soundtrack.Type expectSoundtrackType,
                             int expectCardsPerGrowl) {
        assertEquals(expectID, got.getID());
        assertEquals(expectName, got.getName());
        assertEquals(expectDifficulty, got.getDifficulty());
        assertEquals(expectShuffler, got.getZombieDeckShuffler());
        assertEquals(expectSoundtrackType, got.getSoundtrackType());
        assertEquals(expectCardsPerGrowl, got.getCardsPerGrowl());
    }

    public static void check(Tile got, String expectID, int expectImgResID,
                             Tile.Direction... expectRoads) {
        assertEquals(expectID, got.getID());
        assertEquals(expectImgResID, got.getTileImageResID());
        assertEquals(expectRoads.length, got.getRoadCount());
        for (int ii = 0; ii < expectRoads.length; ++ii) {
            assertTrue(got.hasRoad(expectRoads[ii]));
        }
    }
}
