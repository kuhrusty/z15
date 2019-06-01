package com.kuhrusty.z15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kuhrusty.z15.model.Scenario;
import com.kuhrusty.z15.model.RepositoryFactory;
import com.kuhrusty.z15.model.Soundtrack;
import com.kuhrusty.z15.model.ZombieCard;
import com.kuhrusty.z15.model.ZombieDeck;

import java.util.Locale;

import static android.R.drawable.ic_media_pause;
import static android.R.drawable.ic_media_play;

public class ScenarioActivity extends AppCompatActivity
        implements MediaPlayer.OnCompletionListener,
                   MediaPlayer.OnSeekCompleteListener {
    private static final String LOGBIT = "ScenarioActivity";

    public static final String INTENT_SCENARIO_ID = "scenarioID";

    private static final String BUNDLE_SCENARIO_ID = "scenarioID";
    private static final String BUNDLE_AUDIO_POSITION = "audioPositionMS";

    private ImageButton playBtn;
    private TextView timeRemaining;  //  null if we're not displaying it
    private TextView nextGrowl;  //  null if we're not displaying it
    private TextView diagnostics;  //  null if we're not displaying them
    private String timeFormatString;
    private MediaPlayer mediaPlayer;
    private boolean playingAudio = false;
    private Thread timeUpdateThread = null;

    private final Runnable uiUpdaterWrapper = new Runnable() {
        @Override
        public void run() {
            Log.d(LOGBIT, "uiUpdaterWrapper running on " + Thread.currentThread().getName());
            while (playingAudio) {
                Log.d(LOGBIT, Thread.currentThread().getName() + " running uiUpdater");
                runOnUiThread(uiUpdater);
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    //  don't really care
                }
            }
            Log.d(LOGBIT, "!playingAudio, " + Thread.currentThread().getName() + " exiting");
        }
    };
    private final Runnable uiUpdater = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                audioPositionMS = mediaPlayer.getCurrentPosition();
                if (timeRemaining != null) {
                    timeRemaining.setText(formatTime(scenarioEndMS - audioPositionMS));
                }
                if (nextGrowl != null) {
                    nextGrowl.setText(formatTime(nextGrowlMS - audioPositionMS));
                }
                if (diagnostics != null) {
                    diagnostics.setText(getDiagnostics());
                }
                if ((nextGrowlMS != -1) && (audioPositionMS > nextGrowlMS)) {
                    doGrowl();
                    nextGrowlMS = soundtrack.getNextGrowl(audioPositionMS);
                } else if ((scenarioEndMS != -1) && (audioPositionMS > scenarioEndMS)) {
Toast.makeText(ScenarioActivity.this, "need to display scenario over graphic!", Toast.LENGTH_SHORT).show();
                    scenarioEndMS = -1;  //  so we know we've displayed it
                }
            }
        }
    };

    private int audioResID = 0;
    private int audioPositionMS = 0;
    private int audioDurationMS = 0;  //  not the same as scenario end!
    private int nextGrowlMS = -1;
    private int scenarioEndMS = -1;
    private Scenario scenario;
    private Soundtrack soundtrack;
    private ZombieDeck zombieDeck;  //  null for scenarios with no growls

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        timeFormatString = getString(R.string.time_format);

        //  When people hit the volume buttons, we want to change the media
        //  volume, not the ringtone volume.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        String scenarioID = null;
        if (savedInstanceState != null) {
            scenarioID = savedInstanceState.getString(BUNDLE_SCENARIO_ID);
            audioPositionMS = savedInstanceState.getInt(BUNDLE_AUDIO_POSITION, audioPositionMS);
            Log.d(LOGBIT, "onCreate() savedInstanceState, scenarioID " +
                    scenarioID + ", audioPositionMS " + audioPositionMS);
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                scenarioID = intent.getStringExtra(INTENT_SCENARIO_ID);
            }
            Log.d(LOGBIT, "onCreate() intent, scenarioID " + scenarioID +
                    ", audioPositionMS " + audioPositionMS);
        }

        if (scenarioID != null) {
            scenario = RepositoryFactory.getScenarioRepository(this).getScenario(scenarioID);
            if (scenario == null) {
Toast.makeText(this, "gackk, life is bad, no scenario " + scenarioID, Toast.LENGTH_LONG).show();
            } else {
                soundtrack = RepositoryFactory.getSoundtrackRepository(this).getSoundtrack(scenario.getSoundtrackType());
                if (soundtrack == null) {
Toast.makeText(this, "gackk, life is bad, no soundtrack for scenario " + scenario.getID(), Toast.LENGTH_LONG).show();
                }
                audioResID = soundtrack.getAudioResID();
                nextGrowlMS = soundtrack.getNextGrowl(audioPositionMS);
                scenarioEndMS = soundtrack.getEndMS();

                if (scenario.getZombieDeckShuffler() != null) {
                    zombieDeck = new ZombieDeck();
                    scenario.getZombieDeckShuffler().shuffle(zombieDeck);
                }

                TextView tv = findViewById(R.id.scenarioName);
                //  copied from ScenarioListAdapter.onBindViewHolder(); you
                //  should probably put this in one place.
                tv.setText(String.format(Locale.getDefault(),
                        getString(R.string.scenario_format), scenario.getID(), scenario.getName()));
                if (scenario.getDifficulty().equals(Scenario.Difficulty.Hero)) {
                    tv.setBackground(getResources().getDrawable(R.drawable.red_gradient));
                } else if (scenario.getDifficulty().equals(Scenario.Difficulty.Survivor)) {
                    tv.setBackground(getResources().getDrawable(R.drawable.green_gradient));
                } else {
                    tv.setBackground(getResources().getDrawable(R.drawable.blue_gradient));
                }
            }
        }

        playBtn = findViewById(R.id.playButton);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp != null) {
            if (sp.getBoolean(SettingsActivity.PREF_SHOW_TIMER, false)) {
                timeRemaining = findViewById(R.id.timeRemaining);
                if (timeRemaining != null) timeRemaining.setVisibility(View.VISIBLE);
            }
            if (sp.getBoolean(SettingsActivity.PREF_SHOW_GROWL_TIMER, false)) {
                nextGrowl = findViewById(R.id.nextGrowl);
                if (nextGrowl != null) nextGrowl.setVisibility(View.VISIBLE);
            }
            if (sp.getBoolean(SettingsActivity.PREF_SHOW_FF_REW, false)) {
                View tv = findViewById(R.id.ffControls);
                if (tv != null) tv.setVisibility(View.VISIBLE);
                diagnostics = findViewById(R.id.diagnostics);
                if (diagnostics != null) diagnostics.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer = MediaPlayer.create(this, audioResID);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        audioDurationMS = mediaPlayer.getDuration();
        if (timeRemaining != null) {
            timeRemaining.setText(formatTime(scenarioEndMS - audioPositionMS));
        }
        if (nextGrowl != null) {
            nextGrowl.setText(formatTime(nextGrowlMS - audioPositionMS));
        }
        if (diagnostics != null) {
            diagnostics.setText(getDiagnostics());
        }
    }

    @Override
    protected void onStop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (scenario != null) savedInstanceState.putString(BUNDLE_SCENARIO_ID, scenario.getID());
        savedInstanceState.putInt(BUNDLE_AUDIO_POSITION, audioPositionMS);
    }

    @Override
    public void onPause() {
        pauseAudio();
        super.onPause();
    }

    public void onPlayClicked(View view) {
        if (playingAudio) pauseAudio();
        else playAudio();
    }

    public void onFF10Clicked(View view) {
        seek(10000);
    }
    public void onFF60Clicked(View view) {
        seek(60000);
    }
    public void onRW10Clicked(View view) {
        seek(-10000);
    }
    public void onRW60Clicked(View view) {
        seek(-60000);
    }

    /**
     * Adjusts our position by the given amount, positive or negative.  Only
     * works while we're actually playingAudio.
     */
    private void seek(int ms) {
        //  On a seek forward, the extra second is just to cover any delay in
        //  the seek which might make us seek past the end of the track; math
        //  is hard.
        if ((playingAudio) &&
                (((ms > 0) && ((audioPositionMS + ms + 1000) < audioDurationMS)) ||
                 ((ms < 0) && (audioDurationMS > (-ms))))) {
            //  let the timeUpdateThread update the display
            mediaPlayer.seekTo(audioPositionMS + ms);
        }
    }

    /**
     * Stops the soundtrack, remembers our position, updates the play button
     * icon.
     */
    private void pauseAudio() {
        if (!playingAudio) return;
        playingAudio = false;  //  timeUpdaterThread will see this & exit
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioPositionMS = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        timeUpdateThread = null;
        playBtn.setImageResource(ic_media_play);
        Log.d(LOGBIT, "paused at " + audioPositionMS);

        View container = findViewById(R.id.zombiecards);
        container.animate().cancel();
    }

    /**
     * Starts the soundtrack, updates the play button icon.
     */
    private void playAudio() {
        if (playingAudio) return;
        playingAudio = true;
        //  We want the device to stay on while we're running.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playBtn.setImageResource(ic_media_pause);
        Log.d(LOGBIT, "seeking to " + audioPositionMS);
        mediaPlayer.seekTo(audioPositionMS);
        //  In our onSeekComplete() is where we start playing the sound &
        //  updating our timer display.

        //  Resume animation on zombie card container
        View container = findViewById(R.id.zombiecards);
        if (container.getAlpha() > 0f) {
            container.animate()
                    .alpha(0f)
                    .setDuration((int)(30000 * container.getAlpha()))
                    .setListener(null);
        }
    }

    /**
     * Called when our MediaPlayer finishes playing its current sound.
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(LOGBIT, "onCompletion hit!!");
        playingAudio = false;
        audioPositionMS = 0;
        //  probably want to do other stuff...
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Log.d(LOGBIT, "seek complete, playing");
        if (timeUpdateThread == null) {
            timeUpdateThread = new Thread(uiUpdaterWrapper);
            timeUpdateThread.start();
        }
//not sure this is right if we got here because of a ff or rw button
        mediaPlayer.start();
        nextGrowlMS = soundtrack.getNextGrowl(mediaPlayer.getCurrentPosition());
        scenarioEndMS = soundtrack.getEndMS();
    }

    private void doGrowl() {
        ZombieCard card = zombieDeck.drawCard();
        if (card != null) {
            setNumber(R.id.zombiecard1_number, card.getZombies());
            setOther(R.id.zombiecard1_other, R.id.zombiecard1_letter,
                    card.getLetter(), R.id.zombiecard1_event, card.getEvent());
        }
        if (scenario.getCardsPerGrowl() == 2) {
            card = zombieDeck.drawCard();
            if (card != null) {
                setNumber(R.id.zombiecard2_number, card.getZombies());
                setOther(R.id.zombiecard2_other, R.id.zombiecard2_letter,
                        card.getLetter(), R.id.zombiecard2_event, card.getEvent());
            }
        }
        View container = findViewById(R.id.zombiecards);
        container.setVisibility(View.VISIBLE);
        container.setAlpha(1f);
        container.animate()
                .alpha(0f)
                .setDuration(30000)
                .setListener(null);
    }

    private void setNumber(int viewID, int number) {
        ImageView iv = findViewById(viewID);
        int resID = 0;
        switch (number) {
            case 0: resID = R.drawable.zombie_horde; break;
            case 1: resID = R.drawable.zombie_1; break;
            case 2: resID = R.drawable.zombie_2; break;
            case 3: resID = R.drawable.zombie_3; break;
            case 4: resID = R.drawable.zombie_4; break;
        }
        if (resID != 0) {
            iv.setImageResource(resID);
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }
    }
    private void setOther(int containerViewID, int letterViewID, String letter,
                          int eventViewID, ZombieCard.Event event) {
        int lvResID = 0;
        int evResID = 0;
        if (letter != null) {
            switch (letter) {
                case "A": lvResID = R.drawable.zombie_a; break;
                case "B": lvResID = R.drawable.zombie_b; break;
                case "C": lvResID = R.drawable.zombie_c; break;
                case "D": lvResID = R.drawable.zombie_d; break;
            }
        }
        if (event != null) {
            switch (event) {
                case LoseItem: evResID = R.drawable.lose_item; break;
                case SneakAttack: evResID = R.drawable.sneak_attack; break;
                case Surge: evResID = R.drawable.surge; break;
                case Terror: evResID = R.drawable.terror; break;
            }
        }

        ImageView iv = findViewById(letterViewID);
        if (lvResID != 0) {
            iv.setImageResource(lvResID);
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }
        iv = findViewById(eventViewID);
        if (evResID != 0) {
            iv.setImageResource(evResID);
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }
        View cv = findViewById(containerViewID);
        cv.setVisibility(((lvResID != 0) || (evResID != 0)) ? View.VISIBLE : View.GONE);
    }

    /**
     * Takes a number of ms, returns m:ss or something.  Probably should use a
     * localized format string.
     */
    private String formatTime(int ms) {
        if (ms <= 0) return getString(R.string.no_time);
        return String.format(timeFormatString, (ms / 60000), (ms / 1000) % 60);
    }

    /**
     * Generates the diagnostics string which we might be displaying.
     */
    private String getDiagnostics() {
        int dur = (mediaPlayer != null) ? mediaPlayer.getDuration() : -1;
        StringBuilder rv = new StringBuilder("pos ");
        fmtDiagTime(rv, audioPositionMS);
        rv.append(", growl ");
        fmtDiagTime(rv, nextGrowlMS);
        rv.append(", end ");
        fmtDiagTime(rv, scenarioEndMS);
        rv.append(", dur ");
        fmtDiagTime(rv, dur);
        return rv.toString();
    }
    private void fmtDiagTime(StringBuilder buf, int ms) {
        buf.append(ms);
        if (ms > 0) buf.append(" (").append(formatTime(ms)).append(")");
    }
}
