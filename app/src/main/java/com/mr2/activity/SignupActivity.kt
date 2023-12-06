package com.mr2.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.gson.Gson
import com.mr2.R
import com.mr2.adapter.SectionsPagerAdapter
import retrofit2.Callback
import retrofit2.Response
import com.mr2.api.ApiClient

import com.mr2.databinding.ActivitySignupBinding
import com.mr2.model.TokenResponse
import com.mr2.model.UserData
import com.mr2.utils.TokenManager
import retrofit2.Call


class SignupActivity : ComponentActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }
    private fun initView() {






        binding.login.setOnClickListener {
            var username = binding.username.text.toString()
            var email = binding.email?.text.toString()
            var password = binding.password.text.toString()
            val userData = UserData(username, email, password)
            val call = ApiClient.apiService.signup(userData)
            if(username.isEmpty() == true || email.isEmpty() == true || password.isEmpty() == true ){
                Toast.makeText(this, "Can't send empty data", Toast.LENGTH_SHORT).show()
            }else{
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            val gson = Gson()
                            val jsonBody = response.body().toString()
                            val tokenResponse = gson.fromJson(jsonBody, TokenResponse::class.java)
                            TokenManager.saveTokens(this@SignupActivity, tokenResponse)
                            var intent = Intent(this@SignupActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@SignupActivity, "Can't send data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Handle failure
                    }
                })
            }


            //startActivity(Intent(this, EditActivity::class.java))
        }

    }

}
