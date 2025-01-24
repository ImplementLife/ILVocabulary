package com.il.lexicon.ui.fragment;

import android.widget.TextView;
import com.il.lexicon.R;
import com.il.lexicon.service.SettingsProvider;
import com.il.lexicon.ui.custom.component.BaseFragment;

public class SettingsFragment extends BaseFragment {
    private SettingsProvider settingsProvider;
    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    protected void init() {
        settingsProvider = new SettingsProvider(getContext());
        TextView etRepeatsCount = findViewById(R.id.et_n_repeats_count);
        etRepeatsCount.setText(Integer.toString(settingsProvider.getIntProperty("repeats_count", 3)));
        etRepeatsCount.setOnEditorActionListener((v, e, e2) -> {
            try {
                int i = Integer.parseInt(etRepeatsCount.getText().toString().trim());
                settingsProvider.setProperty("repeats_count", i);
            } catch (Exception ignore) {
                return false;
            }
            return true;
        });
    }

}