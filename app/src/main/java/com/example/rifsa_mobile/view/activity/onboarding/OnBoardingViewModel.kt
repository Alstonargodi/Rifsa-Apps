package com.example.rifsa_mobile.view.activity.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository

class OnBoardingViewModel(
    private var preferences: PreferenceRespository
): ViewModel(){
    fun getUserThemeMode(): LiveData<Boolean> =
        preferences.getThemeMode()
}