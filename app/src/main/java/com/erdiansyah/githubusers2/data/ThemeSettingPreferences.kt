package com.erdiansyah.githubusers2.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow


class ThemeSettingPreferences private constructor(private val themeDataStore: DataStore<Preferences>){
    private val THEME_SET = booleanPreferencesKey("theme_setting")

    suspend fun saveTheme(isDarkTheme: Boolean) {
        themeDataStore.edit {
            it[THEME_SET] = isDarkTheme
        }
    }

    fun getTheme(): Flow<Boolean> {
        val dataMap = themeDataStore.data.map {
            it[THEME_SET] ?: false
        }
        return dataMap
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemeSettingPreferences? = null
        fun getInstance(themeDataStore: DataStore<Preferences>): ThemeSettingPreferences {
            val instance = INSTANCE?: synchronized(this) {
                val inst = ThemeSettingPreferences(themeDataStore)
                INSTANCE = inst
                inst
            }
            return instance
        }
    }

}