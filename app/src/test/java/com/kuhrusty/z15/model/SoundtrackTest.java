package com.kuhrusty.z15.model;

import com.kuhrusty.z15.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SoundtrackTest {
    @Test
    public void testSoundtracks() {
        SoundtrackRepository sr = RepositoryFactory.getSoundtrackRepository(null);
        assertNotNull(sr);
        Soundtrack st = sr.getSoundtrack(Soundtrack.Type.NoGrowl);
        assertNotNull(st);
        assertEquals(Soundtrack.Type.NoGrowl, st.getType());
        assertEquals(R.raw.zombie_15_free, st.getAudioResID());
        assertEquals(900000, st.getEndMS());
        assertEquals(-1, st.getNextGrowl(0));
        assertEquals(-1, st.getNextGrowl(1000));
        assertEquals(-1, st.getNextGrowl(65000));

        st = sr.getSoundtrack(Soundtrack.Type.Growl60);
        assertNotNull(st);
        assertEquals(Soundtrack.Type.Growl60, st.getType());
        assertEquals(R.raw.zombie_15_60sec, st.getAudioResID());
        assertEquals(900000, st.getEndMS());
        assertEquals(60000, st.getNextGrowl(0));
        assertEquals(60000, st.getNextGrowl(1000));
        assertEquals(120000, st.getNextGrowl(60000));
        assertEquals(120000, st.getNextGrowl(65000));
        assertEquals(840000, st.getNextGrowl(839000));
        assertEquals(-1, st.getNextGrowl(840000));

        st = sr.getSoundtrack(Soundtrack.Type.Growl40);
        assertNotNull(st);
        assertEquals(Soundtrack.Type.Growl40, st.getType());
        assertEquals(R.raw.zombie_15_40sec, st.getAudioResID());
        assertEquals(900000, st.getEndMS());
        assertEquals(40000, st.getNextGrowl(0));
        assertEquals(40000, st.getNextGrowl(1000));
        assertEquals(80000, st.getNextGrowl(60000));
        assertEquals(80000, st.getNextGrowl(65000));
        assertEquals(840000, st.getNextGrowl(839000));
        assertEquals(-1, st.getNextGrowl(840000));
    }
}
