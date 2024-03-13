package dev.proptit.messenger.ui.screen.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavHost
import dev.proptit.messenger.R
import dev.proptit.messenger.databinding.ActivityLoginBinding
import makeActivityTransparency

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        makeActivityTransparency()
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.white)
        window.navigationBarColor = ContextCompat.getColor(applicationContext, R.color.white)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHost
        val navController = navHostFragment.navController
    }
}