package com.example.rifsa_mobile.view.activity

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //todo dark mode
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)

        binding.mainBottommenu.visibility = View.VISIBLE
        val navControl = findNavController(R.id.mainnav_framgent)

        val themeMode = resources.configuration
        val currentTheme = themeMode.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when(currentTheme){
            Configuration.UI_MODE_NIGHT_NO ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Configuration.UI_MODE_NIGHT_YES ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        binding.mainBottommenu.apply {
            setupWithNavController(navControl)
        }
    }



}