package com.kuhrusty.z15.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ZombieDeckParcelTest {
    @Before
    public void initRandom() {
        Shufflers.setRandom(new Random(666));
    }

    static <PC extends Parcelable> PC parcel(PC pc, Parcelable.Creator<PC> creator) {
        Parcel tp = Parcel.obtain();
        pc.writeToParcel(tp, 0);
        tp.setDataPosition(0);
        return creator.createFromParcel(tp);
    }

    @Test
    public void testParcel1() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.Scenario3.shuffle(deck);
        checkScenario3(deck);
        deck.undrawAll();
        deck = parcel(deck, ZombieDeck.CREATOR);
        checkScenario3(deck);
    }

    @Test
    public void testParcel2() {
        ZombieDeck deck = new ZombieDeck();
        assertEquals(48, deck.cardsRemaining());
        //System.err.println("Before shuffle:\n" + deck.dump());
        Shufflers.StandardLettersEvents.shuffle(deck);
        //System.err.println("After shuffle:\n" + deck.dump());
        checkStandardLettersEvents(deck);
        deck.undrawAll();
        deck = parcel(deck, ZombieDeck.CREATOR);
        checkStandardLettersEvents(deck);
    }

    /**
     * This checks at least some of the contents of a ZombieDeck which has been
     * passed to Shufflers.Scenario3.shuffle() with
     * Shufflers.setRandom(new Random(666)).
     *
     * <p>Copied from ShufflersTest, as I didn't see an easy way to make that
     * visible in androidTest.
     */
    public void checkScenario3(ZombieDeck deck) {
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
    /**
     * This checks at least some of the contents of a ZombieDeck which has been
     * passed to Shufflers.StandardLettersEvents.shuffle() with
     * Shufflers.setRandom(new Random(666)).
     *
     * <p>Copied from ShufflersTest, as I didn't see an easy way to make that
     * visible in androidTest.
     */
    public void checkStandardLettersEvents(ZombieDeck deck) {
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
    /**
     * Copied from ShufflersTest, as I didn't see an easy way to make that
     * visible in androidTest.
     */
    public static void check(ZombieCard got, ZombieCard expect) {
        assertEquals(expect.getZombies(), got.getZombies());
        assertEquals(expect.getLetter(), got.getLetter());
        assertEquals(expect.getEvent(), got.getEvent());
    }
}
