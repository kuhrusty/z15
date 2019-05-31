package com.kuhrusty.z15.model;

/**
 * Prepares a scenario's Zombie Deck.
 */
public interface ZombieDeckShuffler {
    /**
     * Prepares the deck for a scenario.  This removes unused cards, possibly
     * removes location letters & events, and shuffles the result as described
     * in the scenario setup.
     *
     * @param deck a complete, un-messed with deck of 48 cards.  After this
     *             returns, it will have been messed with.
     */
    void shuffle(ZombieDeck deck);
}
