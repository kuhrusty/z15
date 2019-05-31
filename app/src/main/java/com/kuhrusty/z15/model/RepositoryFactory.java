package com.kuhrusty.z15.model;

import android.content.Context;

/**
 * Real sophisticated.
 */
public class RepositoryFactory {
    private static ScenarioRepository scenarioRepos = null;
    private static SoundtrackRepository stRepos = null;

    public static ScenarioRepository getScenarioRepository(Context context) {
        if (scenarioRepos == null) scenarioRepos = new JSONScenarioRepository(context);
        return scenarioRepos;
    }
    public static SoundtrackRepository getSoundtrackRepository(Context context) {
        if (stRepos == null) stRepos = new HardcodedSoundtracks();
        return stRepos;
    }
}
