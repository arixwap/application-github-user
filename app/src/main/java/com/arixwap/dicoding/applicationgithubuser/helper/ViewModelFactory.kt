package com.arixwap.dicoding.applicationgithubuser.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arixwap.dicoding.applicationgithubuser.ui.favorite.FavoriteViewModel
import com.arixwap.dicoding.applicationgithubuser.ui.setting.SettingPreferences
import com.arixwap.dicoding.applicationgithubuser.ui.setting.SettingViewModel

class ViewModelFactory(private val app: Application, private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, pref: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, pref)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SettingViewModel::class.java -> SettingViewModel(pref) as T
            FavoriteViewModel::class.java -> FavoriteViewModel(app) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
