package com.il.lexicon.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import com.il.lexicon.R;
import com.il.lexicon.ui.custom.component.BaseFragment;

public class SettingsFragment extends BaseFragment {
    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    protected void init() {
        TextView etRepeatsCount = findViewById(R.id.et_n_repeats_count);
        etRepeatsCount.setText(Integer.toString(getIntProperty("repeats_count", 3)));
        etRepeatsCount.setOnEditorActionListener((v, e, e2) -> {
            try {
                int i = Integer.parseInt(etRepeatsCount.getText().toString().trim());
                setProperty("repeats_count", i);
            } catch (Exception ignore) {
                return false;
            }
            return true;
        });
    }

    public int getIntProperty(String tag, int defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences("il_settings", Context.MODE_PRIVATE);
        return sp.getInt(tag, defaultValue);
    }
    public int getIntProperty(String tag) {
        return getIntProperty(tag, 0);
    }

    public boolean getBoolProperty(String tag) {
        SharedPreferences sp = getContext().getSharedPreferences("il_settings", Context.MODE_PRIVATE);
        return sp.getBoolean(tag,true);
    }

    public void setProperty(String tag, boolean value) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("il_settings", Context.MODE_PRIVATE).edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }
    public void setProperty(String tag, int value) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("il_settings", Context.MODE_PRIVATE).edit();
        editor.putInt(tag, value);
        editor.apply();
    }

}