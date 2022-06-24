package com.example.rifsa_mobile.view.authetication.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityLoginBinding
import com.example.rifsa_mobile.model.entity.remote.login.LoginBody
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.MainActivity
import com.example.rifsa_mobile.view.authetication.signup.SignUpActivity
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)

        binding.btnLoginSignup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        binding.btnLoginEnter.setOnClickListener {
            lifecycleScope.launch {
                postLogin()
            }
        }

    }


    private fun boxChecker(): Boolean{
        val email = binding.tvLoginEmail.text.toString()
        val password = binding.tvLoginPassword.text.toString()

        when{
            email.isEmpty() -> return true
            password.isEmpty() -> return true
            else -> (email.isNotEmpty() && password.isNotEmpty())
        }
        return false
    }

    private suspend fun postLogin(){
        val tempForm = LoginBody(
            binding.tvLoginEmail.text.toString(),
            binding.tvLoginPassword.text.toString()
        )
        val email =  binding.tvLoginEmail.text.toString()
        val password = binding.tvLoginPassword.text.toString()

        if (!boxChecker()){
            remoteViewModel.authLogin(email, password)
                .addOnSuccessListener {
                    saveLoginSession(
                        onBoard = true,
                        email,
                        "",
                        it.user?.uid.toString()
                    )
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }else{
            showStatus("Oops! Box masih kosong")
        }
    }

    //TODO | tunggu api node.js diperbaiki
    private fun loginPrediction(data : LoginBody,userId: String){
        lifecycleScope.launch {
            remoteViewModel.postLogin(data).observe(this@LoginActivity){
                when(it){
                    is FetchResult.Loading->{

                    }
                    is FetchResult.Success->{
                        showStatus("Login Berhasil")

                        //TODO | save login session

                    }
                    is FetchResult.Error->{
                        showStatus(it.error)
                    }
                }
            }
        }
    }

    private fun showStatus(title : String){
        binding.apply {
            pgtitleLogin.visibility = View.VISIBLE
            pgtitleLogin.text = title
        }
    }

    private fun saveLoginSession(onBoard : Boolean, name : String, pass: String, userId : String){
        authViewModel.saveUserPrefrences(onBoard,name,pass,userId)
        startActivity(Intent(this,MainActivity::class.java))
        finishAffinity()
    }
}