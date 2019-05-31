package com.kuhrusty.z15.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The hard-coded set of ZombieDeckShuffler implementations, because I am lazy.
 */
public abstract class Shufflers {
    private static Random rand = new Random();

    private static class BaseShuffler implements ZombieDeckShuffler {
        private final int maxZombies;
        private final int hordeCardsPerHalf;
        private final boolean letters;
        private final boolean events;
        private BaseShuffler(int maxZombies, int hordeCardsPerHalf,
                               boolean letters, boolean events) {
            this.maxZombies = maxZombies;
            this.hordeCardsPerHalf = hordeCardsPerHalf;
            this.letters = letters;
            this.events = events;
        }
        @Override
        public void shuffle(ZombieDeck deck) {
            List<ZombieCard> cards = extract(deck, maxZombies);
            clear(cards, !letters, !events);
            Collections.shuffle(cards, rand);
            List<ZombieCard> cards2 = new ArrayList<>(cards.size());
            splitRoughly(cards, cards2);
            for (int ii = 0; ii < hordeCardsPerHalf; ++ii) {
                addRandomHorde(cards);
                addRandomHorde(cards2);
            }
            cards.addAll(cards2);
            deck.setCards(cards);
        }
    }

    /**
     * Zombie card values 1-2, letters & events removed, with 3 Horde cards in
     * each half of the deck.
     */
    public static final ZombieDeckShuffler Scenario2 =
            new BaseShuffler(2, 3, false, false);

    /**
     * Zombie card values 1-3, letters & events removed, with 4 Horde cards in
     * each half of the deck.
     */
    public static final ZombieDeckShuffler Scenario3 =
            new BaseShuffler(3, 4, false, false);

    /**
     * All Zombie cards, letters & events removed, with 4 Horde cards in each
     * half of the deck.
     */
    public static final ZombieDeckShuffler Standard =
            new BaseShuffler(4, 4, false, false);

    /**
     * All Zombie cards, events removed, with 4 Horde cards in each half of the
     * deck.
     */
    public static final ZombieDeckShuffler StandardLetters =
            new BaseShuffler(4, 4, true, false);

    /**
     * All Zombie cards, letters removed, with 4 Horde cards in each half of the
     * deck.
     */
    public static final ZombieDeckShuffler StandardEvents =
            new BaseShuffler(4, 4, false, true);

    /**
     * All Zombie cards, with 4 Horde cards in each half of the deck.
     */
    public static final ZombieDeckShuffler StandardLettersEvents =
            new BaseShuffler(4, 4, true, true);

    /**
     * Sets the Random which will be used; for repeatability in unit tests.
     *
     * @param rand must not be null.
     */
    public static void setRandom(Random rand) {
        Shufflers.rand = rand;
    }

    private static List<ZombieCard> extract(ZombieDeck deck, int maxZombies) {
        List<ZombieCard> rv = new ArrayList<>(deck.cardsRemaining());
        ZombieCard tc = null;
        while ((tc = deck.drawCard()) != null) {
            if ((!tc.isHorde()) && (tc.getZombies() <= maxZombies)) {
                rv.add(tc);
            }
        }
        return rv;
    }

    private static void addRandomHorde(List<ZombieCard> cards) {
        cards.add(rand.nextInt(cards.size() + 1), new ZombieCard());
    }

    private static void splitRoughly(List<ZombieCard> src, List<ZombieCard> otherHalf) {
        int split = (src.size() / 2) + rand.nextInt(6) - 3;
        for (int ii = split; ii < src.size(); ++ii) otherHalf.add(src.get(ii));
        //  no setSize() method?
        for (int ii = src.size() - 1; ii >= split; --ii) src.remove(ii);
    }

    private static void clear(List<ZombieCard> cards, boolean letters, boolean events) {
        if (!(letters || events)) return;
        for (ZombieCard tc : cards) {
            if (letters) tc.clearLetter();
            if (events) tc.clearEvent();
        }
    }
}
