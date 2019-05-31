package com.kuhrusty.z15.model;

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
}
