package com.kuhrusty.z15.model;

public interface SoundtrackRepository {
    /**
     * Returns "a" soundtrack of the given type.  Currently there's only one of
     * each, so that's pretty easy.
     */
    Soundtrack getSoundtrack(Soundtrack.Type type);
}
