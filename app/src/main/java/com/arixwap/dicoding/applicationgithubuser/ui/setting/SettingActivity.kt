package com.arixwap.dicoding.applicationgithubuser.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.arixwap.dicoding.applicationgithubuser.R
import com.arixwap.dicoding.applicationgithubuser.databinding.ActivitySettingBinding
import com.arixwap.dicoding.applicationgithubuser.helper.ViewModelFactory
import com.arixwap.dicoding.applicationgithubuser.ui.main.dataStore

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.setting)
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this.application, pref)
        )[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this, { isDarkModeActive: Boolean ->
            binding.switchDarkMode.isChecked = isDarkModeActive
        })

        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
