package com.kuhrusty.z15.model;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class JSONScenarioRepositoryTest {
    @Test
    public void testRead() {
        ScenarioRepository sr = new JSONScenarioRepository(
                InstrumentationRegistry.getTargetContext());

        List<Scenario> scenarios = sr.getScenarios();
        //  Well, this is awkward.  Whether or not this test passes depends on
        //  whether the pref_show_ks_scenarios pref is set on the target device!
        assertEquals(15, scenarios.size());
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
