package dev.proptit.messenger.ui.screen.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.R
import dev.proptit.messenger.databinding.ActivitySplashBinding
import dev.proptit.messenger.setup.key.Keys
import dev.proptit.messenger.ui.screen.login.LoginActivity
import dev.proptit.messenger.ui.screen.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import makeActivityTransparency
//splash api
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)


        makeActivityTransparency()

        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status_bar_color)
        window.navigationBarColor = ContextCompat.getColor(applicationContext, R.color.nav_bar_color)

        setContentView(binding.root)

        lifecycleScope.launch {
            delay(2000)
            if(Hawk.get(Keys.ID_USER, -1) == -1)
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}