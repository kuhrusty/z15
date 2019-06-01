package com.kuhrusty.z15.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * One card in the Zombie Cards deck.
 */
public class ZombieCard implements Parcelable {
    public enum Event {
        LoseItem,
        SneakAttack,
        Surge,
        Terror
    }
    private int zombies;
    private String letter;
    private Event event;

    /**
     * @param zombies
     * @param letter may be null.
     */
    public ZombieCard(int zombies, String letter) {
        this.zombies = zombies;
        this.letter = letter;
    }
    /**
     * @param zombies
     * @param letter may be null.
     * @param event may be null.
     */
    public ZombieCard(int zombies, String letter, Event event) {
        this.zombies = zombies;
        this.letter = letter;
        this.event = event;
    }
    /**
     * Creates a Horde card.
     */
    public ZombieCard() {
    }

    /**
     * Returns true if this is a Horde card, false if getZombies() will return
     * the number of zombies to add.
     */
    public boolean isHorde() {
        return zombies == 0;
    }

    /**
     * Returns the number of zombies added by this card, or 0 if this is a
     * Horde card.
     */
    public int getZombies() {
        return zombies;
    }

    /**
     * Returns "A", "B", etc., or null.
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Returns the associated event, or null.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Removes the letter, for scenarios which don't use them.
     */
    public void clearLetter() {
        letter = null;
    }
    /**
     * Removes the event, if any, for scenarios which don't use them.
     */
    public void clearEvent() {
        event = null;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(zombies);
        parcel.writeInt(letter != null ? letter.charAt(0) : 0);
        parcel.writeInt(event != null ? event.ordinal() : -1);
    }
    public static final Creator<ZombieCard> CREATOR = new Creator<ZombieCard>() {
        public ZombieCard createFromParcel(Parcel in) {
            int zombies = in.readInt();
            String letter = null;
            switch (in.readInt()) {
                case 'A': letter = "A"; break;
                case 'B': letter = "B"; break;
                case 'C': letter = "C"; break;
                case 'D': letter = "D"; break;
            }
            int ti = in.readInt();
            Event event = null;
            if ((ti >= 0) && (ti < Event.values().length)) {
                event = Event.values()[ti];
            }
            return new ZombieCard(zombies, letter, event);
        }
        public ZombieCard[] newArray(int size) {
            return new ZombieCard[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
}
