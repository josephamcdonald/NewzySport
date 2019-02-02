package com.example.android.newzysport;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class NewzySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newzy_settings);
    }

    public static class NewzyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Find, assign and bind preference summaries to values.
            Preference newzysRetrieved = findPreference(getString(R.string.settings_newzys_to_retrieve_key));
            bindPreferenceSummaryToValue(newzysRetrieved);

            Preference newzysSortBy = findPreference(getString(R.string.settings_sort_newzys_by_key));
            bindPreferenceSummaryToValue(newzysSortBy);

            Preference newzysFromDate = findPreference(getString(R.string.settings_newzys_from_date_key));
            bindPreferenceSummaryToValue(newzysFromDate);

            Preference newzysToDate = findPreference(getString(R.string.settings_newzys_to_date_key));
            bindPreferenceSummaryToValue(newzysToDate);

            Preference newzysCustomSearch = findPreference(getString(R.string.settings_custom_newzys_key));
            bindPreferenceSummaryToValue(newzysCustomSearch);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();
            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {

                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }

            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), getString(R.string.empty_string));
            onPreferenceChange(preference, preferenceString);
        }
    }
}