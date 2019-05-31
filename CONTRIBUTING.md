Other than cosmetic improvements, the main thing this needs is
improved scenario setup diagrams.  (That's
[issue #1](https://github.com/kuhrusty/z15/issues/1) on GitHub.)

See also the project's list of open issues on GitHub:
https://github.com/kuhrusty/z15/issues

### To check out the code

In Android Studio 3.4.1:

1. File -> New -> Project from Version Control -> Git
1. Clone Repository:
   - URL: https://github.com/kuhrusty/z15.git
   - Directory: (whatever you want; probably
     `/home/.../AndroidStudioProjects/z15` or `Zombie15`)

   That should check the stuff out and start a Gradle build.  (If you
   get errors about missing .iml files, ignore them--do *not* remove the
   modules it's talking about.)
1. Build -> Clean Project; then you should be able to build & install on
   your device using the green triangle-thing in the toolbar.

### Adding new soundtracks

I know other soundtracks *exist* (there are a couple on IELLO's site),
but I haven't tried using them or the instructions in this section.

Note that, originally, I was using the MP3s from IELLO's download page,
but I was having some weird problems related to seeking within the
files.  Once I switched to Ogg Vorbis (ripped from the CD which came
with the game), those problems went away, so be careful about using the
alternate MP3s.

To add a new soundtrack, I *think* this is what you have to do:

1. Add the file under `app/src/main/res/raw`.
1. Add a new `Soundtrack` instance in `HardcodedSoundtracks` which uses
   your new resource ID, and which has the times of the growls in your
   new soundtrack.
1. Also in `HardcodedSoundtracks`, have `getSoundtrack()` return your
   new `Soundtrack` instance.  If you want to be able to switch between
   soundtracks at runtime, you might add a preference to
   `app/src/main/res/xml/preferences.xml`, and check for it in
   `HardcodedSoundtracks.getSoundtrack()`.  For an example of this, see
   how `PreferenceManager.getDefaultSharedPreferences()` is used in
   `ScenarioActivity` to decide whether to show various UI components.

### The launch icon

I know the big red & white "15" launch icon doesn't meet the Google
[design specifications](https://developer.android.com/google-play/resources/icon-design-specifications),
but I like it better than the smaller-red-15 over a busy background (an
image from the box cover).
