package mobile.bts.com.viefund.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.thebluealliance.spectrum.SpectrumPreferenceCompat;

import mobile.bts.com.viefund.R;

/**
 * Created by THANH on 4/13/2018.
 */

public class PreferencesDemo extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
//        try {
//            addPreferencesFromResource(R.xml.demo_preferences);
//        } catch (Exception e) {
//            e.printStackTrace();
//            e.getCause().printStackTrace();
//        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (!SpectrumPreferenceCompat.onDisplayPreferenceDialog(preference, this)) {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
