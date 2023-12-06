package com.mr2.activity


/**
 * ce fichier permet à l'utilisateur de se connecter
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mr2.databinding.ActivityLoginBinding
import com.mr2.databinding.ActivityMainBinding

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

