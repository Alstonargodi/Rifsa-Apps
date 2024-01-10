package com.example.rifsa_mobile.view.activity.mainactivity

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityMainBinding
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //todo dark mode
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)


        binding.mainBottommenu.visibility = View.VISIBLE
        val navControl = findNavController(R.id.mainnav_framgent)

        val themeMode = resources.configuration
        viewModel.getUserThemeMode().observe(this){userDarkMode ->
            if(userDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        binding.mainBottommenu.apply {
            setupWithNavController(navControl)
        }
    }



}