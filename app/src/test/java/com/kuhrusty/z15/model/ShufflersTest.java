package com.kuhrusty.z15.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ShufflersTest {
    @Before
    public void initRandom() {
        Shufflers.setRandom(new Random(666));
    }

    @Test
    public void testScenario2() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.Scenario2.shuffle(deck);
        //System.err.println("After shuffle:\n" + deck.dump());
        assertEquals(26, deck.cardsRemaining());
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard());
    }

    @Test
    public void testScenario3() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.Scenario3.shuffle(deck);
        //System.err.println("After shuffle:\n" + deck.dump());
        assertEquals(38, deck.cardsRemaining());
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(3, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(3, null));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard());
    }

    @Test
    public void testStandard() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.Standard.shuffle(deck);
        //System.err.println("After shuffle:\n" + deck.dump());
        assertEquals(48, deck.cardsRemaining());
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(4, null));
        check(deck.drawCard(), new ZombieCard(3, null));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(3, null));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(3, null));
        check(deck.drawCard(), new ZombieCard());
    }

    @Test
    public void testStandardLetters() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.StandardLetters.shuffle(deck);
        //System.err.println("After shuffle:\n" + deck.dump());
        assertEquals(48, deck.cardsRemaining());
        check(deck.drawCard(), new ZombieCard(1, "A"));
        check(deck.drawCard(), new ZombieCard(2, "D"));
        check(deck.drawCard(), new ZombieCard(4, "C"));
        check(deck.drawCard(), new ZombieCard(3, "C"));
        check(deck.drawCard(), new ZombieCard(2, "D"));
        check(deck.drawCard(), new ZombieCard(2, "C"));
        check(deck.drawCard(), new ZombieCard(3, "B"));
        check(deck.drawCard(), new ZombieCard(2, "A"));
        check(deck.drawCard(), new ZombieCard(1, "C"));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(1, "A"));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(3, "C"));
        check(deck.drawCard(), new ZombieCard());
    }

    @Test
    public void testStandardEvents() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.StandardEvents.shuffle(deck);
        //System.err.println("After shuffle:\n" + deck.dump());
        assertEquals(48, deck.cardsRemaining());
        check(deck.drawCard(), new ZombieCard(1, null, ZombieCard.Event.Surge));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(4, null, ZombieCard.Event.LoseItem));
        check(deck.drawCard(), new ZombieCard(3, null));
        check(deck.drawCard(), new ZombieCard(2, null, ZombieCard.Event.Terror));
        check(deck.drawCard(), new ZombieCard(2, null));
        check(deck.drawCard(), new ZombieCard(3, null, ZombieCard.Event.LoseItem));
        check(deck.drawCard(), new ZombieCard(2, null, ZombieCard.Event.LoseItem));
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(1, null));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(3, null));
        check(deck.drawCard(), new ZombieCard());
    }

    @Test
    public void testStandardLettersEvents() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.StandardLettersEvents.shuffle(deck);
        //System.err.println("After shuffle:\n" + deck.dump());
        assertEquals(48, deck.cardsRemaining());
        check(deck.drawCard(), new ZombieCard(1, "A", ZombieCard.Event.Surge));
        check(deck.drawCard(), new ZombieCard(2, "D"));
        check(deck.drawCard(), new ZombieCard(4, "C", ZombieCard.Event.LoseItem));
        check(deck.drawCard(), new ZombieCard(3, "C"));
        check(deck.drawCard(), new ZombieCard(2, "D", ZombieCard.Event.Terror));
        check(deck.drawCard(), new ZombieCard(2, "C"));
        check(deck.drawCard(), new ZombieCard(3, "B", ZombieCard.Event.LoseItem));
        check(deck.drawCard(), new ZombieCard(2, "A", ZombieCard.Event.LoseItem));
        check(deck.drawCard(), new ZombieCard(1, "C"));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(1, "A"));
        check(deck.drawCard(), new ZombieCard());
        check(deck.drawCard(), new ZombieCard(3, "C"));
        check(deck.drawCard(), new ZombieCard());
    }

    public static void check(ZombieCard got, ZombieCard expect) {
        assertEquals(expect.getZombies(), got.getZombies());
        assertEquals(expect.getLetter(), got.getLetter());
        assertEquals(expect.getEvent(), got.getEvent());
    }
}
