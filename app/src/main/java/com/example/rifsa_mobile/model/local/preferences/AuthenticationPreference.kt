package com.example.rifsa_mobile.model.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "userPrefrences")

class AuthenticationPreference(private val dataStore : DataStore<Preferences>) {

    private val onBoardKey  = booleanPreferencesKey("onBoard_key")
    private val nameKey = stringPreferencesKey("name_key")
    private val tokenKey = stringPreferencesKey("token_key")
    private val userIdKey = stringPreferencesKey("userId_key")

    private val locationLatitude = doublePreferencesKey("locationLat_key")
    private val locationLongitude = doublePreferencesKey("locationLong_key")
    private val isGetLocation = booleanPreferencesKey("isGetLocation_key")

    fun getOnBoardKey(): Flow<Boolean>{
        return dataStore.data.map {
            it[onBoardKey] ?: false
        }
    }

    fun getNameKey(): Flow<String>{
        return dataStore.data.map {
            it[nameKey] ?: ""
        }
    }

    fun getTokenKey(): Flow<String>{
        return dataStore.data.map {
            it[tokenKey] ?: ""
        }
    }

    fun getUserId(): Flow<String>{
        return dataStore.data.map {
            it[userIdKey] ?: ""
        }
    }

    fun getUserLocation(): Flow<List<Double>> {
        return dataStore.data.map {
            listOf(
                it[locationLongitude] ?: 0.0,
                it[locationLatitude] ?: 0.0
            )
        }
    }

    fun getLocationListener(): Flow<Boolean>{
        return dataStore.data.map {
            it[isGetLocation] ?: false
        }
    }

    suspend fun saveLocation(latitude : Double,longitude: Double){
        dataStore.edit {
            it[locationLatitude] = latitude
            it[locationLongitude] = longitude
        }
    }

    suspend fun setGetLocation(getlocation : Boolean){
        dataStore.edit {
            it[isGetLocation] = getlocation
        }
    }


    suspend fun savePreferences(onBoard : Boolean, name : String, tokenId:String, userId : String){
        dataStore.edit {
            it[onBoardKey] = onBoard
            it[nameKey] = name
            it[tokenKey] = tokenId
            it[userIdKey] = userId
        }
    }




    companion object{
        @Volatile
        private var INSTANCE: AuthenticationPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>):AuthenticationPreference{
            return INSTANCE ?: synchronized(this){
                val instance = AuthenticationPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }

}