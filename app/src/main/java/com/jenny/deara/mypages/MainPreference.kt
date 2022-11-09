package com.jenny.deara.mypages


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.jenny.deara.R

class MainPreference : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)

    }
}