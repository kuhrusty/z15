package com.kuhrusty.z15.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a scenario's Zombie Deck.
 */
public class ZombieDeck {
    /**
     * Creates a fresh-from-shrink, unshuffled deck.  To prepare it for a
     * scenario, pass it to the appropriate ZombieDeckShuffler.
     */
    public ZombieDeck() {
        initStandardDeck();
    }

    /**
     * Removes & returns the next card on the deck, or null.
     */
    public ZombieCard drawCard() {
        if (pos < cards.size()) {
            return cards.get(pos++);
        }
        return null;
    }

    /**
     * Returns the number of cards left in the deck.
     */
    public int cardsRemaining() {
        return cards.size() - pos;
    }

    /**
     * Replaces the contents of the deck with the given cards in the given
     * order.  Probably doesn't make a deep copy, so, uhh, please don't mess
     * with the list or the cards afterwards.
     *
     * @param cards must not be null.
     */
    public void setCards(List<ZombieCard> cards) {
        this.cards.clear();
        this.cards.addAll(cards);
        pos = 0;
    }

    /**
     * Dumps the contents of the deck to a StringBuilder for diagnostic purposes.
     */
    public StringBuilder dump() {
        StringBuilder buf = new StringBuilder();
        buf.append(cards.size()).append(" cards, pos ").append(pos).append("\n");
        for (int ii = pos; ii < cards.size(); ++ii) {
            ZombieCard tc = cards.get(ii);
            if (tc.isHorde()) {
                buf.append("HORDE\n");
            } else {
                buf.append(tc.getZombies());
                if (tc.getLetter() != null) buf.append(" ").append(tc.getLetter());
                if (tc.getEvent() != null) buf.append(" " ).append(tc.getEvent());
                buf.append("\n");
            }
        }
        return buf;
    }

    private void initStandardDeck() {
        int zc = 1;
        cards.add(new ZombieCard(zc, "A"));
        cards.add(new ZombieCard(zc, "A"));
        cards.add(new ZombieCard(zc, "A", ZombieCard.Event.Surge));
        cards.add(new ZombieCard(zc, "B"));
        cards.add(new ZombieCard(zc, "B"));
        cards.add(new ZombieCard(zc, "B", ZombieCard.Event.SneakAttack));
        cards.add(new ZombieCard(zc, "C"));
        cards.add(new ZombieCard(zc, "C", ZombieCard.Event.Terror));
        cards.add(new ZombieCard(zc, "D"));
        cards.add(new ZombieCard(zc, "D", ZombieCard.Event.LoseItem));

        zc = 2;
        cards.add(new ZombieCard(zc, "A"));
        cards.add(new ZombieCard(zc, "A", ZombieCard.Event.LoseItem));
        cards.add(new ZombieCard(zc, "B"));
        cards.add(new ZombieCard(zc, "B", ZombieCard.Event.Surge));
        cards.add(new ZombieCard(zc, "C"));
        cards.add(new ZombieCard(zc, "C"));
        cards.add(new ZombieCard(zc, "C", ZombieCard.Event.SneakAttack));
        cards.add(new ZombieCard(zc, "D"));
        cards.add(new ZombieCard(zc, "D"));
        cards.add(new ZombieCard(zc, "D", ZombieCard.Event.Terror));

        zc = 3;
        cards.add(new ZombieCard(zc, "A"));
        cards.add(new ZombieCard(zc, "A", ZombieCard.Event.Terror));
        cards.add(new ZombieCard(zc, "B"));
        cards.add(new ZombieCard(zc, "B"));
        cards.add(new ZombieCard(zc, "B", ZombieCard.Event.LoseItem));
        cards.add(new ZombieCard(zc, "C"));
        cards.add(new ZombieCard(zc, "C"));
        cards.add(new ZombieCard(zc, "C", ZombieCard.Event.Surge));
        cards.add(new ZombieCard(zc, "D"));
        cards.add(new ZombieCard(zc, "D", ZombieCard.Event.SneakAttack));

        zc = 4;
        cards.add(new ZombieCard(zc, "A"));
        cards.add(new ZombieCard(zc, "A"));
        cards.add(new ZombieCard(zc, "A", ZombieCard.Event.SneakAttack));
        cards.add(new ZombieCard(zc, "B"));
        cards.add(new ZombieCard(zc, "B", ZombieCard.Event.Terror));
        cards.add(new ZombieCard(zc, "C"));
        cards.add(new ZombieCard(zc, "C", ZombieCard.Event.LoseItem));
        cards.add(new ZombieCard(zc, "D"));
        cards.add(new ZombieCard(zc, "D"));
        cards.add(new ZombieCard(zc, "D", ZombieCard.Event.Surge));

        //  Horde cards
        for (int ii = 0; ii < 8; ++ii) cards.add(new ZombieCard());
    }

    private List<ZombieCard> cards = new ArrayList<>(48);
    private int pos = 0;  //  position of the next card to be drawn
}
