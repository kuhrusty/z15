package com.kuhrusty.z15.model;

import java.util.List;

public interface ScenarioRepository {
    /**
     * Returns all scenarios.
     */
    List<Scenario> getScenarios();

    /**
     * Returns the scenario with the given ID, or null.
     */
    Scenario getScenario(String id);
}
