package com.example.rifsa_mobile.view.activity.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository

class MainActivityViewModel(
    private var preferences : PreferenceRespository
): ViewModel() {
    fun getUserThemeMode(): LiveData<Boolean> =
        preferences.getThemeMode()
}