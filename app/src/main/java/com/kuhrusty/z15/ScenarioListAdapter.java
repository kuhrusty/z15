package com.kuhrusty.z15;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kuhrusty.z15.model.RepositoryFactory;
import com.kuhrusty.z15.model.Scenario;
import com.kuhrusty.z15.model.ScenarioRepository;
import com.kuhrusty.z15.ui.UIUtil;

import java.util.List;

/**
 * This handles the display of the list of Scenarios in MainActivity.  It
 * listens for changes on shared preferences (set in MainActivity) so that it
 * can update the list if they toggle scenarios 16-20.
 */
public class ScenarioListAdapter
        extends RecyclerView.Adapter<ScenarioListAdapter.ScenarioViewHolder>
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final MainActivity activity;  //  real classy API
    private final RecyclerView recyclerView;
    private final List<Scenario> scenarios;

    public static class ScenarioViewHolder extends RecyclerView.ViewHolder {
        public String scenarioID;
        public TextView name;
        public ScenarioViewHolder(TextView tv) {
            super(tv);
            name = tv;
        }
    }

    public ScenarioListAdapter(MainActivity activity, RecyclerView rv) {
        this.activity = activity;
        this.recyclerView = rv;
        ScenarioRepository sr = RepositoryFactory.getScenarioRepository(activity);
        scenarios = sr.getScenarios();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(SettingsActivity.PREF_SHOW_KS_SCENARIOS)) {
            ScenarioRepository mr = RepositoryFactory.getScenarioRepository(activity);
            List<Scenario> tl = mr.getScenarios();
            if (tl.size() != scenarios.size()) {
                scenarios.clear();
                scenarios.addAll(tl);
                recyclerView.setAdapter(this);
            }
        }
    }

    @Override @NonNull
    public ScenarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_scenario, parent, false);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Scenario ts = scenarios.get(itemPosition);
                activity.startScenario(ts.getID());
            }
        });
        return new ScenarioViewHolder(tv);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ScenarioViewHolder holder, int position) {
        Scenario scenario = scenarios.get(position);
        holder.scenarioID = scenario.getID();
        UIUtil.formatScenarioTitle(holder.name, scenario);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return scenarios.size();
    }
}
