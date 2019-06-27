package com.kuhrusty.z15.model;

import android.support.annotation.NonNull;

public class Scenario {
    public enum Difficulty {
        Prologue, Beginner, Survivor, Hero
    }
    private String id;
    private String name;
    private Difficulty difficulty;
    private Soundtrack.Type soundtrack;
    private ZombieDeckShuffler zdShuffler;
    private int cardsPerGrowl = 1;
    private Map map;

    /**
     * Copies non-null members from the other Scenario into this one.  (This is
     * so that JSONScenarioRepository can load one file of language-independent
     * stuff, and another file of language-dependent stuff, and merge the two.)
     */
    public void mergeFrom(@NonNull Scenario other) {
        if ((id == null) && (other.id != null)) id = other.id;
        if ((name == null) && (other.name != null)) name = other.name;
        if ((difficulty == null) && (other.difficulty != null)) difficulty = other.difficulty;
        if ((soundtrack == null) && (other.soundtrack != null)) soundtrack = other.soundtrack;
        if ((zdShuffler == null) && (other.zdShuffler != null)) zdShuffler = other.zdShuffler;
        if ((cardsPerGrowl == 1) && (other.cardsPerGrowl != 1)) cardsPerGrowl = other.cardsPerGrowl;
        if ((map == null) && (other.map != null)) map = other.map;
    }

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Soundtrack.Type getSoundtrackType() {
        return soundtrack;
    }
    public void setSoundtrackType(Soundtrack.Type st) {
        this.soundtrack = st;
    }

    /**
     * Will be null if this scenario doesn't use the zombie deck.
     */
    public ZombieDeckShuffler getZombieDeckShuffler() {
        return zdShuffler;
    }
    public void setZombieDeckShuffler(ZombieDeckShuffler shuffler) {
        this.zdShuffler = shuffler;
    }

    public int getCardsPerGrowl() {
        return cardsPerGrowl;
    }
    public void setCardsPerGrowl(int cardsPerGrowl) {
        this.cardsPerGrowl = cardsPerGrowl;
    }

    public Map getMap() {
        return map;
    }
    public void setMap(Map map) {
        this.map = map;
    }
}
