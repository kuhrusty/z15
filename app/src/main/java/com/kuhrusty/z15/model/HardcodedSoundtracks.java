package com.kuhrusty.z15.model;

import com.kuhrusty.z15.R;

/**
 * Just a lame placeholder until/unless you add support for reading soundtracks
 * at runtime.
 */
public class HardcodedSoundtracks implements SoundtrackRepository {
    @Override
    public Soundtrack getSoundtrack(Soundtrack.Type type) {
        if (type.equals(Soundtrack.Type.Growl60)) return ST60;
        if (type.equals(Soundtrack.Type.Growl40)) return ST40;
        if (type.equals(Soundtrack.Type.NoGrowl)) return STNone;
        return null;
    }

    private static final Soundtrack STNone = new Soundtrack(Soundtrack.Type.NoGrowl,
            //  technically endMS should be something like 14 * 60000 + 58000,
            //  but it looks weird when the timer starts at 14:58... so call it
            //  an even 15.
            R.raw.zombie_15_free, null, 15 * 60000);

    private static final Soundtrack ST60 = new Soundtrack(Soundtrack.Type.Growl60,
            R.raw.zombie_15_60sec, new int[] {
                    //  well, it turns out that these guys *are* on 60-second
                    //  boundaries.
                    60000,
                    2 * 60000,
                    3 * 60000,
                    4 * 60000,
                    5 * 60000,
                    6 * 60000,
                    7 * 60000,
                    8 * 60000,
                    9 * 60000,
                    10 * 60000,
                    11 * 60000,
                    12 * 60000,
                    13 * 60000,
                    14 * 60000 }, 15 * 60000);

    private static final Soundtrack ST40 = new Soundtrack(Soundtrack.Type.Growl40,
            R.raw.zombie_15_40sec, new int[] {
                    //  And, same deal here--these are on 40-second boundaries.
                    40000,
                    2 * 40000,
                    3 * 40000,
                    4 * 40000,
                    5 * 40000,
                    6 * 40000,
                    7 * 40000,
                    8 * 40000,
                    9 * 40000,
                    10 * 40000,
                    11 * 40000,
                    12 * 40000,
                    13 * 40000,
                    14 * 40000,
                    15 * 40000,
                    16 * 40000,
                    17 * 40000,
                    18 * 40000,
                    19 * 40000,
                    20 * 40000,
                    21 * 40000}, 15 * 60000);
}
