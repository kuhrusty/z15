package com.kuhrusty.z15.model;

import java.util.Arrays;

/**
 * Information about a soundtrack: its resource ID, the length of the scenario
 * (not necessarily the same as the length of the track), the times at which
 * growls occur, etc.
 */
public class Soundtrack {
    public enum Type {
        NoGrowl, Growl60, Growl40
    }

    //  my thinking here was that we could have multiple 60-second-growl
    //  soundtracks, and people could choose among them when starting the
    //  scenario or in preferences, something like that.  So, all soundtracks
    //  which are 60-second-growls would have type == Growl60.
    private final Type type;
    private final int audioResID;
    private final int[] growlsMS;
    private final int endMS;

    /**
     *
     * @param type
     * @param audioResID
     * @param growlsMS the list of growl timestamps, in increasing order, in ms
     *                 from the start of the audio track.
     * @param endMS
     */
    public Soundtrack(Type type, int audioResID, int[] growlsMS, int endMS) {
        this.type = type;
        this.audioResID = audioResID;
        this.growlsMS = growlsMS;
        this.endMS = endMS;
    }

    public Type getType() {
        return type;
    }
    public int getAudioResID() {
        return audioResID;
    }

    /**
     * The end of the scenario, in ms from the start of the audio file.  This is
     * not the same as the end of the audio file, as the audio may continue for
     * several seconds after the end of the scenario.
     */
    public int getEndMS() {
        return endMS;
    }

    /**
     * Returns the next growl (in ms from the start of the audio file) after the
     * given timestamp, or -1 if there isn't one.
     */
    public int getNextGrowl(int afterTimeMS) {
        if (growlsMS == null) return -1;
        int pos = Arrays.binarySearch(growlsMS, afterTimeMS + 1);
        if (pos >= 0) return growlsMS[pos];
        pos = -pos - 1;
        if (pos >= growlsMS.length) return -1;
        return growlsMS[pos];
    }
}
