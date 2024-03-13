package dev.proptit.messenger.ui.screen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.proptit.messenger.R
import dev.proptit.messenger.databinding.ActivityMainBinding
import makeActivityTransparency

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        makeActivityTransparency()

        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.white)
        window.navigationBarColor = ContextCompat.getColor(applicationContext, R.color.white)

        setContentView(binding.root)

        val navView = binding.botNavView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHost
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if (destination.id == R.id.navigation_chats || destination.id == R.id.navigation_people || destination.id == R.id.navigation_discover){
                binding.botNavView.visibility = View.VISIBLE
            } else{
                binding.botNavView.visibility = View.GONE
            }

        }
    }
}