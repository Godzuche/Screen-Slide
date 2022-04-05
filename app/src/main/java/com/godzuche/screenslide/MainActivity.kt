package com.godzuche.screenslide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.godzuche.screenslide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ScreenSlidePagerFragment>(R.id.fragment_container_view)
        }

    }

    override fun onBackPressed() {
        b?.let {
            val index = it.invoke()
            if (index == 0) {
                super.onBackPressed()
            }
        }

    }

    companion object {
        var b: (() -> Int)? = null
        fun onBackButtonPressed(a: () -> Int) {
            b = a
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}