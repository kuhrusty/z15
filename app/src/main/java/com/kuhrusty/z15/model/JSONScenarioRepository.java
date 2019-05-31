package com.kuhrusty.z15.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.kuhrusty.z15.R;
import com.kuhrusty.z15.SettingsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONScenarioRepository implements ScenarioRepository {
    private static final String LOGBIT = "JSONScenarioRepository";
    private List<Scenario> cache;
    private Context context;

    JSONScenarioRepository(Context context) {
        this.context = context;
    }

    private static class JsonPrettifier implements JsonDeserializer<Scenario> {
        @Override
        public Scenario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Scenario rv = new Scenario();
            JsonObject obj = json.getAsJsonObject();
            rv.setID(obj.get("id").getAsString());
            rv.setName(obj.get("name").getAsString());
            JsonElement te;
            if ((te = obj.get("difficulty")) != null) {
                String ts = te.getAsString();
                Scenario.Difficulty td;
                if (ts.equals("prologue")) td = Scenario.Difficulty.Prologue;
                else if (ts.equals("beginner")) td = Scenario.Difficulty.Beginner;
                else if (ts.equals("survivor")) td = Scenario.Difficulty.Survivor;
                else if (ts.equals("hero")) td = Scenario.Difficulty.Hero;
                else throw new JsonParseException("Scenario " + rv.getID() + ": bad difficulty \"" + ts + "\"");
                rv.setDifficulty(td);
            }
            if ((te = obj.get("soundtrack")) != null) {
                String ts = te.getAsString();
                Soundtrack.Type st;
                if (ts.equals("growl60")) st = Soundtrack.Type.Growl60;
                else if (ts.equals("growl40")) st = Soundtrack.Type.Growl40;
                else if (ts.equals("nogrowl")) st = Soundtrack.Type.NoGrowl;
                else throw new JsonParseException("Scenario " + rv.getID() + ": bad soundtrack \"" + ts + "\"");
                rv.setSoundtrackType(st);
            }
            if ((te = obj.get("zombieDeck")) != null) {
                String ts = te.getAsString();
                ZombieDeckShuffler zd;
                if (ts.equals("scenario2")) zd = Shufflers.Scenario2;
                else if (ts.equals("scenario3")) zd = Shufflers.Scenario3;
                else if (ts.equals("standard")) zd = Shufflers.Standard;
                else if (ts.equals("standardLetters")) zd = Shufflers.StandardLetters;
                else if (ts.equals("standardEvents")) zd = Shufflers.StandardEvents;
                else if (ts.equals("standardLettersEvents")) zd = Shufflers.StandardLettersEvents;
                else throw new JsonParseException("Scenario " + rv.getID() + ": bad zombieDeck \"" + ts + "\"");
                rv.setZombieDeckShuffler(zd);
            }
            if ((te = obj.get("cardsPerGrowl")) != null) {
                rv.setCardsPerGrowl(te.getAsInt());
            }
            return rv;
        }
    }

    private static GsonBuilder newGsonBuilder() {
        GsonBuilder rv = new GsonBuilder();
        rv.registerTypeAdapter(Scenario.class, new JsonPrettifier());
        return rv;
    }

    @Override
    public Scenario getScenario(String id) {
        List<Scenario> tm = getScenarios();
        //  linear search, but it's like 15 elements.
        for (int ii = 0; ii < tm.size(); ++ii) {
            if (tm.get(ii).getID().equals(id)) return tm.get(ii);
        }
        return null;
    }

    @Override
    public List<Scenario> getScenarios() {
        //  Rather than worrying about whether preferences have changed since
        //  we cached our list, let's cache the complete list, and filter out
        //  scenarios 16+ every time we get called.
        if (cache == null) {
            cache = getList(context, "scenarios.json", Scenario.class);
        }
        List<Scenario> tl = cache;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if ((sp == null) || (!sp.getBoolean(SettingsActivity.PREF_SHOW_KS_SCENARIOS, false))) {
            //  Remove 16+.
            ArrayList<Scenario> filtered = new ArrayList<>(tl.size());
            for (Scenario ts : tl) {
                int id;
                try {
                    id = Integer.parseInt(ts.getID());
                } catch (NumberFormatException nfe) {
                    continue;  //  well, it's not 1-15!
                }
                if (id <= 15) filtered.add(ts);
            }
            tl = filtered;
        }
        return new ArrayList<>(tl);
    }

    //  This guy came from MorbadScorepad JSONGameRepository.
    private <T> List<T> getList(Context context, String filename, Class<T> expectedClass) {
        String fullPath = context.getString(R.string.assets_subdir) + "/" + filename;
        InputStream is;
        try {
            is = context.getAssets().open(fullPath);
        } catch (IOException e) {
            Log.w(LOGBIT, "failed to find " + fullPath + " in assets");
            return null;
        }
        Gson gson = newGsonBuilder().create();
        JsonReader reader = null;
        List<T> rv = new ArrayList<T>();
        try {
            reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.setLenient(true);  //  so we can have embedded comments
            reader.beginArray();
            while (reader.hasNext()) {
                T te = gson.fromJson(reader, expectedClass);
                rv.add(te);
            }
            reader.endArray();
        } catch (IOException ioe) {
            Log.e(LOGBIT, "failed to read " + filename, ioe);
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
                else if (is != null) is.close();
            } catch (IOException ioe) {
                Log.e(LOGBIT, "failed to close " + filename, ioe);
            }
        }
        return rv;
    }
}
