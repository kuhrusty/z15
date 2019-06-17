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

### Adding new missions

These are read at runtime from a text file; theoretically all you should
need to do is edit `app/src/main/assets/en/scenarios.json` and rebuild.
(Note that Android Studio's Instant Run does not seem to notice changes
under `app/src/main/assets`; you have to re-run the app using the green
arrow thingie.)

Lines starting with `//` are ignored, so you can have comments.  This is
parsed by `JSONScenarioRepository.JsonPrettifier`, so that's where to
look if you want to add features.

Here's an example entry: 

```json5
{
  //  This actually gets included in the scenario title, so you
  //  probably want it to be a number.
  "id": "4",
  
  //  This is displayed as the scenario title
  "name": "Let's Go To The Mall",
  
  //  This determines the title background:
  //  - "prologue" - blue
  //  - "survivor" -  green
  //  - "hero" - red
  "difficulty": "beginner",
  
  //  Options are "nogrowl", "growl60", and "growl40".
  "soundtrack": "growl60",
  
  //  How to prepare the Zombie deck, and what information to show on
  //  each card.  (If not present, then this scenario doesn't use the
  //  Zombie deck.)  Options are:
  //  - "scenario2" - Zombie card values 1-2, letters & events removed,
  //                  with 3 Horde cards in each half of the deck.
  //  - "scenario3" - Zombie card values 1-3, letters & events removed,
  //                  with 4 Horde cards in each half of the deck.
  //  - "standard" - All Zombie cards, letters & events removed, with 4
  //                 Horde cards in each half of the deck.
  //  - "standardLetters" - All Zombie cards, events removed, with 4
  //                        Horde cards in each half of the deck.
  //  - "standardEvents" - All Zombie cards, letters removed, with 4
  //                       Horde cards in each half of the deck.
  //  - "standardLettersEvents" - All Zombie cards, with 4 Horde cards
  //                              in each half of the deck.
  "zombieDeck": "standard",
  
  //  How many cards to draw per growl; this defaults to 1.
  "cardsPerGrowl": 1,
  
  //  The layout of the map is, one or more underscores is an empty
  //  tile; otherwise, we want a tile number & letter (the dash is
  //  optional, so "1-A" will be treated the same as "1A"), one or more
  //  spaces, and an orientation ("N", "E", "S", "W").  If one row has
  //  fewer tiles than another, empty columns at the end will be blank.
  "map": [
    " ____   18B E   14B S   22A S   24A S    7A S",
    "29B E   16B W   15B N   10B E   27A W    9A W   13A W",
    "17B N    6B W    5B E    4B W   12B S   21A N",
    "11A E    1B W    2B E    3B W   19B W"
  ]
}
```

### The launch icon

I know the big red & white "15" launch icon doesn't meet the Google
[design specifications](https://developer.android.com/google-play/resources/icon-design-specifications),
but I like it better than the smaller-red-15 over a busy background (an
image from the box cover).
