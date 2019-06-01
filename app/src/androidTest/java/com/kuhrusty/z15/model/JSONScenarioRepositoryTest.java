package com.kuhrusty.z15.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kuhrusty.z15.SettingsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
}
