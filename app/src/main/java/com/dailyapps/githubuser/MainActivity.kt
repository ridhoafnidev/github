package com.dailyapps.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dailyapps.githubuser.adapter.SectionsPagerAdapter
import com.dailyapps.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}