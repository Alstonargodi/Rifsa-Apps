package com.example.rifsa_mobile.model.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.rifsa_mobile.model.entity.preferences.ThemeModePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreTheme : DataStore<Preferences> by preferencesDataStore(
    name = "userThemePreferences"
)

class ThemeModePreferences(
    private val dataStore: DataStore<Preferences>
){
    private val isDarkMode = booleanPreferencesKey("themeMode_key")

    fun getUserThemeMode(): Flow<Boolean> {
        return dataStore.data.map {
            it[isDarkMode] ?: false
        }
    }

    suspend fun saveUserThemeMode(
        themeMode : Boolean
    ){
        dataStore.edit {
            it[isDarkMode] = themeMode
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ThemeModePreferences? = null
        fun getInstance(
            dataStore: DataStore<Preferences>
        ):ThemeModePreferences{
            return INSTANCE ?: synchronized(this){
                val instance = ThemeModePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}