package com.kuhrusty.z15.ui;

import android.support.v7.content.res.AppCompatResources;
import android.widget.TextView;

import com.kuhrusty.z15.R;
import com.kuhrusty.z15.model.Scenario;

import java.util.Locale;

public abstract class UIUtil {
    /**
     * Sets the text and background color of the given TextView based on the
     * given Scenario's name and difficulty.
     */
    public static void formatScenarioTitle(TextView tv, Scenario scenario) {
        if ((tv == null) || (scenario == null)) return;
        tv.setText(String.format(Locale.getDefault(),
                tv.getContext().getString(R.string.scenario_format),
                scenario.getID(), scenario.getName()));
        if (scenario.getDifficulty().equals(Scenario.Difficulty.Hero)) {
            tv.setBackground(AppCompatResources.getDrawable(tv.getContext(), R.drawable.red_gradient));
        } else if (scenario.getDifficulty().equals(Scenario.Difficulty.Survivor)) {
            tv.setBackground(AppCompatResources.getDrawable(tv.getContext(), R.drawable.green_gradient));
        } else {
            tv.setBackground(AppCompatResources.getDrawable(tv.getContext(), R.drawable.blue_gradient));
        }
    }
}
